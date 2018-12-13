package gs.mail.engine;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.service.DomainConnectService;
import gs.mail.engine.util.SmtpSocketThread;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


@Slf4j
//@EnableAsync
//@Component
//@PropertySource(value = "classpath:/application.properties")
public class SmtpSocketApplication {

    @Autowired private DomainConnectService domainConnectService;
    @Value("${main.send.thread.cnt}") private int threadCnt;

//    @Bean
//    @Async
    public void executeThread(){
        log.info("############### executeThread 1 ");
        try{
//            for(Domain domain : domainConnectService.selectDomainList()){
//                for(int i=0; i<threadCnt; i++){
//                    log.info("############### executeThread 2 ");
////                    SmtpSocketThread sst = new SmtpSocketThread(domain.getDomain());
////                    SmtpSocketThread sst = new SmtpSocketThread("119.207.76.55");
////                    sst.start();
//                    Socket socket = new Socket("119.207.76.55", 25);
//
//                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
//                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "euc-kr"), true);
//                    pw.print("p");
//                    pw.flush();
//
//                }
//            }
//            EventLoopGroup workerGroup = new NioEventLoopGroup();
//            Bootstrap b = new Bootstrap(); // (1)
//            b.group(workerGroup); // (2)
//            b.channel(NioSocketChannel.class); // (3)
//            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
//            b.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    log.info("########### ");
//                }
//            });
//
//            // Start the client.
//            ChannelFuture f = b.connect("119.207.76.55", 25).sync();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
