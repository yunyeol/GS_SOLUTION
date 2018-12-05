package gs.mail.engine.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeUtility;
import java.io.*;
import java.net.Socket;
import java.util.Date;

@Component
@Slf4j
public class SmtpSender {

    private String host = "119.207.76.55";
    private int port = 25;

    public void send(){
        try {
            Socket socket = new Socket(host, port);

            BufferedReader br = new BufferedReader(new InputStreamReader( socket.getInputStream(), "euc-kr" ) );
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream() , "euc-kr"), true );
            log.info("서버에 연결되었습니다.");

            String line=br.readLine();
            System.out.println("응답:"+line);
            if (!line.startsWith("220")) {
                log.info("SMTP서버가 아닙니다");
            }

            System.out.println("HELO 명령을 전송합니다.");
            pw.println("HELO humuson.com");line=br.readLine();
            System.out.println("응답:"+line);
            if (!line.startsWith("250")) log.info("HELO 실패했습니다:"+line);

            System.out.println("MAIL FROM 명령을 전송합니다.");
            pw.println("MAIL FROM: <test@naver.com>");
            line=br.readLine();
            System.out.println("응답:"+line);
            if (!line.startsWith("250")) log.info("MAIL FROM 에서 실패했습니다:"+line);

            System.out.println("RCPT 명령을 전송합니다.");
            //pw.println("RCPT TO:<eklee@humuson.com>");
            pw.println("RCPT TO:<dyhwang@humuson.com>");
            line=br.readLine();
            System.out.println("응답:"+line);
            if (!line.startsWith("250")) log.info("RCPT TO 에서 실패했습니다:"+line);

            System.out.println("DATA 명령을 전송합니다.");
            pw.println("DATA");
            line=br.readLine();
            System.out.println("응답:"+line);
            if (!line.startsWith("354")) log.info("DATA 에서 실패했습니다:"+line);

            System.out.println("본문을 전송합니다.");
            pw.println("Subject:" +  MimeUtility.encodeText("스팸입니다 고갱님  ㅎ ", "EUC-KR", "B"));
            pw.println("from: test@naver.com");
            pw.println("to: dyhwang@humuson.com");
            pw.println("date: "+new Date());
            pw.println();
            pw.println( MimeUtility.encodeText("메롱 스팸이다!!!!! ㅎ", "EUC-KR", "B"));
            pw.println(".");
            line=br.readLine();
            System.out.println("응답:"+line);
            if (!line.startsWith("250")) log.info("내용전송에서 실패했습니다:"+line);

            System.out.println("접속 종료합니다.");
            pw.println("quit");

            br.close();
            pw.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
