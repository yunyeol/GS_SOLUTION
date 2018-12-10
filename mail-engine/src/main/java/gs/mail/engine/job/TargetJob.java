package gs.mail.engine.job;

import gs.mail.engine.dto.Target;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class TargetJob {
    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    @Autowired private SqlSessionFactory sqlSessionFactory;
    @Autowired private TaskExecutor taskExecutor;

    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.slave.cnt}") private int slaveCnt;

    @Autowired private RedisTemplate<String, String> redisTemplate;

    @Bean
    public Job targetJobDetail() {
        try{
            FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("targetFlow");

            Flow flow = flowBuilder
                    .start(targetDbFileDecider())
                        .on("DB")
                        .to(targetDbMasterStep())
                    .from(targetDbFileDecider())
                        .on("FILE")
                        .to(targetFileMasterStep())
                    .from(targetDbFileDecider())
                        .on("COMPLETED")
                        .end()
                    .end();

            return jobBuilderFactory.get("targetJobDetail")
                    .incrementer(new SimpleIncrementer())
                    .start(flow)
                    .end()
                    .build();

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public JobExecutionDecider targetDbFileDecider(){
        JobExecutionDecider jobExecutionDecider = new JobExecutionDecider() {
            @Override
            public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
                String sendType = jobExecution.getJobParameters().getString("sendType");

                if(sendType != null && sendType.equals("C_D")){
                    return new FlowExecutionStatus("DB");
                }else if(sendType != null && sendType.equals("C_F")){
                    return new FlowExecutionStatus("FILE");
                }
                return FlowExecutionStatus.COMPLETED;
            }
        };
        return  jobExecutionDecider;
    }

    @Bean
    public Step targetDbMasterStep() {
        return stepBuilderFactory.get("targetMasterSendStep")
                .partitioner(targetDbSlavetStep())
                .partitioner("targetDbPartitioner", targetPartitioner(0L, 0L, ""))
                .taskExecutor(taskExecutor)
                .gridSize(slaveCnt)
                .build();
    }


    @Bean
    public Step targetDbSlavetStep(){
        try{
            return stepBuilderFactory.get("targetDbSlavetStep")
                    .<Target, Target>chunk(commitInterval)
                    .reader(targetDbReader(0L,0L,0L,0L))
                    .writer(targetWriter(0L,0L,""))
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader targetDbReader(
            @Value("#{stepExecutionContext['addressMinId']}") Long addressMinId,
            @Value("#{stepExecutionContext['addressMaxId']}") Long addressMaxId,
            @Value("#{jobParameters['schdlId']}") Long schdlId,
            @Value("#{jobParameters['addressGrpId']}") Long addressGrpId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schdlId", schdlId != null ? schdlId : 0);
        paramMap.put("addressGrpId", addressGrpId != null ? addressGrpId : 0);
        paramMap.put("addressMinId", addressMinId != null ? addressMinId : 0);
        paramMap.put("addressMaxId", addressMaxId != null ? addressMaxId : 0);

        MyBatisCursorItemReader reader = new MyBatisCursorItemReader();
        reader.setParameterValues(paramMap);
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setQueryId("SQL.Target.selectDbTargetList");
        return reader;
    }

    @Bean
    @StepScope
    @Transactional
    public ItemWriter<Target> targetWriter(@Value("#{jobParameters['schdlId']}") Long schdlId,
                                           @Value("#{jobParameters['addressGrpId']}") Long addressGrpId,
                                           @Value("#{jobParameters['sendType']}") String sendType) {
        ItemWriter<Target> writer = new ItemWriter<Target>() {
            @Override
            public void write(List<? extends Target> items) {
                try{
                    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

                    JSONArray jsonArray = new JSONArray();
                    for(Target target : items){
                        Map<String, Object> param = new HashMap<>();
                        param.put("schdlId", schdlId);
                        param.put("address", target.getMbrAddress());
                        param.put("data1", target.getData1());
                        param.put("data2", target.getData2());
                        param.put("data3", target.getData3());

                        if(sendType.equals("C_D")){
                            sqlSessionTemplate.insert("SQL.Target.insertSendRaw", param);
                        }else if(sendType.equals("C_F")){
                            param.put("sendFlag", "11");
                            param.put("rawId", target.getRawId());
                            sqlSessionTemplate.update("SQL.Target.updateSendRawFlag", param);
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("addressGrpId", target.getAddressGrpId());
                        jsonObject.put("addressGrpName", target.getAddressGrpName());
                        jsonObject.put("addressMbrId", target.getAddressMbrId());
                        jsonObject.put("mbrName", target.getMbrName());
                        jsonObject.put("mbrAddress", target.getMbrAddress());
                        jsonObject.put("data1", target.getData1());
                        jsonObject.put("data2", target.getData1());
                        jsonObject.put("data3", target.getData1());

                        jsonArray.put(jsonObject);
                    }
                    sqlSessionTemplate.flushStatements();

                    Map<String, Object> param = new HashMap<>();
                    param.put("schdlId", schdlId);
                    param.put("addressGrpId", addressGrpId);
                    param.put("sendFlag", "10");

                    redisTemplate.opsForValue().set(String.valueOf(schdlId)+"_"+items.get(0).getMbrAddress()
                                                    ,jsonArray.toString());

                    param.put("targetCnt", items.size());
                    sqlSessionTemplate.update("SQL.Target.updateSendCnt", param);

                }catch(Exception e){
                    Map<String, Object> param = new HashMap<>();
                    param.put("schdlId", schdlId);
                    param.put("sendFlag", "12");
                    sqlSessionTemplate.update("SQL.Target.updateSendSchldFlag", param);

                    e.printStackTrace();
                }
            }
        };
        return  writer;
    }

    @Bean
    @JobScope
    public Partitioner targetPartitioner(@Value("#{jobParameters['schdlId']}") Long schdlId,
                                           @Value("#{jobParameters['addressGrpId']}") Long addressGrpId,
                                           @Value("#{jobParameters['sendType']}") String sendType){
        Partitioner partitioner = new Partitioner() {
            @Override
            public Map<String, ExecutionContext> partition(int gridSize) {
                Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
                Map<String, Long> selectQuery = new HashMap<String, Long>();
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("schdlId", schdlId != null ? schdlId : 0);
                paramMap.put("addressGrpId", addressGrpId != null ? addressGrpId : 0);
                paramMap.put("sendFlag", "11");

                sqlSessionTemplate.update("SQL.Target.updateSendSchldFlag", paramMap);

                if(sendType.equals("C_D")){
                    selectQuery = sqlSessionTemplate.selectOne("SQL.Target.selectTargetDbMinMax", paramMap);
                }else if(sendType.equals("C_F")){
                    selectQuery = sqlSessionTemplate.selectOne("SQL.Target.selectTargetFileMinMax", paramMap);
                }

                long minValue = 0;
                long maxValue = 0;

                if(selectQuery != null && selectQuery.get("addressMinId") != null){
                    minValue = selectQuery.get("addressMinId");
                    maxValue = selectQuery.get("addressMaxId");
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
                    value.putLong("addressMinId", start);
                    value.putLong("addressMaxId", end);
                    log.info("target partition" + number+", "+start+","+end);

                    start += targetSizePerNode;
                    end += targetSizePerNode;
                    number++;
                }
                return result;
            }
        };
        return  partitioner;
    }

    /**
     * targetFileMasterStep
     * @return
     */
    @Bean
    public Step targetFileMasterStep() {
        return stepBuilderFactory.get("targetFileMasterStep")
                .partitioner(targetFileSlavetStep())
                .partitioner("targetFilePartitioner", targetPartitioner(0L, 0L, ""))
                .taskExecutor(taskExecutor)
                .gridSize(slaveCnt)
                .build();
    }

    @Bean
    public Step targetFileSlavetStep(){
        try{
            return stepBuilderFactory.get("targetFileSlavetStep")
                    .<Target, Target>chunk(commitInterval)
                    .reader(targetFIleReader(0L,0L, 0L))
                    .writer(targetWriter(0L,0L,""))
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader targetFIleReader(
            @Value("#{jobParameters['schdlId']}") Long schdlId,
            @Value("#{stepExecutionContext['addressMinId']}") Long addressMinId,
            @Value("#{stepExecutionContext['addressMaxId']}") Long addressMaxId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schdlId", schdlId != null ? schdlId : 0);
        paramMap.put("addressMinId", addressMinId != null ? addressMinId : 0);
        paramMap.put("addressMaxId", addressMaxId != null ? addressMaxId : 0);

        MyBatisCursorItemReader reader = new MyBatisCursorItemReader();
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValues(paramMap);
        reader.setQueryId("SQL.Target.selectFileTargetList");
        return reader;
    }
}
