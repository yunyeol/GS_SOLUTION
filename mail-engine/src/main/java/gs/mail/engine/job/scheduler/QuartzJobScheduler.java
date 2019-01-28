package gs.mail.engine.job.scheduler;



import gs.mail.engine.job.scheduler.executor.*;
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
    @Value("${cron.mail.campaign}") private String campaignCron;
    @Value("${cron.mail.realtime.target}") private String realtimeTargetCron;
    @Value("${cron.mail.realtime.send}") private String realtimeSendCron;
    @Value("${cron.mail.realtime.result}") private String realtimeReultCron;
    @Value("${cron.mail.target}") private String targetCron;

    @Bean
    public SchedulerFactoryBean quartzScheduler(@Autowired JobFactory jobFactory) throws SchedulerException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/quartz.properties"));

        //실시간타게팅
        JobDetail realtimeTargetJob = JobBuilder.newJob(RealtimeTargetExecutor.class).withIdentity("RealtimeTargetExecutor")
                                        .storeDurably(true).build();
        CronTrigger realtimeTargetTrigger = TriggerBuilder.newTrigger().forJob("RealtimeTargetExecutor").withIdentity("RealtimeTargetTrigger")
                                            .withSchedule(CronScheduleBuilder.cronSchedule(realtimeTargetCron)).build();

        //실시간발송
//        JobDetail realtimeSendJob = JobBuilder.newJob(RealtimeSendExecutor.class).withIdentity("RealtimeSendExecutor")
//                                    .storeDurably(true).build();
//        CronTrigger realtimeSendTrigger = TriggerBuilder.newTrigger().forJob("RealtimeSendExecutor").withIdentity("RealtimeSendTrigger")
//                                            .withSchedule(CronScheduleBuilder.cronSchedule(realtimeSendCron)).build();

        //실시간결과
        JobDetail realtimeResultJob = JobBuilder.newJob(RealtimeResultExecutor.class).withIdentity("RealtimeResultExecutor")
                                        .storeDurably(true).build();
        CronTrigger realtimeResultTrigger = TriggerBuilder.newTrigger().forJob("RealtimeResultExecutor").withIdentity("realtimeResultTrigger")
                                            .withSchedule(CronScheduleBuilder.cronSchedule(realtimeReultCron)).build();

        //타게팅
        JobDetail targetJob = JobBuilder.newJob(TargetExecutor.class).withIdentity("TargetExecutor")
                            .storeDurably(true).build();
        CronTrigger targetTrigger = TriggerBuilder.newTrigger().forJob("TargetExecutor").withIdentity("TargetTrigger")
                                    .withSchedule(CronScheduleBuilder.cronSchedule(targetCron)).build();

        //캠페인발송
        JobDetail campaignJob = JobBuilder.newJob(CampaignSendExecutor.class).withIdentity("CampaignSendExecutor")
                .storeDurably(true).build();
        CronTrigger campaignTrigger = TriggerBuilder.newTrigger().forJob("CampaignSendExecutor").withIdentity("CampaignSendTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(campaignCron)).build();

        schedulerFactoryBean.setJobDetails(realtimeTargetJob, targetJob, campaignJob, realtimeResultJob);
        schedulerFactoryBean.setTriggers(realtimeTargetTrigger, targetTrigger, campaignTrigger, realtimeResultTrigger);

        return schedulerFactoryBean;
    }
}
