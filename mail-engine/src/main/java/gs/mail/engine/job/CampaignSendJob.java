package gs.mail.engine.job;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gs.mail.engine.dto.Campaign;
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

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class CampaignSendJob {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    @Autowired private SqlSessionFactory sqlSessionFactory;

    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.slave.cnt}") private int slaveCnt;

    @Autowired private RedisTemplate<String, String> redisTemplate;

    @Bean
    public Job campaignSendJobDetail() {
        try{
            return jobBuilderFactory.get("campaignSendJobDetail")
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

                        Set<String> redisKeyList = redisTemplate.keys(schdlId+"*");

                        for(String key : redisKeyList){
                            String keyValue = redisTemplate.opsForValue().get(key);
                            JsonArray array = new JsonParser().parse(keyValue).getAsJsonArray();
                            log.info("########3 test : {}", array);

                            long existingCnt = redisTemplate.countExistingKeys(redisKeyList);
                            //발송전에 읽은 키는 삭제한다.
                            log.info("#### keyValue :{}", key);
                            redisTemplate.delete(key);
                            log.info("#### existingCnt : {}",existingCnt);

                            if(existingCnt > 0){
                                for(int i=0; i<array.size(); i++){
                                    //건건이 메일을 발송한다. 건건히 발송해도 엄청빠를것같음
                                    log.info("### : schdlId : {}", schdlId);
                                    log.info("### : filePath : {}", filePath);
                                    log.info("### : schdlName : {}", schdlName);
                                    log.info("### : sender : {}", sender);

                                    log.info("### : addressGrpId : {}", array.get(i).getAsJsonObject().get("addressGrpId"));
                                    log.info("### : addressMbrId : {}", array.get(i).getAsJsonObject().get("addressMbrId"));
                                    log.info("### : mbrAddress : {}", array.get(i).getAsJsonObject().get("mbrAddress"));
                                    log.info("### : data1 : {}", array.get(i).getAsJsonObject().get("data1"));
                                    log.info("### : data2 : {}", array.get(i).getAsJsonObject().get("data2"));
                                    log.info("### : data3 : {}", array.get(i).getAsJsonObject().get("data3"));

                                    //메일 소켓 통신 메소드 추가예정 파라미터만 위에 잘 정해서 넣어주면 발송되도록 개발
                                }
                            }else{

                                //여기선 레디스에 같은 스케줄키로 잡혀있는건이 없다는것은
                                // 모두 발송했다고 판단 : 결과를 발송완료로 치고 다시 안읽게 처리
                                // 결과를 로그파일로 내릴예정 리스너가 해당 로그파일을 계속읽어서 세부 업데이트 처리
                            }

                        }
                        long end =0;
                        long start = 0;
                        start=System.currentTimeMillis();
                        for(int i=0; i<3000000; i++){
                            log.info("## i : {}", i);
                        }
                        end=System.currentTimeMillis();
                        long result=end-start;
                        double seconds= TimeUnit.MILLISECONDS.toSeconds(result);
                        log.info("########## second : {}", seconds);
//                        if(keyName != null && !keyName.equals("")){
//                            chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
//                                    .putString("sendKeyName", keyName.substring(0, keyName.lastIndexOf(",")));
//                        }
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
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                try{
                    log.info("##### campaign listener afterjob");
//                    Socket socket = new Socket(mailHost, Integer.parseInt(mailPort));
//                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
//
//                    String line;
//                    //StringBuffer resultSb = new StringBuffer();
//                    while((line = br.readLine()) != null){
//                        //resultSb.append(line);
//                        log.info("###  {}", line);
//                    }
//                    br.close();
//                    socket.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        return jobExecutionListener;
    }
}
