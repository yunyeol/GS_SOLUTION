package gs.mail.engine.job;

import gs.mail.engine.dto.Send;
import gs.mail.engine.service.DomainConnectService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.internet.MimeUtility;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public class SendJobProperties {

    @Autowired DomainConnectService domainConnectService;
    private int port = 25;

    protected String getMxDomain(String receiver){
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

    protected void sendMail(ChannelFuture future, Channel channel, Send send){
        try{
            String lineSeparator = System.lineSeparator();
            String domain = send.getReceiver();
            future = channel.writeAndFlush("HELO " + domain.substring(domain.indexOf("@")+1) + lineSeparator);
            future = channel.writeAndFlush("MAIL FROM:"+send.getSender() + lineSeparator);
            future = channel.writeAndFlush("RCPT TO:"+send.getReceiver() + lineSeparator);
            future = channel.writeAndFlush("DATA" + lineSeparator);
            future = channel.writeAndFlush("subject:"+ MimeUtility.encodeText(send.getTitle(),"KSC5601","B") + lineSeparator);
            future = channel.writeAndFlush("from:"+send.getSender() + lineSeparator);
            future = channel.writeAndFlush("to:"+send.getReceiver() + lineSeparator);
            future = channel.writeAndFlush("date:"+ new Date() + lineSeparator);
            future = channel.writeAndFlush(MimeUtility.encodeText(send.getContents(),"KSC5601","B"));
            future = channel.writeAndFlush(lineSeparator);
            future = channel.writeAndFlush("." + lineSeparator);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    protected Channel getConnect(Bootstrap bootstrap){
//        Channel channel = null;
//
//        try{
//            for(Domain domain : domainConnectService.selectDomainList()){
//                channel = bootstrap.connect(getMxDomain(domain.getDomain()), port).sync().channel();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return channel;
//    }
}
