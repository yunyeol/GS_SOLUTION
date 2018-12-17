package gs.mail.engine.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class NettySmtpHandler extends SimpleChannelInboundHandler  {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        int value = count.incrementAndGet();
//        ctx.channel().attr(id).set(value);
//        ctx.writeAndFlush("your id : "+ String.valueOf(value) + "\n");
//        channels.writeAndFlush(String.valueOf(value) + " join \n");
//        channels.add(ctx.channel());
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ByteBuf byteBuf = (ByteBuf) msg;
        //log.info("message : {} ",byteBuf.toString(Charset.defaultCharset()));
        log.info("message : {} ",msg.toString());
    }
}
