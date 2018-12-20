package gs.mail.engine;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.service.DomainConnectService;
import gs.mail.engine.util.SmtpUtils;
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
    private int port = 25;
    @Autowired private SmtpUtils smtpUtils;

    @Bean
    @Async
    public void run(){
        try{
            for(Domain domain : domainConnectService.selectDomainList()){
                //SmtpUtils smtpUtils = new SmtpUtils(getMxDomain(domain.getDomain()), port);
                smtpUtils.setParams("61.34.243.120", port);
                smtpUtils.run();
                log.info("@@@@@@@@@ socket start");
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