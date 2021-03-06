package gs.mail.engine.job.scheduler.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JobParameterContents {
    @Autowired protected JobOperator jobOperator;
    @Autowired protected JobRepository jobRepository;
    @Autowired protected SimpleJobLauncher simpleJobLauncher;

    protected static final String JOB_NAME = "jobName";

    protected JobParameters getJobParametersFromJobMap(Map<String, Object> jobDataMap) {

        JobParametersBuilder builder = new JobParametersBuilder();

        for (Map.Entry<String, Object> entry : jobDataMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String && !key.equals(JOB_NAME)) {
                builder.addString(key, (String) value);
            }
            else if (value instanceof Float || value instanceof Double) {
                builder.addDouble(key, ((Number) value).doubleValue());
            }
            else if (value instanceof Integer || value instanceof Long) {
                builder.addLong(key, ((Number)value).longValue());
            }
            else if (value instanceof Date) {
                builder.addDate(key, (Date) value);
            }
            else {
                log.debug("JobDataMap contains values which are not job parameters (ignoring).");
            }
        }

        return builder.toJobParameters();
    }
}
