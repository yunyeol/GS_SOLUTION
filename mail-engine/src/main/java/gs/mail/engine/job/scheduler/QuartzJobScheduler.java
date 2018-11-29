package gs.mail.engine.job.scheduler;



import gs.mail.engine.job.scheduler.executor.RealtimeSendExecutor;
import gs.mail.engine.job.scheduler.executor.TargetExecutor;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzJobScheduler {
    @Value("${cron.mail.realtime}") private String realtimeCron;
    @Value("${cron.mail.target}") private String targetCron;


    @Bean
    public SchedulerFactoryBean quartzScheduler(@Autowired JobFactory jobFactory) throws SchedulerException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/quartz.properties"));

        //실시간발송
        JobDetail realtimeSendJob = JobBuilder.newJob(RealtimeSendExecutor.class).withIdentity("RealtimeSendExecutor")
                                    .storeDurably(true).build();
        CronTrigger realtimeSendTrigger = TriggerBuilder.newTrigger().forJob("RealtimeSendExecutor").withIdentity("RealtimeSendTrigger")
                                            .withSchedule(CronScheduleBuilder.cronSchedule(realtimeCron)).build();

        //타게팅
        JobDetail targetJob = JobBuilder.newJob(TargetExecutor.class).withIdentity("TargetExecutor")
                            .storeDurably(true).build();
        CronTrigger targetTrigger = TriggerBuilder.newTrigger().forJob("TargetExecutor").withIdentity("TargetTrigger")
                                    .withSchedule(CronScheduleBuilder.cronSchedule(targetCron)).build();

        schedulerFactoryBean.setJobDetails(realtimeSendJob, targetJob);
        schedulerFactoryBean.setTriggers(realtimeSendTrigger, targetTrigger);

        return schedulerFactoryBean;
    }
}
