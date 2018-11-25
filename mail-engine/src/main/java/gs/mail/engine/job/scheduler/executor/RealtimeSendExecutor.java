package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Realtime;
import gs.mail.engine.job.JobParameterContents;
import gs.mail.engine.job.RealtimeSendJob;
import gs.mail.engine.service.RealtimeSendService;
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
public class RealtimeSendExecutor extends JobParameterContents implements Job {

    @Autowired private SimpleJobLauncher simpleJobLauncher;
    @Autowired private RealtimeSendJob realtimeSendJob;

    @Autowired private RealtimeSendService realtimeSendService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Map<String, Object> jobDataMap = context.getMergedJobDataMap();

            for (Realtime realtime : realtimeSendService.selectRealtimeSchdlList()){
                jobDataMap.put("jobName", "RealtimeSend");
                jobDataMap.put("schdlId", realtime.getSchdlId());
//                jobDataMap.put("schdlName", realtime.getSchdlName());
//                jobDataMap.put("sender", realtime.getSender());
//                jobDataMap.put("filePath", realtime.getFilePath());
//                jobDataMap.put("sendGubun", realtime.getSendGubun());
//                jobDataMap.put("sendType", realtime.getSendType());
//                jobDataMap.put("activeYn", realtime.getActiveYn());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                JobExecution jobExecution = simpleJobLauncher.run(realtimeSendJob.realtimeSendJobDetail(), jobParameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}