package gs.mail.engine.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class NettySmtpHandler extends SimpleChannelInboundHandler  {

    //PrintWriter sendLog = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        log.info("message : {} ",msg.toString());

//        Date today = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        File file = null;
//
//        try{
//            file = new File("/app/source/engine/logs/sendLog_" + sdf.format(today) + ".log");
//
//            sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);
//            sendLog.print(msg.toString().replaceAll("\r\n", "") + " || ");
//            if(msg.toString().contains("closing")){
//                sendLog.println();
//            }
//            sendLog.flush();
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//
//        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //sendLog.close();
    }
}
