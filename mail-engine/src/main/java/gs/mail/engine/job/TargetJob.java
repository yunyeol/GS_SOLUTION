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
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public Job targetJobDetail() {
        try{
            FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("Flow1");

            Flow flow = flowBuilder
                        .start(targetDbFileDecider())
                            .on("DB")
                            .end()
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
    public Step targetDbStep(){
        try{
            return stepBuilderFactory.get("targetDbStep")
                    .<Target, Target>chunk(commitInterval)
                    .reader(targetDbReader(0L,0L))
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
            @Value("#{jobParameters['addressGrpId']}") Long addressGrpId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schdlId", schdlId != null ? schdlId : 0);
        paramMap.put("addressGrpId", addressGrpId != null ? addressGrpId : 0);

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
                    log.info("33333");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        return  writer;
    }
}
