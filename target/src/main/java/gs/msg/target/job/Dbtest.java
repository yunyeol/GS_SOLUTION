package gs.msg.target.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Slf4j
@Configuration
public class Dbtest {
    @Autowired private SqlSessionFactory sqlSessionFactory;

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Value("${batch.commit.interval}") private int commitInterval;

    @Bean
    public Job simpleJob2() throws Exception {
        return jobBuilderFactory.get("simpleJob2")
                .start(simpleJobJDBCStep())
                .build();
    }

    @Bean
    public Step simpleJobJDBCStep()throws Exception  {
        return stepBuilderFactory.get("simpleJobJDBCStep")
                .chunk(commitInterval)
                .reader(reder())
               // .processor((Function<? super Object, ?>) processor())
                .writer(writer())
                .throttleLimit(8)
                .build();
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader reder() throws Exception {
        MyBatisCursorItemReader reader = new MyBatisCursorItemReader();
        //Map<String, Object> parameterValues = new HashMap<String, Object>();
        //parameterValues.put("status","A");
        //reader.setPageSize(1000);
        reader.setSqlSessionFactory(sqlSessionFactory);
        //reader.setParameterValues(parameterValues);
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
    @StepScope
    public MyBatisBatchItemWriter writer() throws Exception {
        MyBatisBatchItemWriter writer = new MyBatisBatchItemWriter();
        writer.setSqlSessionFactory(sqlSessionFactory);
        writer.setStatementId("dbaccess.mybatis.SampleMapper.select2");
        return  writer;
    }
}
