package gs.mail.engine.job;

import gs.mail.engine.util.SmtpSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.channels.AsynchronousSocketChannel;

@Slf4j
@Configuration
public class RealtimeResultJob extends SmtpSocket {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private SimpleIncrementer simpleIncrementer;

    @Value("${mail.smtp.send.log.path}") String dirPath;

    @Bean
    public Job realtimeResultJobDetail() {
        try{
            return jobBuilderFactory.get("realtimeResultJobDetail")
                    .incrementer(simpleIncrementer)
                    .start(realtimeResultTasklet())
                    .build();

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Bean
    public Step realtimeResultTasklet()  {
        try {
            return stepBuilderFactory.get("realtimeSchdlTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        final long  schdlId = chunkContext.getStepContext().getStepExecution().getJobParameters().getLong("schdlId");

                        //socketResult("R", dirPath, schdlId);
                        //AsynchronousSocketChannel asyncSocketChannel = AsynchronousSocketChannel.open();
                        //socketResultNio2(asyncSocketChannel);

                        return RepeatStatus.FINISHED;
                    })
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
