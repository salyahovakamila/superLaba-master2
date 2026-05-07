package client.network;

import client.utils.Constants;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TCPClient {
    private SocketChannel channel;
    private Selector selector;
    private final int maxRetries;
    private int retryCount = 0;
    private boolean connected = false;

    private final ByteBuffer readBuffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
    private final ByteBuffer writeBuffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);

    public TCPClient(String host, int port, int maxRetries) throws IOException {
        this.maxRetries = maxRetries;
        initChannel();
        connectWithRetry(host, port);
        this.connected = true;
    }

    private void initChannel() throws IOException {
        this.channel = SocketChannel.open();
        this.channel.configureBlocking(false);
        this.selector = Selector.open();
    }

    private void connectWithRetry(String host, int port) throws IOException {
        while (retryCount < maxRetries) {
            try {
                boolean immediate = channel.connect(new InetSocketAddress(host, port));
                if (immediate) {
                    channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    return;
                }

                channel.register(selector, SelectionKey.OP_CONNECT);
                if (selector.select(Constants.TIMEOUT_MS) == 0) {
                    throw new IOException("Таймаут подключения");
                }

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isConnectable()) {
                        if (channel.finishConnect()) {
                            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            return;
                        } else {
                            throw new IOException("Не удалось завершить подключение");
                        }
                    }
                }

            } catch (IOException e) {
                retryCount++;
                if (retryCount >= maxRetries) throw e;
                closeQuietly();
                try { Thread.sleep(Constants.RETRY_DELAY_MS); }
                catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        throw new IOException("Превышено количество попыток подключения");
    }

    public synchronized void send(byte[] data) throws IOException {
        if (!connected || !channel.isOpen()) throw new IOException("Клиент не подключён");

        writeBuffer.clear();
        writeBuffer.putInt(data.length);
        writeBuffer.put(data);
        writeBuffer.flip();

        while (writeBuffer.hasRemaining()) {
            if (selector.select(Constants.TIMEOUT_MS) == 0) throw new IOException("Таймаут отправки");

            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            boolean ready = false;
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (key.isWritable() && key.channel() == channel) ready = true;
            }
            if (!ready) throw new IOException("Канал не готов к записи");
            channel.write(writeBuffer);
        }
    }

    public synchronized byte[] receive() throws IOException {
        if (!connected || !channel.isOpen()) throw new IOException("Клиент не подключён");

        // Читаем длину
        readBuffer.clear();
        readBuffer.limit(4);
        readFully(4);
        readBuffer.flip();
        int length = readBuffer.getInt();

        if (length <= 0 || length > Constants.MAX_MESSAGE_SIZE) {
            throw new IOException("Некорректная длина сообщения: " + length);
        }

        // Читаем данные
        readBuffer.clear();
        readBuffer.limit(length);
        readFully(length);
        readBuffer.flip();

        byte[] result = new byte[length];
        readBuffer.get(result);
        return result;
    }

    private void readFully(int expected) throws IOException {
        while (readBuffer.hasRemaining()) {
            if (selector.select(Constants.TIMEOUT_MS) == 0) throw new IOException("Таймаут чтения");

            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            boolean ready = false;
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (key.isReadable() && key.channel() == channel) ready = true;
            }
            if (!ready) throw new IOException("Канал не готов к чтению");

            int read = channel.read(readBuffer);
            if (read == -1) throw new IOException("Сервер закрыл соединение");
        }
    }

    public boolean reconnect(String host, int port) {
        try {
            closeQuietly();
            retryCount = 0;
            initChannel();
            connectWithRetry(host, port);
            connected = true;
            return true;
        } catch (IOException e) {
            connected = false;
            return false;
        }
    }

    public boolean isConnected() {
        return connected && channel.isOpen() && channel.isConnected();
    }

    private void closeQuietly() {
        try { if (selector.isOpen()) selector.close(); } catch (IOException ignored) {}
        try { if (channel.isOpen()) channel.close(); } catch (IOException ignored) {}
    }

    public void close() throws IOException {
        connected = false;
        closeQuietly();
    }

    public Selector getSelector() { return selector; }
    public SocketChannel getChannel() { return channel; }
}
