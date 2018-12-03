package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.job.JobParameterContents;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class CampaignSendExecutor extends JobParameterContents implements Job {
    @Autowired private SimpleJobLauncher simpleJobLauncher;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Map<String, Object> jobDataMap = context.getMergedJobDataMap();

            log.info("######### campaign");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
