package gs.mail.engine.util;

import gs.mail.engine.dto.Send;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import javax.mail.internet.MimeUtility;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class NettyClientConnect {

    private int port = 25;

    public Channel connect(Send send){
        try{
            EventLoopGroup workerGroup = new NioEventLoopGroup(3, new DefaultThreadFactory("worker", true));

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new StringDecoder(CharsetUtil.UTF_8), new StringEncoder(CharsetUtil.UTF_8))
                            .addLast(new NettySmtpHandler());
                }
            });

            Channel channel = bootstrap.connect(getMxDomain(send.getReceiver()), port).sync().channel();
            return channel;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Channel connect(String domain){
        try{
            EventLoopGroup workerGroup = new NioEventLoopGroup(5, new DefaultThreadFactory("worker", true));

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new StringDecoder(CharsetUtil.UTF_8), new StringEncoder(CharsetUtil.UTF_8))
                            .addLast(new NettySmtpHandler());
                }
            });

            Channel channel = bootstrap.connect(getMxDomain(domain), port).sync().channel();
            return channel;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void send(Send send, Channel channel){
        try{
            ChannelFuture future = null;
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

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getMxDomain(String receiver){
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
