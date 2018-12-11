package gs.mail.engine.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class SmtpSender extends Thread{

    //private String host = "119.207.76.55";
    private int port = 25;
    //@Value("${mail.smtp.send.log.path}")
    //private String dirPath = "C:/git/";
    private String dirPath = "/app/source/engine/logs/send/";

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw, sendLog;

    private String gubun;
    private String sender;
    private String receiver;
    private String title;
    private String contents;

    public SmtpSender(){
    }

    public SmtpSender(String gubun, String receiver){
        connect(gubun, receiver);
    }

    public void run(){
        log.info("###### test");
    }

    public void connect(String gubun, String receiver){
        try{
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

            log.info("#### fileDir : {}", fileDir);
            File file = new File(fileDir + "/sendLog_" + sdf.format(today) + ".log");

            socket = new Socket(getMxDomain(receiver), port);
            log.info("socker open : host : {}, port ; {}", getMxDomain(receiver), port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "euc-kr"), true);

            sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);
            sendLog.print("CONNECT SERVER :: RES :" + br.readLine() + " || ");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        try{
            if(br != null) br.close();
            if(pw != null) pw.close();
            if(sendLog != null) sendLog.close();
            if(socket != null) socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getMxDomain(String receiver){
        try{
            String domain = receiver.substring(receiver.indexOf("@")+1);
            log.info("@##### receiver : {}", domain);

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

    public void send(String sender, String receiver, String title, String contents){
        try {
            //파라미터로 받는사람 주소를 받으면 해당 도메인을 오픈
            pw.println("HELO "+receiver.substring(receiver.indexOf("@")+1));
            log.info("############### HELO : {}",receiver.substring(receiver.indexOf("@")+1));
            sendLog.print("HELO SEND :: RES :"+br.readLine()+" || ");

            pw.println("MAIL FROM: <"+sender+">");
            sendLog.print("MAIL FROM :: RES :"+br.readLine()+" || ");

            pw.println("RCPT TO:<"+receiver+">");
            sendLog.print("RCPT TO :: RES :"+br.readLine()+" || ");

            pw.println("DATA");
            sendLog.print("DATA :: RES :"+br.readLine()+" || ");

            //pw.println("Subject:" +  MimeUtility.encodeText("스팸입니다 고갱님  ㅎ ", "EUC-KR", "B"));
            pw.println("subject:"+title);
            pw.println("from:"+sender);
            pw.println("to:"+receiver);
            pw.println("date: "+new Date());
            pw.println();
            //pw.println( MimeUtility.encodeText("메롱 스팸이다!!!!! ㅎ", "EUC-KR", "B"));
            pw.println(contents);
            pw.println(".");
            sendLog.print("DATA SEND :: RES :"+br.readLine()+" || ");

            sendLog.print("QUIT");
            sendLog.println();
            pw.println("quit");

            pw.flush();
            sendLog.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
