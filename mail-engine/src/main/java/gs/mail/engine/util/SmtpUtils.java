package gs.mail.engine.util;

import gs.mail.engine.dto.Send;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeUtility;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SmtpUtils extends Thread{
    @Value("${mail.smtp.send.log.path}") private String dirPathtest;
    private String dirPath = "/app/source/engine/logs/send/";
    //private String dirPath = "C:/git/";

    private String ip;
    private int port;
    private Socket socket;

    @Override
    public void run(){
        try {
            Socket socket = connect(this.ip, this.port);

            while (true){
                log.info("##### ping");
                ping(socket);
                Thread.sleep(10000);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public SmtpUtils(){}

    public SmtpUtils(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public Socket connect(String ip, int port){

//        BufferedReader br = null;
//        PrintStream ps = null;

        try{
            //socket = new Socket(getMxDomain(send.getReceiver()), port);
            socket = new Socket(ip, port);
//            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
//            ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");

            return socket;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            try{
//                if(br != null) br.close();
//                if(ps != null) ps.close();
//                if(socket != null) socket.close();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
        return null;
    }

    public void ping(Socket socket){
        try{
            PrintStream ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");
            ps.print("PING");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(Socket socket){
        try{
            if(socket != null) socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send(String gubun, Send send){
        PrintWriter sendLog = null;
        String carriageReturn = "\r\n";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
            PrintStream ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");

            String fileDir = dirPath;
            if (gubun.equals("C")) {
                fileDir = fileDir + "campaign";
            } else if (gubun.equals("R")) {
                fileDir = fileDir + "realtime";
            }

            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");

            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            log.info("#### dirPathtest : {}", dirPathtest);
            File file = new File(fileDir + "/sendLog_" + sdf.format(today) + ".log");
            sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);

            ps.print("HELO "+send.getReceiver().substring(send.getReceiver().indexOf("@")+1)+carriageReturn );
            //sendLog.print("HELO_RES:"+br.readLine()+" || ");

            ps.print("MAIL FROM: <"+send.getSender()+">"+carriageReturn);
            //sendLog.print("MAIL_FROM_RES:"+br.readLine()+" || ");

            ps.print("RCPT TO:<"+send.getReceiver()+">"+carriageReturn);
            //sendLog.print("RCPT_TO_RES:"+br.readLine()+" || ");

            ps.print("DATA"+carriageReturn);
            //sendLog.print("DATA_RES :"+br.readLine()+" || ");
            ps.print("Mime-Version: 1.0"+carriageReturn);
            ps.print("Content-Type:text/html;charset=euc-kr"+carriageReturn);
            ps.print("Content-Transfer-Encoding:8bit"+carriageReturn);
            ps.print("Subject:"+send.getTitle()+carriageReturn);
            ps.print("From:"+send.getSender()+carriageReturn);
            ps.print("To:"+send.getReceiver()+carriageReturn);
            ps.print("Date: "+new Date()+carriageReturn);
            ps.print(carriageReturn);
            ps.print(send.getContents()+carriageReturn);
            ps.print("."+carriageReturn);
            //sendLog.print("DATA_SEND_RES:"+br.readLine()+" || ");

            sendLog.print("UUID:"+send.getUuid()+" || ");
            while (br.readLine() != null){
                sendLog.print("RES:"+br.readLine()+" || ");
            }
            sendLog.print(carriageReturn);

           // sendLog.print("DATA SEND :: RES :"+br.readLine()+" || ");
            //sendLog.print("QUIT");
            //sendLog.println();
            ps.print("QUIT"+carriageReturn);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(sendLog != null) sendLog.close();
        }
    }
}
