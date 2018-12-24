package gs.mail.engine.job;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class SmtpSocketContents {
    private static SmtpSocketContents instance;
    private static ByteBuffer buffer;
    protected static Socket socket;
    protected static SocketChannel socketChannel;

    public static SmtpSocketContents start(String domain, int port) {
        if (instance == null)
            instance = new SmtpSocketContents(domain, port);
        return instance;
    }

    private SmtpSocketContents(String domain, int port) {
        try {
            socket = new Socket(domain, port);
            buffer = ByteBuffer.allocate(256);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
