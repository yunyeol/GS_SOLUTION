package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Target;
import gs.mail.engine.job.JobParameterContents;
import gs.mail.engine.job.TargetJob;
import gs.mail.engine.service.TargetService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


@Slf4j
public class TargetExecutor extends JobParameterContents implements Job {

    @Autowired private JobOperator jobOperator;
    @Autowired private JobRepository jobRepository;
    @Autowired private SimpleJobLauncher simpleJobLauncher;

    @Autowired private TargetJob targetJob;
    @Autowired private TargetService targetService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            Map<String, Object> jobDataMap = context.getMergedJobDataMap();

//            JobParameters jobParameters1 = getJobParametersFromJobMap(jobDataMap);
//            JobExecution lastExecution = jobRepository.getLastJobExecution("realtimeSendJobDetail", jobParameters1);
//
//            if(lastExecution.getExitStatus().getExitCode() != "COMPLETED"){
//                log.info("Job ExitCode : {}", lastExecution.getExitStatus().getExitCode());
//                lastExecution.setExitStatus(ExitStatus.COMPLETED);
//                lastExecution.setStatus(BatchStatus.COMPLETED);
//                lastExecution.setEndTime(new Date());
//                jobRepository.update(lastExecution);
//            }

            for (Target target : targetService.selectTargetList()){
                jobDataMap.put("jobName", "Target");
                jobDataMap.put("schdlId", target.getSchdlId());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                JobExecution jobExecution = simpleJobLauncher.run(targetJob.targetJobDetail(), jobParameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
