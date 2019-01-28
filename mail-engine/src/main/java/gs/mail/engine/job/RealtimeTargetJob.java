package gs.mail.engine.job;

import gs.mail.engine.dto.Realtime;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
import org.springframework.data.redis.core.RedisTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Configuration
public class RealtimeTargetJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private SimpleIncrementer simpleIncrementer;

    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    @Autowired private SqlSessionFactory sqlSessionFactory;

    @Autowired private TaskExecutor taskExecutor;

    @Value("${batch.slave.cnt}") private int slaveCnt;
    @Value("${batch.commit.interval}") private int commitInterval;

    @Autowired private RedisTemplate<String, String> redisTemplate;

    @Bean
    public Job realtimeTargetJobDetail() {
        try{
            return jobBuilderFactory.get("realtimeSendJobDetail")
                    .incrementer(simpleIncrementer)
                    .start(realtimeTargetTasklet())
                    .next(realtimeMasterTargetStep())
                    .build();

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Bean
    public Step realtimeTargetTasklet()  {
        try {
            return stepBuilderFactory.get("realtimeTargetTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        long  schdlId = chunkContext.getStepContext().getStepExecution().getJobParameters().getLong("schdlId");

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
    public Step realtimeMasterTargetStep() {
        try{
            return stepBuilderFactory.get("realtimeMasterTargetStep")
                    .partitioner(realtimeSlaveTargetStep())
                    .partitioner("realtimeTargetPatitioner", realtimePartitioner(0L))
                    .gridSize(slaveCnt)
                    .taskExecutor(taskExecutor)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public Step realtimeSlaveTargetStep(){
        try{
            return stepBuilderFactory.get("realtimeSlaveTargetStep")
                    .<Realtime, Realtime>chunk(commitInterval)
                    .reader(realtimeTargetQueueReader(0L,0L, 0L))
                    .writer(realtimeTargetQueueWriter(0L,0L))
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader realtimeTargetQueueReader(
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
    public ItemWriter<Realtime> realtimeTargetQueueWriter(@Value("#{stepExecutionContext['queueMinId']}") Long queueMinId,
                                                        @Value("#{stepExecutionContext['queueMaxId']}") Long queueMaxId) {
        ItemWriter<Realtime> writer = new ItemWriter<Realtime>() {
            @Override
            public void write(List<? extends Realtime> items) {
                try{
                    JSONArray jsonArray = new JSONArray();
                    for(Realtime realtime : items){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("schdlId", realtime.getSchdlId());
                        jsonObject.put("receiver", realtime.getReceiver());
                        jsonObject.put("uuid", realtime.getUuid());
                        jsonObject.put("title", realtime.getTitle());
                        jsonObject.put("contents", realtime.getContents());
                        jsonObject.put("map1", realtime.getMap1());
                        jsonObject.put("map2", realtime.getMap2());
                        jsonObject.put("map3", realtime.getMap3());
                        jsonObject.put("map4", realtime.getMap4());
                        jsonObject.put("map4", realtime.getMap4());
                        jsonObject.put("map5", realtime.getMap5());
                        jsonObject.put("map6", realtime.getMap6());
                        jsonObject.put("map7", realtime.getMap7());
                        jsonObject.put("map8", realtime.getMap8());
                        jsonObject.put("map9", realtime.getMap9());
                        jsonObject.put("map10", realtime.getMap10());
                        jsonObject.put("reservedate", realtime.getReserveDate());

                        jsonArray.put(jsonObject);

                        log.info("## : {}",jsonArray.toString());
                        String key = realtime.getSchdlId()+"_"+realtime.getUuid();
                        redisTemplate.opsForSet().add(key, jsonArray.toString());
                    }
                }catch(Exception e) {
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
}
