package gs.mail.engine.job;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gs.mail.engine.dto.Campaign;
import gs.mail.engine.dto.Send;
import gs.mail.engine.service.CampaignSendService;
import gs.mail.engine.util.SmtpSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class CampaignSendJob extends SmtpSocket {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private SimpleIncrementer simpleIncrementer;

    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    @Autowired private SqlSessionFactory sqlSessionFactory;

    @Value("${mail.smtp.send.log.path}") String dirPath;
    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.slave.cnt}") private int slaveCnt;

    @Autowired private RedisTemplate<String, String> redisTemplate;
    @Autowired private CampaignSendService campaignSendService;

    @Bean
    public Job campaignSendJobDetail() {
        try{
            return jobBuilderFactory.get("campaignSendJobDetail")
                    .incrementer(simpleIncrementer)
                    .start(campaignSchdlTasklet())
                    .listener(campaignSendListener())
                    .build();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public Step campaignSchdlTasklet()  {
        try {
            return stepBuilderFactory.get("campaignSchdlTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        long  schdlId = chunkContext.getStepContext().getStepExecution().getJobParameters().getLong("schdlId");
                        String filePath = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("filePath");
                        String schdlName = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("schdlName");
                        String sender = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("sender");
                        String senderName = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("senderName");
                        String title = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("subject");

                        log.info("### schdlId :{}", schdlId);
                        log.info("### filePath :{}", filePath);
                        log.info("### schdlName :{}", schdlName);
                        log.info("### sender :{}", sender);
                        log.info("### senderName :{}", senderName);
                        log.info("### title :{}", title);

                        Campaign campaign = new Campaign();
                        campaign.setSchdlId(schdlId);
                        campaign.setMasterSchdlId(schdlId);
                        campaign.setSchdlName(schdlName);
                        campaign.setSender(sender);
                        campaign.setTitle(title);

                        if(filePath != null){
                            StringBuffer sb = new StringBuffer();
                            File file = new File(filePath);
                            boolean isExists = file.exists();

                            if(isExists){
                                BufferedReader in = new BufferedReader(new FileReader(file));
                                String line;
                                while ((line = in.readLine()) != null) {
                                    sb.append(line);
                                }
                                in.close();

                                String htmlContents = sb.toString();
                                campaign.setContents(htmlContents);

                                Set<String> redisKeyList = redisTemplate.keys(schdlId+"*");
                                log.info("## : {}",schdlId);
                                log.info("############# : {}",redisKeyList.size());

                                for(String key : redisKeyList){
                                    String keyValue = redisTemplate.opsForValue().get(key);
                                    JsonArray array = new JsonParser().parse(keyValue).getAsJsonArray();

                                    long existingCnt = redisTemplate.countExistingKeys(redisKeyList);

                                    log.info("#### keyValue :{}", key);
                                    redisTemplate.delete(key);
                                    log.info("#### existingCnt : {}",existingCnt);

                                    if(existingCnt > 0){
                                        for(int i=0; i<array.size() && i < 1000; i++){
                                            campaign.setReceiver(array.get(i).getAsJsonObject().get("mbrAddress").toString());

                                            log.info("{}, {}, {}, {}, {}",
                                                    campaign.toString(), campaign.getContents(), campaign.getTitle(), campaign.getReceiver(), campaign.getSender());

                                            /** nio2 socket */
                                            socketSendNio2(campaign, "C", dirPath);
                                        }
                                    }else{

                                        //여기선 레디스에 같은 스케줄키로 잡혀있는건이 없다는것은
                                        // 모두 발송했다고 판단 : 결과를 발송완료로 치고 다시 안읽게 처리
                                        // 결과를 로그파일로 내릴예정 리스너가 해당 로그파일을 계속읽어서 세부 업데이트 처리
                                    }

                                }
                            }else{
                                log.info("Not Exists Contents File");
                            }
                        }
                        return RepeatStatus.FINISHED;
                    })
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    @JobScope
    public JobExecutionListener campaignSendListener() throws Exception{
        JobExecutionListener jobExecutionListener = new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                long schdlId = jobExecution.getJobParameters().getLong("schdlId");
                campaignSendService.setRunnging(schdlId, true);
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                long schdlId = jobExecution.getJobParameters().getLong("schdlId");
                campaignSendService.setRunnging(schdlId, false);
            }
        };

        return jobExecutionListener;
    }
}
