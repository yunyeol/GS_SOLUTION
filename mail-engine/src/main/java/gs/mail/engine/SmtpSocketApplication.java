package gs.mail.engine;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.service.DomainConnectService;
import gs.mail.engine.util.NettySmtpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


@Slf4j
//@EnableAsync
//@Component
public class SmtpSocketApplication {

    @Autowired private DomainConnectService domainConnectService;
    @Value("${main.send.thread.cnt}") private int threadCnt;

    private int port = 25;

//    @Bean
//    @Async
    public void executeThread(){
        log.info("############### executeThread 1 ");
        try{
            for(Domain domain : domainConnectService.selectDomainList()){
                for(int i=0; i<threadCnt; i++){
                    log.info("############### executeThread 2 ");
                    EventLoopGroup workerGroup = new NioEventLoopGroup();
                    Bootstrap b = new Bootstrap();
                    b.group(workerGroup);
                    b.channel(NioSocketChannel.class);
                    b.option(ChannelOption.SO_KEEPALIVE, true);
                    b.handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new StringDecoder(CharsetUtil.UTF_8), new StringEncoder(CharsetUtil.UTF_8))
                                    .addLast(new NettySmtpHandler());
                        }
                    });

                    // Start the client.
                    ChannelFuture f = b.connect(getMxDomain(domain.getDomain()), port).sync();
                    f.channel().closeFuture().sync();
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

            log.info("### nslookup : {} ", domainList.get(0).toString());
            return domainList.get(0).toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
