package gs.mail.engine.job.scheduler.executor;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.service.DomainConnectService;
import gs.mail.engine.util.SmtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class DomainConnectExecutor implements Job {

    @Value("${main.send.thread.cnt}") private int threadCnt;

    @Autowired private DomainConnectService domainConnectService;
    @Autowired private SmtpUtils smtpUtils;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            for(Domain domain : domainConnectService.selectDomainList()){
                for(int i=0; i<threadCnt; i++){
                    log.info("domain connect job : {}", i);
                    smtpUtils.start();
                }
                log.info("############### 123123 domain");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
