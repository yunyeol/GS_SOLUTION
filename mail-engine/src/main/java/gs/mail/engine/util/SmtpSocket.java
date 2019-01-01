package gs.mail.engine.util;


import gs.mail.engine.dto.Send;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class SmtpSocket {
    protected static int port = 25;

    protected void socketSend(Socket socket, String gubun, String dirPath, Send send){
        try{
            String carriageReturn = "\r\n";

            synchronized( this ) {
                PrintStream ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));

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

                File file = new File(fileDir + "/sendLog_" + sdf.format(today) + ".log");
                PrintWriter sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);

                sendLog.print("UUID:"+send.getUuid()+" || ");
                ps.print("HELO "+send.getReceiver().substring(send.getReceiver().indexOf("@")+1)+carriageReturn );
                sendLog.print("HELO_RES:"+br.readLine()+" || ");

                ps.print("MAIL FROM: <"+send.getSender()+">"+carriageReturn);
                sendLog.print("MAIL_FROM_RES:"+br.readLine()+" || ");

                ps.print("RCPT TO:<"+send.getReceiver()+">"+carriageReturn);
                sendLog.print("RCPT_TO_RES:"+br.readLine()+" || ");

                ps.print("DATA"+carriageReturn);
                sendLog.print("DATA_RES :"+br.readLine()+" || ");
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
                //ps.print("QUIT"+carriageReturn);
                ps.flush();

                sendLog.print("DATA_SEND_RES:"+br.readLine()+" || ");

                sendLog.print("SEND :: RES :"+br.readLine()+" || ");
                sendLog.print("QUIT");
                sendLog.println();
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

            return domainList.get(0).toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}