package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Realtime;
import gs.mail.engine.job.RealtimeSendJob;
import gs.mail.engine.service.RealtimeSendService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


@Slf4j
public class RealtimeSendExecutor extends JobParameterContents implements Job {

    @Autowired private RealtimeSendJob realtimeSendJob;
    @Autowired private RealtimeSendService realtimeSendService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            Map<String, Object> jobDataMap = context.getMergedJobDataMap();
            String jobName = (String) jobDataMap.get(JOB_NAME);

            for (Realtime realtime : realtimeSendService.selectRealtimeSchdlList()){
                jobDataMap.put("jobName", "RealtimeMailSend");
                jobDataMap.put("schdlId", realtime.getSchdlId());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                boolean isStarted = jobRepository.isJobInstanceExists(jobName, jobParameters);

                if(!isStarted && !realtimeSendService.isRunningChk(realtime.getSchdlId()) && realtime.getQueId() > 0){
                    simpleJobLauncher.run(realtimeSendJob.realtimeSendJobDetail(), jobParameters);
                }else{
                    log.info("{} job is already running ", jobName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
