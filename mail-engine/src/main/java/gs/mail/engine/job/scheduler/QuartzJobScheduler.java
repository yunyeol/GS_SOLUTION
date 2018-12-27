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
    @Value("${cron.mail.realtime}") private String realtimeCron;
    @Value("${cron.mail.target}") private String targetCron;
    @Value("${cron.mail.socket}") private String socketCron;


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

        //캠페인발송
        JobDetail campaignJob = JobBuilder.newJob(CampaignSendExecutor.class).withIdentity("CampaignSendExecutor")
                .storeDurably(true).build();
        CronTrigger campaignTrigger = TriggerBuilder.newTrigger().forJob("CampaignSendExecutor").withIdentity("CampaignSendTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(campaignCron)).build();

        //소켓
        JobDetail socketJob = JobBuilder.newJob(SocketExecutor.class).withIdentity("SocketExecutor")
                .storeDurably(true).build();
        CronTrigger socketTrgger = TriggerBuilder.newTrigger().forJob("SocketExecutor").withIdentity("socketTrgger")
                .withSchedule(CronScheduleBuilder.cronSchedule(socketCron)).build();

        schedulerFactoryBean.setJobDetails(realtimeSendJob, targetJob, campaignJob, socketJob);
        schedulerFactoryBean.setTriggers(realtimeSendTrigger, targetTrigger, campaignTrigger, socketTrgger);

        return schedulerFactoryBean;
    }
}
