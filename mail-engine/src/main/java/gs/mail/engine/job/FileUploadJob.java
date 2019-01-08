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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class FileUploadJob {
    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private SimpleIncrementer simpleIncrementer;

    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    @Autowired private SqlSessionFactory sqlSessionFactory;
    @Autowired private TaskExecutor taskExecutor;

    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.slave.cnt}") private int slaveCnt;
    @Value("${file.target.path}") private String targetFilePath;

    @Bean
    public Job fileUploadJobDetail() {
        try{
            return jobBuilderFactory.get("fileUploadJobDetail")
                    .incrementer(simpleIncrementer)
                    .start(fileUploadTasklet())
                    //.next()
                    .build();

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Bean
    public Step fileUploadTasklet()  {
        try {
            return stepBuilderFactory.get("fileUploadTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        long  schdlId = chunkContext.getStepContext().getStepExecution().getJobParameters().getLong("schdlId");
                        String targetFile = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("targetFilePath");

                        File file = new File(targetFilePath + targetFile);
                        if(!file.isFile()){
                            log.info("TargetFile Not Exists!");
                            Map<String, Long> param = new HashMap<>();
                            param.put("schdlId", schdlId);
                            sqlSessionTemplate.update("SQL.FILE.UPLOAD.updateTargetFileNotExists", param);
                        }
                        return RepeatStatus.FINISHED;
                    })
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
