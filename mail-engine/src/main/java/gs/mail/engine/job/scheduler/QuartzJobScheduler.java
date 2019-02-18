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

import java.util.HashMap;
import java.util.Map;

@Configuration
public class QuartzJobScheduler {
    @Value("${cron.mail.campaign}") private String campaignCron;
    @Value("${cron.mail.realtime.send}") private String realtimeSendCron;
    @Value("${cron.mail.realtime.result}") private String realtimeReultCron;
    @Value("${cron.mail.target}") private String targetCron;

    @Bean
    public SchedulerFactoryBean quartzScheduler(@Autowired JobFactory jobFactory) throws SchedulerException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/quartz.properties"));

        //실시간발송
        JobDataMap realtimeSendMap = new JobDataMap();
        realtimeSendMap.put("jobName","realitmeSend");
        JobDetail realtimeSendJob = JobBuilder.newJob(RealtimeSendExecutor.class).setJobData(realtimeSendMap)
                                    .withIdentity("RealtimeSendExecutor").storeDurably(true).build();
        CronTrigger realtimeSendTrigger = TriggerBuilder.newTrigger().forJob("RealtimeSendExecutor").withIdentity("RealtimeSendTrigger")
                                            .withSchedule(CronScheduleBuilder.cronSchedule(realtimeSendCron)).build();

        //실시간결과
        JobDetail realtimeResultJob = JobBuilder.newJob(RealtimeResultExecutor.class).withIdentity("RealtimeResultExecutor")
                                        .storeDurably(true).build();
        CronTrigger realtimeResultTrigger = TriggerBuilder.newTrigger().forJob("RealtimeResultExecutor").withIdentity("realtimeResultTrigger")
                                            .withSchedule(CronScheduleBuilder.cronSchedule(realtimeReultCron)).build();

        //타게팅
        JobDataMap targetMap = new JobDataMap();
        targetMap.put("jobName","target");
        JobDetail targetJob = JobBuilder.newJob(TargetExecutor.class).setJobData(targetMap)
                                .withIdentity("TargetExecutor").storeDurably(true).build();
        CronTrigger targetTrigger = TriggerBuilder.newTrigger().forJob("TargetExecutor").withIdentity("TargetTrigger")
                                    .withSchedule(CronScheduleBuilder.cronSchedule(targetCron)).build();

        //캠페인발송
        JobDataMap campaignSendMap = new JobDataMap();
        campaignSendMap.put("jobName","campaignSend");
        JobDetail campaignJob = JobBuilder.newJob(CampaignSendExecutor.class).setJobData(campaignSendMap)
                                .withIdentity("CampaignSendExecutor").storeDurably(true).build();
        CronTrigger campaignTrigger = TriggerBuilder.newTrigger().forJob("CampaignSendExecutor").withIdentity("CampaignSendTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(campaignCron)).build();

        schedulerFactoryBean.setJobDetails(realtimeSendJob, targetJob, campaignJob, realtimeResultJob);
        schedulerFactoryBean.setTriggers(realtimeSendTrigger, targetTrigger, campaignTrigger, realtimeResultTrigger);

        return schedulerFactoryBean;
    }
}
