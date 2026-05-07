package client.network;

import client.utils.Constants;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class TCPClient {
    public static SocketChannel connect() throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(Constants.HOST, Constants.PORT));
        // Ожидаем завершения соединения с таймаутом
        long deadline = System.currentTimeMillis() + Constants.TIMEOUT_MS;
        while (!channel.finishConnect()) {
            if (System.currentTimeMillis() > deadline) {
                throw new IOException("Таймаут подключения");
            }
            Thread.yield();
        }
        return channel;
    }
}