package gs.mail.engine.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettySmtpHandler extends SimpleChannelInboundHandler  {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("#### channelActive()");
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("message : {} ",msg.toString());
    }
}
