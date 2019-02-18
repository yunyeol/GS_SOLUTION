package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Campaign;
import gs.mail.engine.job.CampaignSendJob;
import gs.mail.engine.service.CampaignSendService;
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

    @Autowired private CampaignSendService campaignSendService;
    @Autowired private CampaignSendJob campaignSendJob;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            Map<String, Object> jobDataMap = context.getMergedJobDataMap();
            String jobName = (String) jobDataMap.get(JOB_NAME);

            for(Campaign campaign : campaignSendService.selectCampaignSchdlList()){
                jobDataMap.put("jobName", "CampaignMailSend");
                jobDataMap.put("schdlId", campaign.getSchdlId());
                jobDataMap.put("filePath", campaign.getFilePath());
                jobDataMap.put("schdlName", campaign.getSchdlName());
                jobDataMap.put("sender", campaign.getSender());
                jobDataMap.put("senderName", campaign.getSenderName());
                jobDataMap.put("subject", campaign.getSubject());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                boolean isStarted = jobRepository.isJobInstanceExists(jobName, jobParameters);

                if(!isStarted && !campaignSendService.isRunningChk(campaign.getSchdlId()) && campaign.getSchdlId() > 0){
                    simpleJobLauncher.run(campaignSendJob.campaignSendJobDetail(), jobParameters);
                }else{
                    log.info("{} job is already running ", jobName);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
