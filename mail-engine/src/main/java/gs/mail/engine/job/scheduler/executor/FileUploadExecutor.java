package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Target;
import gs.mail.engine.job.FileUploadJob;
import gs.mail.engine.service.FileUploadService;
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
public class FileUploadExecutor extends JobParameterContents implements Job {

    @Autowired private SimpleJobLauncher simpleJobLauncher;
    @Autowired private FileUploadService fileUploadService;
    @Autowired private FileUploadJob fileUploadJob;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            Map<String, Object> jobDataMap = context.getMergedJobDataMap();

            for(Target target: fileUploadService.selectFileUploadSchdlList()){
                jobDataMap.put("jobName", "FileUpload");
                jobDataMap.put("schdlId", target.getSchdlId());
                jobDataMap.put("addressGrpId", target.getAddressGrpId());
                jobDataMap.put("sendType", target.getSendType());
                jobDataMap.put("sendGubun", target.getSendGubun());
                jobDataMap.put("fileUploadYn", target.getFileUploadYn());
                jobDataMap.put("targetFilePath", target.getTargetFilePath());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                JobExecution jobExecution = simpleJobLauncher.run(fileUploadJob.fileUploadJobDetail(), jobParameters);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
