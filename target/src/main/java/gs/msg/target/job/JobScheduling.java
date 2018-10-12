package gs.msg.target.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
public class JobScheduling {

    @Autowired public SimpleJobLauncher simpleJobLauncher;

    /**
     * 등록할 스케줄을 @Autowired로 지정한다.
     */
    @Autowired private Test test;
    @Autowired private Dbtest dbtest;

    /**
     * cron 값은 application.properties에 설정하고 해당 키값을 입력한다.
     * JobParameter에 Key값은 스케줄이름 + System.currentTimeMillis로 지정한다.
     * 최종적으로 수행할 JOB을 파라미터로 넘겨준다.
     * @throws Exception
     */
    @Scheduled(cron = "${target.test}")
    public void sendSmsForExpiringBookmark() throws Exception
    {
        JobParameters param = new JobParametersBuilder()
                                    .addString("jobId", String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution execution = simpleJobLauncher.run(test.simpleJob(), param);
        log.info("Job finished with status : {}", execution.getStatus());
    }

    @Scheduled(cron = "${target.test.sql}")
    public void sqlTest() throws Exception
    {
        JobParameters param = new JobParametersBuilder()
                .addString("jobId_sql", String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution execution = simpleJobLauncher.run(dbtest.simpleJob2(), param);
        log.info("Job finished with status : {}", execution.getStatus());
    }
}
