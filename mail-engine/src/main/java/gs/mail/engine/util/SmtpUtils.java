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
public class SmtpUtils{
    @Value("${mail.smtp.send.log.path}") private String dirPathtest;
    private String dirPath = "/app/source/engine/logs/send/";

    private int port = 25;

    public SmtpUtils(String gubun, Send send) {
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw = null;

        try{
            socket = new Socket(getMxDomain(send.getReceiver()), port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "euc-kr"), true);

            send(br, pw, gubun, send);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            try{
//                if(br != null) br.close();
//                if(pw != null) pw.close();
//                if(socket != null) socket.close();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
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

            return domainList.get(0).toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void send(BufferedReader br, PrintWriter pw, String gubun, Send send){
        PrintWriter sendLog = null;

        try {
            String fileDir = dirPath;
            if (gubun.equals("C")) {
                fileDir = fileDir + "campaign";
            } else if (gubun.equals("R")) {
                fileDir = fileDir + "realtime";
            }

            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            log.info("#### dirPathtest : {}", dirPathtest);
            File file = new File(fileDir + "/sendLog_" + sdf.format(today) + ".log");

            sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);

            pw.println("HELO "+send.getReceiver().substring(send.getReceiver().indexOf("@")+1) );
            sendLog.print("HELO SEND :: RES :"+br.readLine()+" || ");

            pw.println("MAIL FROM: <"+send.getSender()+">" );
            sendLog.print("MAIL FROM :: RES :"+br.readLine()+" || ");

            pw.println("RCPT TO:<"+send.getReceiver()+">" );
            sendLog.print("RCPT TO :: RES :"+br.readLine()+" || ");

            pw.println("DATA" );
            sendLog.print("DATA :: RES :"+br.readLine()+" || ");
            pw.println("Content-Type:text/html;");
            pw.println("Subject:"+send.getTitle());
            pw.println("From:"+send.getSender());
            pw.println("To:"+send.getReceiver());
            pw.println("Date: "+new Date());
            pw.println();
            pw.println(send.getContents());
            pw.println(".");
            sendLog.print("DATA SEND :: RES :"+br.readLine()+" || ");

            sendLog.print("QUIT");
            sendLog.println();
//            pw.println("QUIT");
//            pw.flush();
            sendLog.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(sendLog != null) sendLog.close();
        }
    }
}
