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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    @Value("${spring.smtp.host}") private String mailHost;

    @Bean
    public Job realtimeSendJobDetail() {
        try{
            return jobBuilderFactory.get("realtimeSendJobDetail")
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
                            log.info("queue schdlId : {}, minId : {}, maxId : {}",schdlId, queueMinId, queueMaxId);
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
     * realtimeTargetStep
     * @return
     */
    @Bean
    public Step realtimeSendStep(){
        try{
            return stepBuilderFactory.get("realtimeSendStep")
                    .<Realtime, Realtime>chunk(commitInterval)
                    .reader(realtimeSendQueueReader("queueMinId","queueMaxId", 0L))
                    .processor(realtimeSendQueueProcessor())
                    .writer(realtimeSendQueueWriter(0L))
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader realtimeSendQueueReader(
            @Value("#{jobExecutionContext['queueMinId']}") String queueMinId,
            @Value("#{jobExecutionContext['queueMaxId']}") String queueMaxId,
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
                paramMap.put("schdlId", item.getMasterSchdlId());
                paramMap.put("receiver", item.getReceiver());

                sqlSessionTemplate.update("SQL.RealitmeSend.updateTargetYn", paramMap);

                paramMap = new HashMap<String, Object>();
                paramMap.put("schdlId", item.getSchdlId());
                paramMap.put("receiver", item.getReceiver());

                sqlSessionTemplate.insert("SQL.RealitmeSend.insertRealtimeQueRaw", paramMap);

                return item;
            }
        };
        return pross;
    }

    @Bean
    @StepScope
    public ItemWriter<Realtime> realtimeSendQueueWriter(@Value("#{jobParameters['schdlId']}") Long schdlId) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schdlId", schdlId);

        ItemWriter<Realtime> writer = new ItemWriter<Realtime>() {
            @Override
            public void write(List<? extends Realtime> items) throws Exception {
                log.info("### write : {}", items.toString());

                Properties prop = new Properties();
                prop.setProperty("mail.transport.protocol", "smtp");
                prop.setProperty("mail.smtp.host", "106.10.51.181");
                prop.setProperty("mail.smtp.port", "25");
                prop.setProperty( "mail.smtp.starttls.enable", "true");

                Session mailSession = Session.getDefaultInstance(prop, null);

                Message msg = new MimeMessage(mailSession);


                for(Realtime realtime : items){
                    msg.setFrom(new InternetAddress(realtime.getSender()));
                    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(realtime.getReceiver(), false));
                    msg.setSubject("제목입니다.");
                    msg.setContent(realtime.getMailContents(), "text/html; charset=utf-8");
                    msg.setSentDate(new Date());

                    Transport.send(msg);
                }
            }
        };

        return  writer;
    }

    /**
     * realtimeSendStep
     */

}
