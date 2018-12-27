package gs.mail.engine.job;


import gs.mail.engine.util.SmtpSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@Slf4j
@Configuration
public class SocketJob extends SmtpSocket {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private SimpleIncrementer simpleIncrementer;

    private int port = 25;

    @Bean
    public Job socketJobDetail() {
        try{
            return jobBuilderFactory.get("socketJobDetail")
                    .incrementer(simpleIncrementer)
                    .start(socketTasklet())
                    .build();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public Step socketTasklet()  {
        try {
            return stepBuilderFactory.get("socketTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        String domain = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("domain");

                        log.info("#### domain : {}", domain);
                        if(socket == null || socket.isClosed()){
                            //socket = new Socket(getMxDomain(domain), port);
                            socket = new Socket("119.207.76.55", port);
                            log.info("##### socket start");
                        }

                        if(socket != null && !socket.isClosed()){
                            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
                            if(br.readLine() == null){
                                log.info("#### : {}",br.readLine());
                                socket.close();
                            }
                        }

                        return RepeatStatus.FINISHED;
                    })
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(socket != null) socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
