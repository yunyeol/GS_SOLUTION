package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Target;
import gs.mail.engine.job.JobParameterContents;
import gs.mail.engine.job.TargetJob;
import gs.mail.engine.service.TargetService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
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

            for (Target target : targetService.selectTargetList()){
                jobDataMap.put("jobName", "MailTarget");
                jobDataMap.put("schdlId", target.getSchdlId());
                jobDataMap.put("addressGrpId", target.getAddressGrpId());
                jobDataMap.put("sendType", target.getSendType());
                jobDataMap.put("time", System.currentTimeMillis());

                JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

                JobExecution jobExecution = simpleJobLauncher.run(targetJob.targetJobDetail(), jobParameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
