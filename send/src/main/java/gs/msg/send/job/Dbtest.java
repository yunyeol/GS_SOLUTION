package gs.msg.send.job;

import gs.msg.send.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class Dbtest {
    @Autowired public SqlSessionTemplate sqlSessionTemplate;
    @Autowired public Config config1;

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob2() throws Exception {
        return jobBuilderFactory.get("simpleJob2")
                .start(simpleJobJDBCStep())
                .build();
    }

    @Bean
    public Step simpleJobJDBCStep()throws Exception  {
        return stepBuilderFactory.get("simpleJobJDBCStep")
                .chunk(200)
                .reader(reder())
               // .processor((Function<? super Object, ?>) processor())
                .writer(writer())
                .taskExecutor(config1.executor())
                .build();
    }

    @Bean
    public MyBatisCursorItemReader reder() throws Exception {
        MyBatisCursorItemReader reader = new MyBatisCursorItemReader();
        Map<String, Object> parameterValues = new HashMap<String, Object>();
        parameterValues.put("status","A");
        //reader.setPageSize(1000);
        reader.setSqlSessionFactory(config1.sqlSessionFactory());
        reader.setParameterValues(parameterValues);
        reader.setQueryId("dbaccess.mybatis.SampleMapper.select1");
        return reader;
    }

//    @Bean
//    public ItemProcessor processor() {
//        ItemProcessor pross = new ItemProcessor() {
//            @Override
//            public Object processItem(Object item) throws Exception {
//                log.info("## item : {}", item.toString());
//                return null;
//            }
//        };
//        return pross;
//    }

    @Bean
    public MyBatisBatchItemWriter writer() throws Exception {
        MyBatisBatchItemWriter writer = new MyBatisBatchItemWriter();
        writer.setSqlSessionFactory(config1.sqlSessionFactory());
        writer.setStatementId("dbaccess.mybatis.SampleMapper.select2");
        return  writer;
    }
}
