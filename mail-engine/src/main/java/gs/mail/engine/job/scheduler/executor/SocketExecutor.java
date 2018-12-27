package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.job.SocketJob;
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
public class SocketExecutor extends JobParameterContents implements Job {

    @Autowired private DomainService domainService;
    @Autowired private SimpleJobLauncher simpleJobLauncher;
    @Autowired private SocketJob socketJob;

    @Override
    public void execute(JobExecutionContext context) {
        try{

            Map<String, Object> jobDataMap = context.getMergedJobDataMap();

            for (Domain domain : domainService.selectDomainList()){
                jobDataMap.put("jobName", "Socket");
                jobDataMap.put("domain", domain.getDomain());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                JobExecution jobExecution = simpleJobLauncher.run(socketJob.socketJobDetail(), jobParameters);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
