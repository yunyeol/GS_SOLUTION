package gs.mail.engine.job;

import gs.mail.engine.util.SmtpSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

@Slf4j
@Configuration
public class SocketJob extends SmtpSocket {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private SimpleIncrementer simpleIncrementer;

    @Autowired private TaskExecutor taskExecutor;

    @Bean
    public Job socketJobDetail() {
        try{
            return jobBuilderFactory.get("socketJobDetail")
                    //.incrementer(simpleIncrementer)
                    .start(socketTasklet())
                    .build();

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    @Bean
//    public Step socketStep() {
//        try{
//            return stepBuilderFactory.get("socketStep")
//                    .tasklet(socketTasklet())
//                    .taskExecutor(taskExecutor)
//                    .throttleLimit(1)
//                    .build();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Bean
//    public Tasklet socketTasklet(){
//        try {
//            Tasklet tasklet = new Tasklet() {
//                @Override
//                public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                    String domain = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("domain");
//                    log.info("### : {} ", domain);
//
//                    if(domain != null && (socket == null || socket.isClosed())){
//                        //socket = new Socket(getMxDomain(domain), port);
//                        socket = new Socket("119.207.76.55", port);
//                    }
//
//                    if(socket != null && !socket.isClosed()) {
//                        log.info("############## 1");
//                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
//
//                        String line = "";
//                        if(br != null){
//                            while ((line = br.readLine()) != null){
//                                log.info("############## 2 {}", line);
//                                if(line == null //|| (!line.startsWith("250") && !line.startsWith("220"))
//                                    || line.contains("disconnected")){
//                                    log.info("############## 3");
//                                    log.info("####### line : {} ", line);
//                                    socket.close();
//                                }
//                            }
//                        }else{
//                            log.info("############## 4");
//                            socket.close();
//                        }
//                    }
//                    return RepeatStatus.FINISHED;
//                }
//            };
//            return tasklet;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Bean
    public Step socketTasklet()  {
        try {
            return stepBuilderFactory.get("socketTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        String domain = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("domain");

//                        if(domain != null && (socket == null || socket.isClosed())){
//                            //socket = new Socket(getMxDomain(domain), port);
//                            socket = new Socket("119.207.76.55", port);
//                        }
//
//
//                        if(socket != null && !socket.isClosed()) {
//                            log.info("############ isClosed : {}", socket.isClosed());
//                            String carriageReturn = "\r\n";
//                            PrintStream ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");
//                            ps.print("HELO "+domain+carriageReturn );
//                            ps.flush();

//                            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
//                            if(br != null && br.readLine() == null){
//                                log.info("########## : {}" ,br.readLine());
//                                socket.close();
//                            }
//                            while (br.readLine() != null){
//                                log.info("########## : {}", br.readLine());
//                            }
                       // }
                        return RepeatStatus.CONTINUABLE;
                    })
                    .build();
        }catch (Exception e){
            try {
                if(socket != null) socket.close();
            }catch (Exception e1){
                e1.printStackTrace();
            }

            e.printStackTrace();
        }finally {
            try {
                if(socket != null) socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
