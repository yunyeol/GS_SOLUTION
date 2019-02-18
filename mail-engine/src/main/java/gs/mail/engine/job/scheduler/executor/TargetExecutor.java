package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Target;
import gs.mail.engine.job.TargetJob;
import gs.mail.engine.service.TargetService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
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

    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            Map<String, Object> jobDataMap = context.getMergedJobDataMap();
            String jobName = (String) jobDataMap.get(JOB_NAME);

            for (Target target : targetService.selectTargetList()){
                jobDataMap.put("jobName", "MailTarget");
                jobDataMap.put("schdlId", target.getSchdlId());
                jobDataMap.put("addressGrpId", target.getAddressGrpId());
                jobDataMap.put("sendType", target.getSendType());
                jobDataMap.put("sendGubun", target.getSendGubun());
                jobDataMap.put("targetFile", target.getTargetFile());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                boolean isStarted = jobRepository.isJobInstanceExists(jobName, jobParameters);

                if(!isStarted && !targetService.isRunningChk(target.getSchdlId()) && target.getSchdlId() > 0){
                    simpleJobLauncher.run(targetJob.targetJobDetail(), jobParameters);
                }else{
                    log.info("{} job is already running ", jobName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
