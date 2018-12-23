package gs.mail.engine;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.service.DomainConnectService;
import gs.mail.engine.util.SmtpConnect;
import gs.mail.engine.util.SmtpThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@EnableAsync
@Component
public class SmtpApplication {

    @Autowired private DomainConnectService domainConnectService;

    @Bean
    @Async
    public void run(){
        try{
            for(Domain domain : domainConnectService.selectDomainList()){
                for(int i=0; i<5; i++){
                    //log.info("#### : {}", domain.getDomain());

                    SmtpThread smtpThread = new SmtpThread("119.207.76.55");
                    smtpThread.start();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
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
