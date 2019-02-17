package gs.mail.engine.util;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gs.mail.engine.dto.Send;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;

@Slf4j
public class SmtpSocket {

    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    protected static int port = 25;

//    protected void socketSend(Socket socket, String gubun, String dirPath, Send send){
//        String carriageReturn = "\r\n";
//        PrintStream ps = null;
//        BufferedReader br = null;
//        PrintWriter sendLog = null;
//
//        try{
//            synchronized( this ) {
//                ps = new PrintStream(socket.getOutputStream(), true, "euc-kr");
//                br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "euc-kr"));
//
//                String fileDir = dirPath;
//                if (gubun.equals("C")) {
//                    fileDir = fileDir + "campaign/";
//                } else if (gubun.equals("R")) {
//                    fileDir = fileDir + "realtime/";
//                }
//
//                Date today = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//
//                File dir = new File(fileDir);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                File file = new File(fileDir + "sendLog_"+send.getMasterSchdlId()+"_" + sdf.format(today) + ".log");
//                sendLog = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);
//
//                sendLog.print("{\"SOCKET_CONN\":\""+subStringResult(br.readLine())+"\",");
//                sendLog.print("\"SCHDL_ID\":\""+send.getSchdlId()+"\",");
//                sendLog.print("\"UUID\":\""+send.getUuid()+"\",");
//                ps.print("HELO "+send.getReceiver().substring(send.getReceiver().indexOf("@")+1)+carriageReturn );
//                sendLog.print("\"HELO_RES\":\""+subStringResult(br.readLine())+"\",");
//
//                ps.print("MAIL FROM: <"+send.getSender()+">"+carriageReturn);
//                sendLog.print("\"MAIL_FROM_RES\":\""+subStringResult(br.readLine())+"\",");
//
//                ps.print("RCPT TO:<"+send.getReceiver()+">"+carriageReturn);
//                sendLog.print("\"RCPT_TO_RES\":\""+subStringResult(br.readLine())+"\",");
//
//                ps.print("DATA"+carriageReturn);
//                sendLog.print("\"DATA_RES\":\""+subStringResult(br.readLine())+"\",");
//                ps.print("Mime-Version: 1.0"+carriageReturn);
//                ps.print("Content-Type:text/html;charset=euc-kr"+carriageReturn);
//                ps.print("Content-Transfer-Encoding:8bit"+carriageReturn);
//                ps.print("Subject:"+send.getTitle()+carriageReturn);
//                ps.print("From:"+send.getSender()+carriageReturn);
//                ps.print("To:"+send.getReceiver()+carriageReturn);
//                ps.print("Date: "+new Date()+carriageReturn);
//                ps.print(carriageReturn);
//                ps.print(send.getContents()+carriageReturn);
//                ps.print("."+carriageReturn);
//                ps.print("QUIT"+carriageReturn);
//                ps.flush();
//
//                sendLog.print("\"DATA_SEND_RES\":\""+subStringResult(br.readLine())+"\",");
//                sendLog.print("\"SEND_RES\":\""+subStringResult(br.readLine())+"\"}");
//                sendLog.println();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

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
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
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

                writeBuffer.clear();
                writeBuffer.put(charset.encode("HELO "+send.getReceiver().substring(send.getReceiver().indexOf("@")+1)+carriageReturn));
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
                log.info("#### : {}", socketChannel.read(readBuffer));

                writeBuffer.clear();
                writeBuffer.put(charset.encode("MAIL FROM: <"+send.getSender()+">"+carriageReturn));
                writeBuffer.flip();
                socketChannel.write(writeBuffer);

                writeBuffer.clear();
                writeBuffer.put(charset.encode("RCPT TO:<"+send.getReceiver()+">"+carriageReturn));
                writeBuffer.flip();
                socketChannel.write(writeBuffer);

                writeBuffer.clear();
                writeBuffer.put(charset.encode("DATA "+carriageReturn));
                writeBuffer.flip();
                socketChannel.write(writeBuffer);

