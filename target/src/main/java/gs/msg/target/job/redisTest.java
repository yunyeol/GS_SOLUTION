package gs.msg.target.job;

import com.zaxxer.hikari.HikariDataSource;
import gs.msg.target.dto.testDto;
import gs.msg.target.mapper.testmapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class redisTest {

    @Autowired private HikariDataSource dataSource;
    @Autowired private SqlSessionFactory sqlSessionFactory;
    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Autowired private testmapper testMapper;

    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.data.processing.cnt}") private int slaveCnt;

    @Bean
    public Job redisTestJob() {
        return jobBuilderFactory.get("redisTestJob")
                .incrementer(new RunIdIncrementer())
                .start(redisTestStep())
                .build();
    }

    @Bean
    public Step redisTestStep() {
        return stepBuilderFactory.get("redisTestStep")
                .partitioner("redisSlaveStep", partitioner1())
                .step(redisSlaveStep())
                .gridSize(slaveCnt)
                .build();
    }

    public Partitioner partitioner1(){
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
    public Step redisSlaveStep() {
        return  stepBuilderFactory.get("redisSlaveStep")
                .chunk(commitInterval)
                .reader(jdbcCursorItemReader("minValue","maxValue"))
                .writer(redisSlaveWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader jdbcCursorItemReader(
            @Value("#{stepExecutionContext['minValue']}") String minValue,
            @Value("#{stepExecutionContext['maxValue']}") String maxValue){

        log.info("Redis minValue : " + minValue+", maxValue : " + maxValue);

        String sql = "SELECT " +
            "              id id, " +
            "              TEST test " +
            "        FROM test " +
            "        WHERE id BETWEEN "+ minValue +" AND " +maxValue +
            "        AND readflag ='N'";

        JdbcCursorItemReader reader = new JdbcCursorItemReader();
        reader.setDataSource(dataSource);
        reader.setSql(sql);
        reader.setRowMapper(testMapper);


        return reader;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter writer(){
        JdbcBatchItemWriter writer = new JdbcBatchItemWriter();
        writer.setDataSource(dataSource);
        writer.setItemSqlParameterSourceProvider();
        return writer;
    }

    @Bean
    public ItemSqlParameterSourceProvider<testDto> setter(){

    }
}
