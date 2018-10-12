package gs.msg.send.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
public class Config {

    /**
     * DB CONFIG
     * DATASOURCE 설정, 히카리데이터소스 사용
     * 트랜잭션 매니저 설정
     * MyBatis연동으로인한 SqlSessionFactory, SqlSessionTemplate 선언
     */
	@Bean(destroyMethod = "close")
	@ConfigurationProperties("spring.datasource")
	public HikariDataSource dataSource(){
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/mybatis-config.xml"));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/sql/**/*.xml"));
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }


    /**
     * TASK CONFIG
     * 멀티쓰레드를 사용하기 위한 ThreadPoolTaskExecutor 설정
     */
    @Value("${executor.core.pool.size}") private int corePool;
    @Value("${executor.max.pool.size}") private int maxPool;
    @Value("${executor.que.capacity}") private int queCapacity;

    @Bean
    public TaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePool);
        executor.setMaxPoolSize(maxPool);
        executor.setQueueCapacity(queCapacity);
        return executor;
    }

    /**
     * BATCH CONFIG
     * 여러 JOB들이 동시에 돌아갈수 있도록 MapJobRepositoryFactoryBean, JobRepository, SimpleJobLauncher 설정
     */
    @Bean
    public JobExplorerFactoryBean jobExplorerFactoryBean(){
        JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setDataSource(dataSource());
        return jobExplorerFactoryBean;
    }

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean(PlatformTransactionManager txManager) throws Exception {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(txManager);
        mapJobRepositoryFactoryBean.afterPropertiesSet();
        return mapJobRepositoryFactoryBean;
    }

    @Bean
    public JobRepository jobRepository(MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean) throws Exception {
        return mapJobRepositoryFactoryBean.getObject();
    }

    @Bean
    public SimpleJobLauncher simpleJobLauncher(JobRepository jobRepository){
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(executor());
        return simpleJobLauncher;
    }
}
