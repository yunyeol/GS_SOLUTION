package gs.mail.engine.job;

import gs.mail.engine.dto.Realtime;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@Configuration
public class RealtimeSendJob {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    @Autowired private SqlSessionFactory sqlSessionFactory;

    @Value("${batch.commit.interval}") private int commitInterval;

    @Value("${mail.smtp.host}") private String mailHost;
    @Value("${mail.smtp.port}") private String mailPort;
    @Value("${mail.smtp.protocol}") private String mailProtocol;

    @Bean
    public Job realtimeSendJobDetail() {
        try{
            return jobBuilderFactory.get("realtimeSendJobDetail")
                    //.incrementer(new RunIdIncrementer())
                    .start(realtimeSchdlTasklet())
                    .next(realtimeSendStep())
                    .build();

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * realtimeSchdlTasklet
     */
    @Bean
    public Step realtimeSchdlTasklet()  {
        try {
            return stepBuilderFactory.get("realtimeSchdlTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        final long  schdlId = chunkContext.getStepContext().getStepExecution().getJobParameters().getLong("schdlId");

                        Map<String, Long> param = new HashMap<String, Long>();
                        param.put("schdlId", schdlId);

                        List<Realtime> todaySchdl = sqlSessionTemplate.selectList("SQL.RealitmeSend.selectRealtimeTodaySchdl", param);

                        if(todaySchdl.size() == 0){
                            sqlSessionTemplate.insert("SQL.RealitmeSend.insertRealtimeTodaySchdl", param);
                        }

                        Map<String, Map<String, Long>> queueMap = sqlSessionTemplate.selectMap("SQL.RealitmeSend.selectMailQueueMinMax", param, "selectMailQueueMinMax");

                        long queueMinId = 0;
                        long queueMaxId = 0;

                        if(queueMap != null && queueMap.get("selectMailQueueMinMax").get("queueMinId") != null){
                            queueMinId = queueMap.get("selectMailQueueMinMax").get("queueMinId");
                            queueMaxId = queueMap.get("selectMailQueueMinMax").get("queueMaxId");
                            log.info("Tasklet schdlId : {}, minId : {}, maxId : {}",schdlId, queueMinId, queueMaxId);

                            param = new HashMap<String, Long>();
                            param.put("queueMinId", queueMinId);
                            param.put("queueMaxId",queueMaxId);
                            sqlSessionTemplate.update("SQL.RealitmeSend.updateTargetYn", param);
                        }

                        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().putLong("queueMinId", queueMinId);
                        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().putLong("queueMaxId", queueMaxId);

                        return RepeatStatus.FINISHED;
                    })
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * realtimeSendStep
     * @return
     */
    @Bean
    public Step realtimeSendStep(){
        try{
            return stepBuilderFactory.get("realtimeSendStep")
                    .<Realtime, Realtime>chunk(commitInterval)
                    .reader(realtimeSendQueueReader(0L,0L, 0L))
                    .processor(realtimeSendQueueProcessor())
                    .writer(realtimeSendQueueWriter())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader realtimeSendQueueReader(
            @Value("#{jobExecutionContext['queueMinId']}") Long queueMinId,
            @Value("#{jobExecutionContext['queueMaxId']}") Long queueMaxId,
            @Value("#{jobParameters['schdlId']}") Long schdlId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("queueMinId",queueMinId);
        paramMap.put("queueMaxId",queueMaxId);
        paramMap.put("schdlId", schdlId != null ? schdlId : 0);

        MyBatisCursorItemReader reader = new MyBatisCursorItemReader();
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValues(paramMap);
        reader.setQueryId("SQL.RealitmeSend.selectMailQueueList");
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<Realtime, Realtime> realtimeSendQueueProcessor(){
        ItemProcessor<Realtime, Realtime> pross = new ItemProcessor<Realtime, Realtime>() {
            @Override
            public Realtime process(Realtime item) throws Exception {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("schdlId", item.getSchdlId());
                paramMap.put("receiver", item.getReceiver());
                sqlSessionTemplate.insert("SQL.RealitmeSend.insertRealtimeQueRaw", paramMap);

                paramMap = new HashMap<String, Object>();
                paramMap.put("queId", item.getQueId());
                sqlSessionTemplate.delete("SQL.RealitmeSend.deleteMailQueue",paramMap);

                return item;
            }
        };
        return pross;
    }

    @Bean
    @StepScope
    public ItemWriter<Realtime> realtimeSendQueueWriter() throws Exception {
        ItemWriter<Realtime> writer = new ItemWriter<Realtime>() {
            @Override
            public void write(List<? extends Realtime> items) throws Exception {
                StringBuffer sb = new StringBuffer();
                File file = new File(items.get(0).getFilePath());
                boolean isExists = file.exists();

                if(isExists){
                    BufferedReader in = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    in.close();
                }
                String htmlContents = sb.toString();

                Properties prop = new Properties();
                prop.setProperty("mail.transport.protocol", mailProtocol);
                prop.setProperty("mail.smtp.host", mailHost);
                prop.setProperty("mail.smtp.port", mailPort);
                prop.setProperty( "mail.smtp.starttls.enable", "true");

                Session mailSession = Session.getDefaultInstance(prop, null);
                Message msg = new MimeMessage(mailSession);

                InternetAddress[] recipientAddress = new InternetAddress[items.size()];
                int cnt = 0;
                for(Realtime realtime : items){
                    msg.setSubject(realtime.getMailTitle());
                    msg.setContent(htmlContents.replace("${CONTENTS}", realtime.getMailContents()), "text/html; charset=utf-8");

                    recipientAddress[cnt] = new InternetAddress(realtime.getReceiver().trim());
                    cnt++;
                }
                msg.setFrom(new InternetAddress(items.get(0).getSender()));
                msg.setSentDate(new Date());
                msg.setRecipients(Message.RecipientType.TO, recipientAddress);
                Transport.send(msg);

                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("schdlId", items.get(0).getSchdlId());
                paramMap.put("sendCnt", cnt);
                paramMap.put("targetCnt", cnt);
                sqlSessionTemplate.insert("SQL.RealitmeSend.updateSchdlTargetSendCnt", paramMap);

            }
        };

        return  writer;
    }
}
