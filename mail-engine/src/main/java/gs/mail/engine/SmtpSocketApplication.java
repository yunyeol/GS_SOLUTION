package gs.mail.engine;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.service.DomainConnectService;
import gs.mail.engine.util.SmtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.net.Socket;

@Slf4j
@EnableAsync
@Component
public class SmtpSocketApplication {

    @Autowired private DomainConnectService domainConnectService;
    @Value("${main.send.thread.cnt}") private int threadCnt;

    @Bean
    @Async
    public void executeThread(){
        try{
            //Socket socket = new Socket();

            while(true){
                Socket socket = null;
                for(Domain domain : domainConnectService.selectDomainList()){
                    for(int i=0; i<threadCnt; i++){
//                        if(socket !=null && socket.isConnected() && !socket.isClosed()){
//                            log.info("current connect : {}", domain.getDomain());
//                        }else {
                        Thread t = new SmtpUtils(domain.getDomain(), socket);
                        t.start();
                       // }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
