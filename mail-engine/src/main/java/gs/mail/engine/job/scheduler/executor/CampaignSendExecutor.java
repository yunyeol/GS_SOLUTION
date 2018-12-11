package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.job.CampaignSendJob;
import gs.mail.engine.service.CampaignSendService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class CampaignSendExecutor extends JobParameterContents implements Job {

    @Autowired private SimpleJobLauncher simpleJobLauncher;

    @Autowired private CampaignSendService campaignSendService;
    @Autowired private CampaignSendJob campaignSendJob;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{

//            log.info("smtp : 1");
//            smtpSender.send();
//            log.info("smpt : 2");

            Map<String, Object> jobDataMap = context.getMergedJobDataMap();

//            for(Campaign campaign : campaignSendService.selectCampaignSchdlList()){
//                jobDataMap.put("jobName", "CampaignMailSend");
//                jobDataMap.put("schdlId", campaign.getSchdlId());
//                jobDataMap.put("filePath", campaign.getFilePath());
//                jobDataMap.put("schdlName", campaign.getSchdlName());
//                jobDataMap.put("sender", campaign.getSender());
//                jobDataMap.put("time", System.currentTimeMillis());
//
//                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);
//
//                JobExecution jobExecution = simpleJobLauncher.run(campaignSendJob.campaignSendJobDetail(), jobParameters);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
