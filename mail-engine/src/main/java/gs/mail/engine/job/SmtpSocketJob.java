package gs.mail.engine.job;

import gs.mail.engine.dto.Realtime;
import gs.mail.engine.util.SmtpSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.io.*;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class SmtpSocketJob extends SmtpSocketContents {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private SimpleIncrementer simpleIncrementer;

    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    @Autowired private SqlSessionFactory sqlSessionFactory;

    @Autowired private TaskExecutor taskExecutor;

    @Value("${mail.smtp.send.log.path}") String dirPath;
    @Value("${batch.commit.interval}") private int commitInterval;
    @Value("${batch.slave.cnt}") private int slaveCnt;

    private int port = 25;

    @Bean
    public Job smtpSocketJobDetail() {
        try{
            return jobBuilderFactory.get("smtpSocketJobDetail")
                    .incrementer(simpleIncrementer)
                    .start(smtpSocketTasklet())
                    .build();

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Bean
    public Step smtpSocketTasklet()  {
        try {
            return stepBuilderFactory.get("smtpSocketTasklet")
                    .tasklet((contribution, chunkContext) -> {
                        log.info("##########3 smtpSocketTasklet");
                        //if(socket != null) socket.close();
                        String  domain = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("domain");

                        //socket = new Socket(getMxDomain(domain), port);
                        //socket = new Socket("119.207.76.55", port);

                        Selector selector = Selector.open();
                        socketChannel.configureBlocking(false);
                        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_CONNECT);

                        return RepeatStatus.FINISHED;
                    })
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getMxDomain(String receiver){
        try{
            String domain = receiver.substring(receiver.indexOf("@")+1);

            Process process = Runtime.getRuntime().exec("nslookup -type=mx " + domain);
            InputStream in = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            List<String> domainList = new ArrayList();
            String line = "";
            while ((line = br.readLine()) != null){
                if(line.contains("internet address")){
                    domainList.add(line.substring(line.indexOf("internet address") + 19));
                }
            }
            br.close();
            in.close();

            return domainList.get(0).toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
