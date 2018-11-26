package gs.mail.engine.job;

import gs.mail.engine.dto.Realtime;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
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

import java.io.*;
import java.net.Socket;
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
    @Value("${batch.slave.cnt}") private int slaveCnt;

    @Value("${mail.smtp.host}") private String mailHost;
    @Value("${mail.smtp.port}") private String mailPort;
    @Value("${mail.smtp.protocol}") private String mailProtocol;

    private long schdlId;

    @Bean
    public Job realtimeSendJobDetail() {
        try{
            return jobBuilderFactory.get("realtimeSendJobDetail")
                    .start(realtimeSchdlTasklet())
                    .next(realtimeSendStep())
                    .listener(realtimeSendQueListener())
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

//    @Bean
//    public Step realtimeMasterSendStep() {
//        return stepBuilderFactory.get("realtimeMasterSendStep")
//                .partitioner("realtimeSlaveSendStep", partitioner())
//                .step(realtimeSlaveSendStep())
//                .gridSize(slaveCnt)
//                .build();
//    }

    /**
     * realtimeSendStep
     * @return
     */
    @Bean
   // public Step realtimeSlaveSendStep(){
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
//            @Value("#{stepExecutionContext['minValue']}") Long minValue,
//            @Value("#{stepExecutionContext['maxValue']}") Long maxValue,
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
    public ItemWriter<Realtime> realtimeSendQueueWriter() {
        ItemWriter<Realtime> writer = new ItemWriter<Realtime>() {
            @Override
            public void write(List<? extends Realtime> items) throws Exception {
                StringBuffer sb = new StringBuffer();
                File file = new File(items.get(0).getFilePath());
                boolean isExists = file.exists();

                if(isExists){
                    try{
                        BufferedReader in = new BufferedReader(new FileReader(file));
                        String line;
                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                        }
                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    log.info("Not Exists Contents File");
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

//                Socket socket = new Socket(mailHost, Integer.parseInt(mailPort));
//                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
//
//                String line;
//                //StringBuffer resultSb = new StringBuffer();
//                while((line = br.readLine()) != null){
//                    //resultSb.append(line);
//                    log.info("### 1 {}", line);
//                }
//                br.close();
//                socket.close();

                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("schdlId", items.get(0).getSchdlId());
                paramMap.put("sendCnt", cnt);
                paramMap.put("targetCnt", cnt);
                sqlSessionTemplate.insert("SQL.RealitmeSend.updateSchdlTargetSendCnt", paramMap);

            }
        };

        return  writer;
    }

    @Bean
    public JobExecutionListener realtimeSendQueListener() throws Exception{
        JobExecutionListener jobExecutionListener = new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                schdlId = jobExecution.getJobParameters().getLong("schdlId");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {

            }
        };

        return jobExecutionListener;
    }

//    public Partitioner partitioner(){
//        Partitioner partitioner = new Partitioner() {
//            @Override
//            public Map<String, ExecutionContext> partition(int gridSize) {
//                Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
//                Map<String, Long> selectQuery = new HashMap<String, Long>();
//
//                Map<String, Object> paramMap = new HashMap<String, Object>();
//                paramMap.put("schdlId", schdlId);
//
//                log.info("### : {}",schdlId);
//
//                selectQuery = sqlSessionTemplate.selectOne("SQL.RealitmeSend.selectMailQueueMinMax", paramMap);
//
//                log.info("### = {}",selectQuery);
//                long minValue = 0;
//                long maxValue = 0;
//
//                if(selectQuery != null && selectQuery.get("queueMinId") != null){
//                    minValue = selectQuery.get("queueMinId");
//                    maxValue = selectQuery.get("queueMaxId");
//                }
//
//                long targetSize = maxValue - minValue;
//                long targetSizePerNode = (targetSize / gridSize  ) + 1;
//
////                if(targetSizePerNode <= gridSize){
////                    targetSizePerNode = gridSize;
////                }
//
//                int number = 0;
//                long start = minValue;
//                long end = start + targetSizePerNode - 1;
//                while (start <= maxValue) {
//                    ExecutionContext value = new ExecutionContext();
//                    result.put("partition" + number, value);
//
//                    if (end >= maxValue) {
//                        end = maxValue;
//                    }
//                    value.putLong("minValue", start);
//                    value.putLong("maxValue", end);
//
//                    log.info("partition" + number+", "+start+","+end);
//
//                    start += targetSizePerNode;
//                    end += targetSizePerNode;
//                    number++;
//                }
//                return result;
//            }
//        };
//        return  partitioner;
//    }
}