                writeBuffer.clear();
                writeBuffer.put(charset.encode("Mime-Version: 1.0"+carriageReturn));
                writeBuffer.put(charset.encode("Content-Type:text/html;charset=euc-kr"+carriageReturn));
                writeBuffer.put(charset.encode("Content-Transfer-Encoding:8bit"+carriageReturn));
                writeBuffer.put(charset.encode("Subject:"+send.getTitle()+carriageReturn));
                writeBuffer.put(charset.encode("From:"+send.getSender()+carriageReturn));
                writeBuffer.put(charset.encode("To:"+send.getReceiver()+carriageReturn));
                writeBuffer.put(charset.encode("Date: "+new Date()+carriageReturn));
                writeBuffer.put(charset.encode(carriageReturn));
                writeBuffer.put(charset.encode(send.getContents()+carriageReturn));
                writeBuffer.put(charset.encode("."+carriageReturn));
                writeBuffer.put(charset.encode("QUIT"+carriageReturn));
                writeBuffer.flip();
                socketChannel.write(writeBuffer);

                //socketChannel.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void socketSendNio2(Send send, String gubun, String dirPath) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        String carriageReturn = "\r\n";
        Charset charset = Charset.forName("euc-kr");

        try {
            AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withThreadPool(Executors.newSingleThreadExecutor());
            final AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open(asynchronousChannelGroup);

            if (asynchronousSocketChannel.isOpen()) {
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

                //asynchronousSocketChannel.connect(new InetSocketAddress(, port), null, new CompletionHandler<Void, Void>() {
                asynchronousSocketChannel.connect(new InetSocketAddress("119.207.76.55", port), null, new CompletionHandler<Void, Void>() {
                    @Override
                    public void completed(Void result, Void attachment) {
                        try{
                            byteBuffer.clear();
                            byteBuffer.put(charset.encode("HELO " + send.getReceiver().substring(send.getReceiver().indexOf("@") + 1) + carriageReturn));
                            byteBuffer.flip();
                            asynchronousSocketChannel.write(byteBuffer).get();

                            byteBuffer.clear();
                            byteBuffer.put(charset.encode("MAIL FROM: <" + send.getSender() + ">" + carriageReturn));
                            byteBuffer.flip();
                            asynchronousSocketChannel.write(byteBuffer).get();

                            byteBuffer.clear();
                            byteBuffer.put(charset.encode("RCPT TO:<" + send.getReceiver() + ">" + carriageReturn));
                            byteBuffer.flip();
                            asynchronousSocketChannel.write(byteBuffer).get();

                            byteBuffer.clear();
                            byteBuffer.put(charset.encode("DATA " + carriageReturn));
                            byteBuffer.flip();
                            asynchronousSocketChannel.write(byteBuffer).get();

                            byteBuffer.clear();
                            byteBuffer.put(charset.encode("Mime-Version: 1.0" + carriageReturn));
                            byteBuffer.put(charset.encode("Content-Type:text/html;charset=euc-kr" + carriageReturn));
                            byteBuffer.put(charset.encode("Content-Transfer-Encoding:8bit" + carriageReturn));
                            byteBuffer.put(charset.encode("Subject:" + send.getTitle() + carriageReturn));
                            byteBuffer.put(charset.encode("From:" + send.getSender() + carriageReturn));
                            byteBuffer.put(charset.encode("To:" + send.getReceiver() + carriageReturn));
                            byteBuffer.put(charset.encode("Date: " + new Date() + carriageReturn));
                            byteBuffer.put(charset.encode(carriageReturn));
                            byteBuffer.put(charset.encode(send.getContents() + carriageReturn));
                            byteBuffer.put(charset.encode("." + carriageReturn));
                            byteBuffer.put(charset.encode("QUIT" + carriageReturn));
                            byteBuffer.flip();
                            asynchronousSocketChannel.write(byteBuffer).get();


                            StringBuilder sb = new StringBuilder();
                            sb.append(send.getUuid()).append("||");
                            while (asynchronousSocketChannel.read(readBuffer).get() != -1){
                                readBuffer.flip();

                                sb.append(Charset.defaultCharset().decode(readBuffer).toString().replaceAll(carriageReturn, "||"));

                                if(readBuffer.hasRemaining()){
                                    readBuffer.compact();
                                }else{
                                    readBuffer.clear();
                                }
                            }

                            String fileDir = dirPath;
                            if ("C".equals(gubun)) {
                                fileDir = fileDir + "/campaign/";
                            } else if ("R".equals(gubun)) {
                                fileDir = fileDir + "/realtime/";
                            }

                            Date today = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
                            String fileName = "sendLog_"+send.getMasterSchdlId()+"_" + sdf.format(today) + ".log";

                            if(send.getLogFile() == null || !send.getLogFile().equals(fileName)){
                                HashMap<String, Object> paramMap = new HashMap<String, Object>();
                                paramMap.put("schdlId", send.getSchdlId());
                                paramMap.put("lineNumber", "0");
                                paramMap.put("logFile", fileName);
                                sqlSessionTemplate.update("SQL.Send.updateSendSchdlLogFile", paramMap);
                            }

                            Path path = Paths.get(fileDir + fileName);
                            Files.write(path, (sb.substring(0, sb.lastIndexOf("||"))+carriageReturn).getBytes(), StandardOpenOption.APPEND);

                            asynchronousSocketChannel.shutdownInput();
                            asynchronousSocketChannel.shutdownInput();
                            asynchronousSocketChannel.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        try{
                            asynchronousSocketChannel.close();
                            log.info("#### fail ###");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void socketResult(String gubun, String dirPath, long schdlId){
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("schdlId", schdlId);
        String sendFlag = "";
        String resultCd = "";
        String uuid = "";
        Send send = sqlSessionTemplate.selectOne("SQL.Send.selectResultFileInfo", paramMap);
        int successCnt = 0;
        int failCnt = 0;

        String fileDir = dirPath;
        if (gubun.equals("C")) {
            fileDir = fileDir + "/campaign/";
        } else if (gubun.equals("R")) {
            fileDir = fileDir + "/realtime/";
        }

        try{
            if(send != null && send.getLogFile() != null && send.getLogFile() != ""){
                long lineNum = Long.parseLong(send.getLineNumber());

                if (send.getLogFile() != null && Files.exists(Paths.get(fileDir + send.getLogFile()))) {
                    List<String> lineList = new ArrayList<String>();
                    Files.lines(Paths.get(fileDir + send.getLogFile()),Charset.forName("UTF-8"))
                            .skip(lineNum).forEachOrdered(lineList::add);

                    for (int i = 0; i < lineList.size() && i < 1000; i++) {
                        String[] listSplit = lineList.get(i).split("\\|\\|");

                        if(listSplit != null && listSplit.length > 0){
                            uuid = listSplit[0];
                            if(!listSplit[1].contains("220")){
                                sendFlag = "41";
                                resultCd = listSplit[1].substring(0,3);
                                failCnt++;
                            }else if(!listSplit[2].contains("250")){
                                sendFlag = "41";
                                resultCd = listSplit[2].substring(0,3);
                                failCnt++;
                            }else if(!listSplit[3].contains("250")){
                                sendFlag = "41";
                                resultCd = listSplit[3].substring(0,9);
                                failCnt++;
                            }else if(!listSplit[4].contains("250")){
                                sendFlag = "41";
                                resultCd = listSplit[4].substring(0,9);
                                failCnt++;
                            }else if(!listSplit[5].contains("354")){
                                sendFlag = "41";
                                resultCd = listSplit[5].substring(0,3);
                                failCnt++;
                            }else if(!listSplit[6].contains("250")){
                                sendFlag = "41";
                                resultCd = listSplit[6].substring(0,9);
                                failCnt++;
                            }else if(!listSplit[7].contains("221")){
                                sendFlag = "41";
                                resultCd = listSplit[7].substring(0,9);
                                failCnt++;
                            }else{
                                sendFlag = "40";
                                resultCd = listSplit[7].substring(0,9);
                                successCnt++;
                            }

                        }

                        paramMap.put("sendFlag", sendFlag);
                        paramMap.put("resultCd", resultCd);
                        paramMap.put("uuid", uuid);
                        paramMap.put("successCnt", successCnt);
                        paramMap.put("failCnt", failCnt);
                        sqlSessionTemplate.update("SQL.Send.updateSendQueRawSendFlag", paramMap);

                        lineNum++;
                    }
                    paramMap.put("sendFlag", sendFlag);
                    paramMap.put("resultCd", resultCd);
                    paramMap.put("uuid", uuid);
                    paramMap.put("successCnt", successCnt);
                    paramMap.put("failCnt", failCnt);
                    paramMap.put("lineNumber", lineNum);
                    paramMap.put("logFileName", send.getLogFile());
                    sqlSessionTemplate.update("SQL.Send.updateSendSchdlFileNumber", paramMap);
                    sqlSessionTemplate.update("SQL.Send.updateSendSchdlCnt", paramMap);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
