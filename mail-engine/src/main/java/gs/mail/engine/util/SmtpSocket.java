package gs.mail.engine.util;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gs.mail.engine.dto.Send;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

@Slf4j
public class SmtpSocket {
    @Autowired private SqlSessionTemplate sqlSessionTemplate;
    protected static int port = 25;

    protected void socketSend(Socket socket, String gubun, String dirPath, Send send){
        String carriageReturn = "\r\n";
        PrintStream ps = null;
        BufferedReader br = null;
        PrintWriter sendLog = null;

        try{
            synchronized( this ) {
                ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");
                br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));

                String fileDir = dirPath;
                if (gubun.equals("C")) {
                    fileDir = fileDir + "campaign/";
                } else if (gubun.equals("R")) {
                    fileDir = fileDir + "realtime/";
                }

                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                File dir = new File(fileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(fileDir + "sendLog_"+send.getMasterSchdlId()+"_" + sdf.format(today) + ".log");
                sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);

                sendLog.print("{\"SOCKET_CONN\":\""+subStringResult(br.readLine())+"\",");
                sendLog.print("\"SCHDL_ID\":\""+send.getSchdlId()+"\",");
                sendLog.print("\"UUID\":\""+send.getUuid()+"\",");
                ps.print("HELO "+send.getReceiver().substring(send.getReceiver().indexOf("@")+1)+carriageReturn );
                sendLog.print("\"HELO_RES\":\""+subStringResult(br.readLine())+"\",");

                ps.print("MAIL FROM: <"+send.getSender()+">"+carriageReturn);
                sendLog.print("\"MAIL_FROM_RES\":\""+subStringResult(br.readLine())+"\",");

                ps.print("RCPT TO:<"+send.getReceiver()+">"+carriageReturn);
                sendLog.print("\"RCPT_TO_RES\":\""+subStringResult(br.readLine())+"\",");

                ps.print("DATA"+carriageReturn);
                sendLog.print("\"DATA_RES\":\""+subStringResult(br.readLine())+"\",");
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
                ps.print("QUIT"+carriageReturn);
                ps.flush();

                sendLog.print("\"DATA_SEND_RES\":\""+subStringResult(br.readLine())+"\",");
                sendLog.print("\"SEND_RES\":\""+subStringResult(br.readLine())+"\"}");
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
                if(line.contains("mail exchanger")){
                    String searchDomain = line.substring(line.indexOf("mail exchanger") + 20, line.length()-1);
                    String dotRemoveDomain = domain.substring(0, domain.indexOf("."));
                    if(dotRemoveDomain.equals("gmail")){
                        dotRemoveDomain = "google";
                    }

                    if(searchDomain.contains(dotRemoveDomain)){
                        domainList.add(searchDomain);
                    }
                }
            }
            br.close();
            in.close();

