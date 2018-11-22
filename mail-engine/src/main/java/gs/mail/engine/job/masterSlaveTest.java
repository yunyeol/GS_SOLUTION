package gs.mail.engine.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class masterSlaveTest {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.data.processing.cnt}") private int slaveCnt;

    @Bean
    public Job masterSlaveTest1(){
            return jobBuilderFactory.get("masterSlaveTest1")
                    .incrementer(new RunIdIncrementer())
                    .start(masterSlaveTestStep())
                    .build();
    }

    @Bean
    public Step masterSlaveTestStep() {
        return stepBuilderFactory.get("masterStep")
                .partitioner("slaveStep", partitioner())
                .step(slaveStep())
                .gridSize(slaveCnt)
                .build();
    }

    public Partitioner partitioner(){
        Partitioner partitioner = new Partitioner() {
            @Override
            public Map<String, ExecutionContext> partition(int gridSize) {
                Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
                Map<String, Integer> selectQuery = new HashMap<String, Integer>();

                selectQuery = sqlSessionTemplate.selectOne("master.slave.test.selectData");

                int minValue = 0;
                int maxValue = 0;

                if(selectQuery != null){
                    minValue = selectQuery.get("MIN_ID");
                    maxValue = selectQuery.get("MAX_ID");
                }

                int targetSize = maxValue - minValue;
                long targetSizePerNode = (targetSize / gridSize ) + 1;

                if(targetSizePerNode <= gridSize){
                    targetSizePerNode = gridSize;
                }

                log.info("minValue : " + minValue+", maxValue : " + maxValue);
                log.info("targetSize : " + targetSize);
                log.info("targetSizePerNode : " + targetSizePerNode);

                int number = 0;
                long start = minValue;
                long end = start + targetSizePerNode - 1;
                while (start <= maxValue) {
                    ExecutionContext value = new ExecutionContext();
                    result.put("partition" + number, value);

                    if (end >= maxValue) {
                        end = maxValue;
                    }
                    value.putLong("minValue", start);
                    value.putLong("maxValue", end);

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

    @Bean
    @StepScope
    public Step slaveStep() {
        return  stepBuilderFactory.get("slaveStep")
                .chunk(commitInterval)
                .reader(slaveReder("minValue","maxValue"))
                .writer(slaveWriter())
                .build();
    }

    /**
     * myBatisItemReader
     * @param minValue
     * @param maxValue
     * @return
     */
    @Bean
    @StepScope
    public MyBatisCursorItemReader slaveReder(
            @Value("#{stepExecutionContext['minValue']}") String minValue,
            @Value("#{stepExecutionContext['maxValue']}") String maxValue) {

        log.info("minValue : " + minValue+", maxValue : " + maxValue);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("minValue",minValue);
        paramMap.put("maxValue",maxValue);

        MyBatisCursorItemReader reader = new MyBatisCursorItemReader();
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValues(paramMap);
        reader.setQueryId("master.slave.test.selectData2");
        return reader;
    }

    @Bean
    @StepScope
    public MyBatisBatchItemWriter slaveWriter() {
        MyBatisBatchItemWriter writer = new MyBatisBatchItemWriter();
        writer.setSqlSessionFactory(sqlSessionFactory);
        writer.setStatementId("master.slave.test.insertData");
        return  writer;
    }
}
