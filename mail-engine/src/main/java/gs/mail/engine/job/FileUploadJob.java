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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

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
                    .next(fileUploadStep())
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

    @Bean
    public Step fileUploadStep(){
        try{
            return stepBuilderFactory.get("fileUploadStep")
                    .<Target, Target>chunk(commitInterval)
                    .reader(fileItemReader(""))
                    .writer(fileUPloadWriter())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    @StepScope
    public FlatFileItemReader fileItemReader(@Value("#{jobParameters['targetFilePath']}") String targetFile){
        FlatFileItemReader fileItemReader = new FlatFileItemReader();
        fileItemReader.setResource(new FileSystemResource(targetFilePath + targetFile));
        DefaultLineMapper lineMapper = new DefaultLineMapper();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter("|");

        FieldSetMapper fieldSetMapper = new FieldSetMapper() {
            @Override
            public Object mapFieldSet(FieldSet fieldSet) throws BindException {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("address", fieldSet.readString(0));
                item.put("name", fieldSet.readString(1));
                item.put("map1", fieldSet.readString(2));
                item.put("map2", fieldSet.readString(3));
                item.put("map3", fieldSet.readString(4));
                item.put("map4", fieldSet.readString(5));
                item.put("map5", fieldSet.readString(6));
                item.put("map6", fieldSet.readString(7));
                item.put("map7", fieldSet.readString(8));
                item.put("map8", fieldSet.readString(9));
                item.put("map9", fieldSet.readString(10));
                item.put("map10", fieldSet.readString(11));
                return item;
            }
        };
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        fileItemReader.setLineMapper(lineMapper);
        fileItemReader.setEncoding("UTF-8");

        return fileItemReader;
    }

    @Bean
    @StepScope
    public ItemWriter fileUPloadWriter(){
        ItemWriter itemWriter = new ItemWriter() {
            @Override
            public void write(List items) throws Exception {

            }
        };

        return itemWriter;
    }
}
