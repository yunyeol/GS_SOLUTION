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
public class SmtpConnect extends SmtpSend{

    public void connect(String ip, int port){
        try{
            socket = new Socket(ip, port);
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

    public void ping(String ip){
        PrintStream ps = null;
        try{
            ps =  new PrintStream(socket.getOutputStream(), true, "euc-kr");
            ps.print("HELO " + ip);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //ps.close();
        }
    }
}
