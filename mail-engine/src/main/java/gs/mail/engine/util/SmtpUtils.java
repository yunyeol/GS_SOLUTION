package gs.mail.engine.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SmtpUtils{
    //@Value("${mail.smtp.send.log.path}")
    private String dirPath = "C:/git/";
    //private String dirPath = "/app/source/engine/logs/send/";

    private int port = 25;
    private BufferedReader br;
    private PrintWriter pw;

    public SmtpUtils() { }

//    public SmtpUtils(String receiver) {
//        try{
//
//            Socket socket = new Socket(getMxDomain(receiver), port);
//            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
//            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "euc-kr"), true);
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

//    public void connect(String receiver){
//        try{
//            if(socket == null){
//                socket = new Socket(getMxDomain(receiver), port);
//                socket.setKeepAlive(true);
//                br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
//                pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "euc-kr"), true);
//
//                log.info("###### socket.isConnected() : {}",socket.isConnected() );
//                log.info("###### socket.setKeepAlive() : {}",socket.getKeepAlive() );
//                log.info("###### socket.isClosed() : {}",socket.isClosed() );
//            }else{
//                log.info("###### isConnected() : {}",socket.isConnected() );
//                log.info("###### setKeepAlive() : {}",socket.getKeepAlive() );
//                log.info("###### isClosed() : {}",socket.isClosed() );
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public void close(Socket socket){
        try{
            if(br != null) br.close();
            if(pw != null) pw.close();
            if(socket != null) socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public String getMxDomain(String receiver){
//        try{
//            String domain = receiver.substring(receiver.indexOf("@")+1);
//
//            Process process = Runtime.getRuntime().exec("nslookup -type=mx " + domain);
//            InputStream in = process.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//
//            List<String> domainList = new ArrayList();
//            String line = "";
//            while ((line = br.readLine()) != null){
//                if(line.contains("internet address")){
//                    domainList.add(line.substring(line.indexOf("internet address") + 19));
//                }
//            }
//            br.close();
//            in.close();
//
//            log.info("### nslookup : {} ", domainList.get(0).toString());
//            return domainList.get(0).toString();
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }

//    public void send(String gubun, String sender, String receiver, String title, String contents){
//        PrintWriter sendLog = null;
//        try {
//            String fileDir = dirPath;
//            if (gubun.equals("C")) {
//                fileDir = fileDir + "campaign";
//            } else if (gubun.equals("R")) {
//                fileDir = fileDir + "realtime";
//            }
//
//            Date today = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//
//            File dir = new File(fileDir);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            log.info("#### fileDir : {}", fileDir);
//            File file = new File(fileDir + "/sendLog_" + sdf.format(today) + ".log");
//
//            sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);
//
//            pw.println("HELO "+receiver.substring(receiver.indexOf("@")+1));
//            sendLog.print("HELO SEND :: RES :"+br.readLine()+" || ");
//
//            pw.println("MAIL FROM: <"+sender+">");
//            sendLog.print("MAIL FROM :: RES :"+br.readLine()+" || ");
//
//            pw.println("RCPT TO:<"+receiver+">");
//            sendLog.print("RCPT TO :: RES :"+br.readLine()+" || ");
//
//            pw.println("DATA");
//            sendLog.print("DATA :: RES :"+br.readLine()+" || ");
//
//            //pw.println("Subject:" +  MimeUtility.encodeText("스팸입니다 고갱님  ㅎ ", "EUC-KR", "B"));
//            pw.println("subject:"+title);
//            pw.println("from:"+sender);
//            pw.println("to:"+receiver);
//            pw.println("date: "+new Date());
//            pw.println();
//            //pw.println( MimeUtility.encodeText("메롱 스팸이다!!!!! ㅎ", "EUC-KR", "B"));
//            pw.println(contents);
//            pw.println(".");
//            sendLog.print("DATA SEND :: RES :"+br.readLine()+" || ");
//
//            sendLog.print("QUIT");
//            sendLog.println();
//            pw.println("quit");
//
//            pw.flush();
//            sendLog.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if(sendLog != null) sendLog.close();
//        }
//    }
}
