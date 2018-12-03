package gs.mail.engine.job;

import gs.mail.engine.dto.Target;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

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

    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.slave.cnt}") private int slaveCnt;

    @Autowired private RedisTemplate<String, String> redisTemplate;

    @Bean
    public Job targetJobDetail() {
        try{
            FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("Flow1");

            Flow flow = flowBuilder
                    .start(targetDbFileDecider())
                    .on("DB")
                    .to(targetMasterStep())
                    .from(targetDbFileDecider())
                    .on("FILE")
                    .end()
                    .build();

            return jobBuilderFactory.get("targetobDetail")
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

                log.info("######### : {}",sendType);

                if(sendType != null && sendType.equals("C_D")){
                    return new FlowExecutionStatus("DB");
                }else if(sendType != null && sendType.equals("C_F")){
                    return new FlowExecutionStatus("FILE");
                }
                return new FlowExecutionStatus("NULL");
            }
        };
        return  jobExecutionDecider;
    }

    @Bean
    public Step targetMasterStep() {
        return stepBuilderFactory.get("targetMasterSendStep")
                .partitioner("targetSlavePartitioner", targetDbPartitioner(0L, 0L))
                .step(targetDbSlavetStep())
                .gridSize(slaveCnt)
                .build();
    }

    @Bean
    public Step targetDbSlavetStep(){
        try{
            return stepBuilderFactory.get("targetDbStep")
                    .<Target, Target>chunk(commitInterval)
                    .reader(targetDbReader(0L,0L, 0L, 0L))
                    //.processor(realtimeSendQueueProcessor())
                    .writer(targetWriter())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader targetDbReader(
            @Value("#{jobParameters['schdlId']}") Long schdlId,
            @Value("#{jobParameters['addressGrpId']}") Long addressGrpId,
            @Value("#{jobParameters['addressMinId']}") Long addressMinId,
            @Value("#{jobParameters['addressMaxId']}") Long addressMaxId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schdlId", schdlId != null ? schdlId : 0);
        paramMap.put("addressGrpId", addressGrpId != null ? addressGrpId : 0);
        paramMap.put("addressMinId", addressMinId != null ? addressMinId : 0);
        paramMap.put("addressMaxId", addressMaxId != null ? addressMaxId : 0);

        MyBatisCursorItemReader reader = new MyBatisCursorItemReader();
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValues(paramMap);
        reader.setQueryId("SQL.Target.selectDbTargetList");
        return reader;
    }

    @Bean
    public ItemWriter<Target> targetWriter() {
        ItemWriter<Target> writer = new ItemWriter<Target>() {
            @Override
            public void write(List<? extends Target> items) {
                try{
                    log.info(items.toString());
                    redisTemplate.opsForValue().set("test","test123123123");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        return  writer;
    }

    @Bean
    @JobScope
    public Partitioner targetDbPartitioner(@Value("#{jobParameters['schdlId']}") Long schdlId,
                                           @Value("#{jobParameters['schdlId']}") Long addressGrpId){
        Partitioner partitioner = new Partitioner() {
            @Override
            public Map<String, ExecutionContext> partition(int gridSize) {
                Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
                Map<String, Long> selectQuery = new HashMap<String, Long>();
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("schdlId", schdlId != null ? schdlId : 0);
                paramMap.put("addressGrpId", addressGrpId != null ? addressGrpId : 0);

                selectQuery = sqlSessionTemplate.selectOne("SQL.Target.selectTargetDbMinMax", paramMap);

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
                    log.info("partition" + number+", "+start+","+end);

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
