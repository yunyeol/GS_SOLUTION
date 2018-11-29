package gs.mail.engine.job.scheduler;


import gs.mail.engine.dto.Target;
import gs.mail.engine.job.scheduler.executor.RealtimeSendExecutor;
import gs.mail.engine.job.scheduler.executor.TargetExecutor;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzJobScheduler {
    @Value("${cron.mail.realtime}") private String realtimeCron;
    @Value("${cron.mail.target}") private String targetCron;

    @Bean
    public SchedulerFactoryBean RealtimeSendStart(@Autowired JobFactory jobFactory) throws SchedulerException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(jobFactory);

        JobDetail job = JobBuilder.newJob(RealtimeSendExecutor.class).withIdentity("RealtimeSendExecutor").storeDurably(true).build();

        CronTrigger trigger = TriggerBuilder.newTrigger().forJob("RealtimeSendExecutor").
                withSchedule(CronScheduleBuilder.cronSchedule(realtimeCron)).build();

        schedulerFactoryBean.setJobDetails(job);
        schedulerFactoryBean.setTriggers(trigger);

        return schedulerFactoryBean;
    }

    @Bean
    public SchedulerFactoryBean TargetStart(@Autowired JobFactory jobFactory) throws SchedulerException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(jobFactory);

        JobDetail job = JobBuilder.newJob(TargetExecutor.class).withIdentity("TargetExecutor").storeDurably(true).build();

        CronTrigger trigger = TriggerBuilder.newTrigger().forJob("TargetExecutor").
                withSchedule(CronScheduleBuilder.cronSchedule(targetCron)).build();

        schedulerFactoryBean.setJobDetails(job);
        schedulerFactoryBean.setTriggers(trigger);

        return schedulerFactoryBean;
    }

}
