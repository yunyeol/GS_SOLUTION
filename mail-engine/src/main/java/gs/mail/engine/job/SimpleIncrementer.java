package gs.mail.engine.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.Map;

@Slf4j
public class SimpleIncrementer implements JobParametersIncrementer {

    private Map<String, JobParameter> beforeJobParameter;

    @Override
    public JobParameters getNext(JobParameters parameters) {
        beforeJobParameter = parameters.getParameters();
        log.info("############ : {} ", parameters.getParameters());
        if (parameters==null || parameters.isEmpty()) {
            return new JobParametersBuilder().addLong("run.id", 1L).toJobParameters();
        }
        long id = parameters.getLong("run.id",1L) + 1;
        return new JobParametersBuilder().addLong("run.id", id).toJobParameters();
    }
}
