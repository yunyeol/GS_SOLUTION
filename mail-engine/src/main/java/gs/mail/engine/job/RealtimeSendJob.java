package gs.mail.engine.job;

import gs.mail.engine.dto.Realtime;
import gs.mail.engine.service.RealtimeSendService;
import gs.mail.engine.util.SmtpSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.io.*;
import java.util.*;

@Slf4j
@Configuration
public class RealtimeSendJob extends SmtpSocket {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private SimpleIncrementer simpleIncrementer;

    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    @Autowired private SqlSessionFactory sqlSessionFactory;

    @Autowired private TaskExecutor taskExecutor;

    @Autowired private RealtimeSendService realtimeSendService;

    @Value("${mail.smtp.send.log.path}") String dirPath;
    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.slave.cnt}") private int slaveCnt;

    @Bean
    public Job realtimeSendJobDetail() {
        try{
            return jobBuilderFactory.get("realtimeSendJobDetail")
                    .incrementer(simpleIncrementer)
                    .start(realtimeSchdlTasklet())
                    .next(realtimeMasterSendStep())
                    .listener(realtimeSendListener())
                    .build();

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Bean
    public Step realtimeSchdlTasklet()  {
        try {
            return stepBuilderFactory.get("realtimeSchdlTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        final long  schdlId = chunkContext.getStepContext().getStepExecution().getJobParameters().getLong("schdlId");

                        Map<String, Long> param = new HashMap<>();
                        param.put("schdlId", schdlId);

                        List<Realtime> todaySchdl = sqlSessionTemplate.selectList("SQL.RealitmeSend.selectRealtimeTodaySchdl", param);
                        if(todaySchdl.size() == 0){
                            sqlSessionTemplate.insert("SQL.RealitmeSend.insertRealtimeTodaySchdl", param);
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
    public JobExecutionListener realtimeSendListener() throws Exception{
        JobExecutionListener jobExecutionListener = new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                long schdlId = jobExecution.getJobParameters().getLong("schdlId");
                realtimeSendService.setRunnging(schdlId, true);
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                long schdlId = jobExecution.getJobParameters().getLong("schdlId");
                realtimeSendService.setRunnging(schdlId, false);
            }
        };

        return jobExecutionListener;
    }

    @Bean
    public Step realtimeMasterSendStep() {
        try{
            return stepBuilderFactory.get("realtimeMasterSendStep")
                    .partitioner(realtimeSlaveSendStep())
                    .partitioner("realtimePatitioner", realtimePartitioner(0L))
                    .gridSize(slaveCnt)
                    .taskExecutor(taskExecutor)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public Step realtimeSlaveSendStep(){
        try{
            return stepBuilderFactory.get("realtimeSlaveSendStep")
                    .<Realtime, Realtime>chunk(commitInterval)
                    .reader(realtimeSendQueueReader(0L,0L, 0L))
                    .processor(realtimeSendQueueProcessor(0L,0L))
                    .writer(realtimeSendQueueWriter(0L,0L))
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader realtimeSendQueueReader(
            @Value("#{stepExecutionContext['queueMinId']}") Long queueMinId,
            @Value("#{stepExecutionContext['queueMaxId']}") Long queueMaxId,
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
    public ItemProcessor<Realtime, Realtime> realtimeSendQueueProcessor(
                                @Value("#{stepExecutionContext['queueMinId']}") Long queueMinId,
                                @Value("#{stepExecutionContext['queueMaxId']}") Long queueMaxId){
        ItemProcessor<Realtime, Realtime> pross = new ItemProcessor<Realtime, Realtime>() {
            @Override
            public Realtime process(Realtime item) throws Exception {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("queueMinId",queueMinId);
                paramMap.put("queueMaxId",queueMaxId);

                sqlSessionTemplate.insert("SQL.RealitmeSend.insertRealtimeQueRaw", paramMap);
                sqlSessionTemplate.delete("SQL.RealitmeSend.deleteMailQueue",paramMap);
                return item;
            }
        };
        return pross;
    }

    @Bean
    @StepScope
    public ItemWriter<Realtime> realtimeSendQueueWriter(@Value("#{stepExecutionContext['queueMinId']}") Long queueMinId,
                                                        @Value("#{stepExecutionContext['queueMaxId']}") Long queueMaxId) {
        ItemWriter<Realtime> writer = new ItemWriter<Realtime>() {
            @Override
            public void write(List<? extends Realtime> items) {
                try{
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

                        String htmlContents = sb.toString();

                        int cnt = 0;
                        for(Realtime realtime : items){
                            realtime.setContents(htmlContents.replace("${CONTENTS}", new String(realtime.getContents().getBytes("UTF-8"))));
                            log.info("{}, {}, {}, {}, {}",
                                    realtime.toString(), realtime.getContents(), realtime.getTitle(), realtime.getReceiver(), realtime.getSender());

                            /** nio2 socket */
                            socketSendNio2(realtime, "R", dirPath);

                            cnt++;
                        }

                        updateSchdlCnt(items.get(0).getSchdlId(), cnt, cnt, 0, 0);
                    }else{
                        log.info("Not Exists Contents File");
                        updateSchdlCnt(items.get(0).getSchdlId(), 0, 0, 0, items.size());

                        for(Realtime realtime : items){
                            HashMap<String, Object> paramMap = new HashMap<String, Object>();
                            paramMap.put("uuid", realtime.getUuid());
                            paramMap.put("sendFlag","50");
                            paramMap.put("resultCd","1100");
                            sqlSessionTemplate.update("SQL.RealitmeSend.updateFailSchdlRaw", paramMap);
                        }
                    }
                }catch(Exception e) {
                    updateSchdlCnt(items.get(0).getSchdlId(), 0, 0, 0, items.size());
                    e.printStackTrace();
                }
            }
        };
        return  writer;
    }

    @Bean
    @JobScope
    public Partitioner realtimePartitioner(@Value("#{jobParameters['schdlId']}") Long schdlId){
        Partitioner partitioner = new Partitioner() {
            @Override
            public Map<String, ExecutionContext> partition(int gridSize) {
                Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
                Map<String, Long> selectQuery = new HashMap<String, Long>();
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("schdlId", schdlId != null ? schdlId : 0);

                long minValue = 0;
                long maxValue = 0;

                selectQuery = sqlSessionTemplate.selectOne("SQL.RealitmeSend.selectMailQueueMinMax", paramMap);

                if(selectQuery != null && selectQuery.get("queueMinId") != null){
                    minValue = selectQuery.get("queueMinId");
                    maxValue = selectQuery.get("queueMaxId");

                    HashMap<String, Long> param = new HashMap<String, Long>();
                    param.put("queueMinId", minValue);
                    param.put("queueMaxId",maxValue);
                    sqlSessionTemplate.update("SQL.RealitmeSend.updateTargetYn", param);
                }

                long targetSize = maxValue - minValue;
                long targetSizePerNode = (targetSize / gridSize) + 1;

                if(targetSizePerNode <= gridSize){
                    targetSizePerNode = gridSize;
                }

                int number = 0;
                long start = minValue;
                long end = start + targetSizePerNode - 1;
                while (start <= maxValue) {
                    ExecutionContext value = new ExecutionContext();
                    result.put("partition" + number, value);

                    if (end >= maxValue) {
                        end = maxValue;
                    }
                    value.putLong("queueMinId", start);
                    value.putLong("queueMaxId", end);
                    log.info("realtime partition" + number+", "+start+","+end);

                    start += targetSizePerNode;
                    end += targetSizePerNode;
                    number++;
                }
                return result;
            }
        };
        return  partitioner;
    }

    private void updateSchdlCnt(long schdlId, int sendCnt, int targetCnt, int succesCnt, int failCnt){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schdlId", schdlId);
        paramMap.put("sendCnt", sendCnt);
        paramMap.put("targetCnt", targetCnt);
        paramMap.put("successCnt", succesCnt);
        paramMap.put("failCnt", failCnt);
        sqlSessionTemplate.insert("SQL.RealitmeSend.updateSchdlCnt", paramMap);
    }
}
