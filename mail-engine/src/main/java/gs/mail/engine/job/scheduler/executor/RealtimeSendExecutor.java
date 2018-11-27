package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Realtime;
import gs.mail.engine.job.JobParameterContents;
import gs.mail.engine.job.RealtimeSendJob;
import gs.mail.engine.service.RealtimeSendService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
public class RealtimeSendExecutor extends JobParameterContents implements Job {

    @Autowired private JobOperator jobOperator;
    @Autowired private JobRepository jobRepository;
    @Autowired private SimpleJobLauncher simpleJobLauncher;

    @Autowired private RealtimeSendJob realtimeSendJob;
    @Autowired private RealtimeSendService realtimeSendService;

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

            for (Realtime realtime : realtimeSendService.selectRealtimeSchdlList()){
                jobDataMap.put("jobName", "RealtimeSend");
                jobDataMap.put("schdlId", realtime.getSchdlId());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                JobExecution jobExecution = simpleJobLauncher.run(realtimeSendJob.realtimeSendJobDetail(), jobParameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
