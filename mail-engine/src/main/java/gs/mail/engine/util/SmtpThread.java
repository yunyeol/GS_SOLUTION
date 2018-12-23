package gs.mail.engine.util;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.service.DomainConnectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SmtpThread extends Thread {

    @Autowired private DomainConnectService domainConnectService;

    private int port = 25;
    private String ip;

    public SmtpThread(String ip){
        this.ip = ip;
    }

    @Override
    public void run(){
        try {

            for(Domain domain: domainConnectService.selectDomainList()){
                log.info("#### : {}",domain.getDomain());
            }

            SmtpConnect smtpConnect = new SmtpConnect();
            log.info("###### : ip {}", this.ip);

            while (true){
                smtpConnect.connect(this.ip, this.port);

                smtpConnect.ping(this.ip);
                Thread.sleep(10000);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