            Random random = new Random();
            log.info("send Domain : {}", domainList.get(random.nextInt(domainList.size())));
            return domainList.get(random.nextInt(domainList.size()));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    protected void socketSendByte(SocketChannel socketChannel, String gubun, String dirPath, Send send){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        String carriageReturn = "\r\n";
        //PrintStream ps = null;
        //BufferedReader br = null;
        //PrintWriter sendLog = null;
        Charset charset = Charset.forName("euc-kr");

        try{
            synchronized( this ) {
                //ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");
                //br = new BufferedReader(new InputStreamReader(socketChannel.socket().getInputStream(), "euc-kr"));

//                String fileDir = dirPath;
//                if (gubun.equals("C")) {
//                    fileDir = fileDir + "campaign/";
//                } else if (gubun.equals("R")) {
//                    fileDir = fileDir + "realtime/";
//                }

//                Date today = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
//
//                File dir = new File(fileDir);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }

//                File file = new File(fileDir + "sendLog_"+send.getSchdlId()+"_" + sdf.format(today) + ".log");
//                sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);
//
//                sendLog.print("SOCKET_CONN:"+subStringResult(br.readLine())+"||");
//                sendLog.print("UUID:"+send.getUuid()+"||");

                byteBuffer.clear();
                byteBuffer.put(charset.encode("HELO "+send.getReceiver().substring(send.getReceiver().indexOf("@")+1)+carriageReturn));
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                log.info("#### : {}", socketChannel.read(readBuffer));

                byteBuffer.clear();
                byteBuffer.put(charset.encode("MAIL FROM: <"+send.getSender()+">"+carriageReturn));
                byteBuffer.flip();
                socketChannel.write(byteBuffer);

                byteBuffer.clear();
                byteBuffer.put(charset.encode("RCPT TO:<"+send.getReceiver()+">"+carriageReturn));
                byteBuffer.flip();
                socketChannel.write(byteBuffer);

                byteBuffer.clear();
                byteBuffer.put(charset.encode("DATA "+carriageReturn));
                byteBuffer.flip();
                socketChannel.write(byteBuffer);

                byteBuffer.clear();
                byteBuffer.put(charset.encode("Mime-Version: 1.0"+carriageReturn));
                byteBuffer.put(charset.encode("Content-Type:text/html;charset=euc-kr"+carriageReturn));
                byteBuffer.put(charset.encode("Content-Transfer-Encoding:8bit"+carriageReturn));
                byteBuffer.put(charset.encode("Subject:"+send.getTitle()+carriageReturn));
                byteBuffer.put(charset.encode("From:"+send.getSender()+carriageReturn));
                byteBuffer.put(charset.encode("To:"+send.getReceiver()+carriageReturn));
                byteBuffer.put(charset.encode("Date: "+new Date()+carriageReturn));
                byteBuffer.put(charset.encode(carriageReturn));
                byteBuffer.put(charset.encode(send.getContents()+carriageReturn));
                byteBuffer.put(charset.encode("."+carriageReturn));
                byteBuffer.put(charset.encode("QUIT"+carriageReturn));
                byteBuffer.flip();
                socketChannel.write(byteBuffer);

                //socketChannel.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void socketSendNio2(AsynchronousSocketChannel asyncSocketChannel, String gubun, String dirPath, Send send){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        String carriageReturn = "\r\n";
        //PrintStream ps = null;
        //BufferedReader br = null;
        //PrintWriter sendLog = null;
        Charset charset = Charset.forName("euc-kr");

        try{
            InetSocketAddress addr = new InetSocketAddress("119.207.76.55", port);
            Future<Void> conn = asyncSocketChannel.connect(addr);
            conn.get();

            synchronized( this ) {
                //ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");
                //br = new BufferedReader(new InputStreamReader(socketChannel.socket().getInputStream(), "euc-kr"));

//                String fileDir = dirPath;
//                if (gubun.equals("C")) {
//                    fileDir = fileDir + "campaign/";
//                } else if (gubun.equals("R")) {
//                    fileDir = fileDir + "realtime/";
//                }

//                Date today = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
//
//                File dir = new File(fileDir);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }

//                File file = new File(fileDir + "sendLog_"+send.getSchdlId()+"_" + sdf.format(today) + ".log");
//                sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);
//
//                sendLog.print("SOCKET_CONN:"+subStringResult(br.readLine())+"||");
//                sendLog.print("UUID:"+send.getUuid()+"||");

                log.info("#### 1");
                byteBuffer.clear();
                byteBuffer.put(charset.encode("HELO "+send.getReceiver().substring(send.getReceiver().indexOf("@")+1)+carriageReturn));
                byteBuffer.flip();
                asyncSocketChannel.write(byteBuffer);
//                asyncSocketChannel.read(readBuffer, null, new CompletionHandler<Integer, Object>() {
//                    @Override
//                    public void completed(Integer result, Object attachment) {
//                        readBuffer.flip();
//                        log.info("response from server "+result+" "
//                                +Charset.defaultCharset()
//                                .decode(readBuffer).toString());
//                    }
//
//                    @Override
//                    public void failed(Throwable exc, Object attachment) {
//                        log.info("## failed ##");
//
//                    }
//                });

                byteBuffer.clear();
                byteBuffer.put(charset.encode("MAIL FROM: <"+send.getSender()+">"+carriageReturn));
                byteBuffer.flip();
                asyncSocketChannel.write(byteBuffer);
//                asyncSocketChannel.read(readBuffer, null, new CompletionHandler<Integer, Object>() {
//                    @Override
//                    public void completed(Integer result, Object attachment) {
//                        readBuffer.flip();
//                        log.info("response from server "+result+" "
//                                +Charset.defaultCharset()
//                                .decode(readBuffer).toString());
//                    }
//
//                    @Override
//                    public void failed(Throwable exc, Object attachment) {
//                        log.info("## failed ##");
//
//                    }
//                });

                byteBuffer.clear();
                byteBuffer.put(charset.encode("RCPT TO:<"+send.getReceiver()+">"+carriageReturn));
                byteBuffer.flip();
                asyncSocketChannel.write(byteBuffer);
//                asyncSocketChannel.read(readBuffer, null, new CompletionHandler<Integer, Object>() {
//                    @Override
//                    public void completed(Integer result, Object attachment) {
//                        readBuffer.flip();
//                        log.info("response from server "+result+" "
//                                +Charset.defaultCharset()
//                                .decode(readBuffer).toString());
//                    }
//
//                    @Override
//                    public void failed(Throwable exc, Object attachment) {
//                        log.info("## failed ##");
//
//                    }
//                });

                byteBuffer.clear();
                byteBuffer.put(charset.encode("DATA "+carriageReturn));
                byteBuffer.flip();
                asyncSocketChannel.write(byteBuffer);
//                asyncSocketChannel.read(readBuffer, null, new CompletionHandler<Integer, Object>() {
//                    @Override
//                    public void completed(Integer result, Object attachment) {
//                        readBuffer.flip();
//                        log.info("response from server "+result+" "
//                                +Charset.defaultCharset()
//                                .decode(readBuffer).toString());
//                    }
//
//                    @Override
//                    public void failed(Throwable exc, Object attachment) {
//                        log.info("## failed ##");
//
//                    }
//                });

                byteBuffer.clear();
                byteBuffer.put(charset.encode("Mime-Version: 1.0"+carriageReturn));
                byteBuffer.put(charset.encode("Content-Type:text/html;charset=euc-kr"+carriageReturn));
                byteBuffer.put(charset.encode("Content-Transfer-Encoding:8bit"+carriageReturn));
                byteBuffer.put(charset.encode("Subject:"+send.getTitle()+carriageReturn));
                byteBuffer.put(charset.encode("From:"+send.getSender()+carriageReturn));
                byteBuffer.put(charset.encode("To:"+send.getReceiver()+carriageReturn));
                byteBuffer.put(charset.encode("Date: "+new Date()+carriageReturn));
                byteBuffer.put(charset.encode(carriageReturn));
                byteBuffer.put(charset.encode(send.getContents()+carriageReturn));
                byteBuffer.put(charset.encode("."+carriageReturn));
                byteBuffer.put(charset.encode("QUIT"+carriageReturn));
                byteBuffer.flip();
                asyncSocketChannel.write(byteBuffer);
//                asyncSocketChannel.read(readBuffer, null, new CompletionHandler<Integer, Object>() {
//                    @Override
//                    public void completed(Integer result, Object attachment) {
//                        readBuffer.flip();
//                        log.info("response from server "+result+" "
//                                +Charset.defaultCharset()
//                                .decode(readBuffer).toString());
//                    }
//
//                    @Override
//                    public void failed(Throwable exc, Object attachment) {
//                        log.info("## failed ##");
//
//                    }
//                });

                //socketChannel.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String subStringResult(String rs){
        int length = rs.length();
        if(length > 6){
            return rs.substring(0,9);
        }else{
            return rs.substring(0,3);
        }
    }

    protected void socketResult(String gubun, String dirPath, long schdlId){
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schdlId", schdlId);
        String sendFlag = "";
        String resultCd = "";
        String uuid = "";
        Send send = new Send();
        int successCnt = 0;
        int failCnt = 0;
        List<String> todayFileList = new ArrayList<String>();

        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String fileDir = dirPath;
        if (gubun.equals("C")) {
            fileDir = fileDir + "campaign/";
        } else if (gubun.equals("R")) {
            fileDir = fileDir + "realtime/";
        }

        File file = new File(fileDir);

        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File tFile : fileList) {
                if (tFile.getName().equals("sendLog_"+schdlId+"_"+sdf.format(today)+".log")) {
                    todayFileList.add(tFile.getName());
                    send.setLogFileName(tFile.getName());
                }
            }
        }

        try{
            if(!todayFileList.isEmpty()){
                send = sqlSessionTemplate.selectOne("SQL.Send.selectResultFileInfo", paramMap);

                Collections.sort(todayFileList, new CompareFileNameAsc());
                send.setLogFileName(todayFileList.get(0));

                long lineNum = Long.parseLong(send.getLineNumber());

                if (send.getLogFileName() != null) {
                    List<String> lineList = new ArrayList<String>();
                    Files.lines(Paths.get(fileDir + send.getLogFileName()),Charset.forName("UTF-8"))
                            .skip(lineNum).forEachOrdered(lineList::add);

                    if(lineList.size() != 0){
                        for (int i = 0; i < lineList.size() && i < 1000; i++) {
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(lineList.get(i));
                            uuid = element.getAsJsonObject().get("UUID").getAsString();

                            if (!element.getAsJsonObject().get("SOCKET_CONN").getAsString().contains("220")) {
                                sendFlag = "41";
                                resultCd = element.getAsJsonObject().get("SOCKET_CONN").getAsString();
                                failCnt++;
                                log.debug("SOCKET_CONN : {}", element.getAsJsonObject().get("SOCKET_CONN").getAsString());
                            } else if (!element.getAsJsonObject().get("HELO_RES").getAsString().contains("250")) {
                                sendFlag = "41";
                                resultCd = element.getAsJsonObject().get("HELO_RES").getAsString();
                                failCnt++;
                                log.debug("HELO_RES : {}", element.getAsJsonObject().get("HELO_RES").getAsString());
                            } else if (!element.getAsJsonObject().get("MAIL_FROM_RES").getAsString().contains("250")) {
                                sendFlag = "41";
                                resultCd = element.getAsJsonObject().get("MAIL_FROM_RES").getAsString();
                                failCnt++;
                                log.debug("MAIL_FROM_RES : {}", element.getAsJsonObject().get("MAIL_FROM_RES").getAsString());
                            } else if (!element.getAsJsonObject().get("RCPT_TO_RES").getAsString().contains("250")) {
                                sendFlag = "41";
                                resultCd = element.getAsJsonObject().get("RCPT_TO_RES").getAsString();
                                failCnt++;
                                log.debug("RCPT_TO_RES : {}", element.getAsJsonObject().get("RCPT_TO_RES").getAsString());
                            } else if (!element.getAsJsonObject().get("DATA_RES").getAsString().contains("354")) {
                                sendFlag = "41";
                                resultCd = element.getAsJsonObject().get("DATA_RES").getAsString();
                                failCnt++;
                                log.debug("DATA_RES : {}", element.getAsJsonObject().get("DATA_RES").getAsString());
                            } else if (!element.getAsJsonObject().get("DATA_SEND_RES").getAsString().contains("250")) {
                                sendFlag = "41";
                                resultCd = element.getAsJsonObject().get("DATA_SEND_RES").getAsString();
                                failCnt++;
                                log.debug("DATA_SEND_RES : {}", element.getAsJsonObject().get("DATA_SEND_RES").getAsString());
                            } else if (!element.getAsJsonObject().get("SEND_RES").getAsString().contains("221")) {
                                sendFlag = "41";
                                resultCd = element.getAsJsonObject().get("SEND_RES").getAsString();
                                failCnt++;
                                log.debug("SEND_RES : {}", element.getAsJsonObject().get("SEND_RES").getAsString());
                            } else {
                                sendFlag = "40";
                                resultCd = element.getAsJsonObject().get("SEND_RES").getAsString();
                                successCnt++;
                                log.debug("SEND_RES : {}", element.getAsJsonObject().get("SEND_RES").getAsString());
                            }

                            paramMap.put("sendFlag", sendFlag);
                            paramMap.put("resultCd", resultCd);
                            paramMap.put("uuid", uuid);
                            paramMap.put("successCnt", successCnt);
                            paramMap.put("failCnt", failCnt);
                            sqlSessionTemplate.update("SQL.Send.updateSendQueRawSendFlag", paramMap);

                            lineNum++;
                        }
                        paramMap.put("lineNumber", lineNum);
                        paramMap.put("logFileName", send.getLogFileName());
                        sqlSessionTemplate.update("SQL.Send.updateSendSchdlFileNumber", paramMap);
                        sqlSessionTemplate.update("SQL.Send.updateSendSchdlCnt", paramMap);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static class CompareFileNameAsc implements Comparator<String>{

        @Override
        public int compare(String o1, String o2) {
            // TODO Auto-generated method stub
            return o1.compareTo(o2);
        }
    }
}
