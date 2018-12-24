package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.job.SmtpSocketJob;
import gs.mail.engine.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


@Slf4j
public class SmtpSocketExecutor extends JobParameterContents implements Job {

    @Autowired private SmtpSocketJob smtpSocketJob;
    @Autowired private SimpleJobLauncher simpleJobLauncher;

    @Autowired private DomainService domainService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            Map<String, Object> jobDataMap = context.getMergedJobDataMap();

            log.info("##### SmtpSocketExecutor execute");

            for(Domain domain : domainService.selectDomainList()){
                jobDataMap.put("jobName", "SmtpSocket");
                jobDataMap.put("domain", domain.getDomain());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                JobExecution jobExecution = simpleJobLauncher.run(smtpSocketJob.smtpSocketJobDetail(), jobParameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
