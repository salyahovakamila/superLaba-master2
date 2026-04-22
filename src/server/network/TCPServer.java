package server.network;

import server.commands.CommandExecutor;
import server.utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TCPServer {
    private final int port;
    private final CommandExecutor commandExecutor;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private volatile boolean running = true;

    public TCPServer(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

    public void start() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(port));

        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Сервер запущен на порту " + port);
        System.out.println("Ожидание подключений...");

        while (running) {
            try {
                selector.select(1000);
                if (!running) break;

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (!key.isValid()) continue;

                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            }
        }

        if (selector != null) selector.close();
        if (serverChannel != null) serverChannel.close();
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Клиент подключился: " + clientChannel.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ClientHandler handler = new ClientHandler(clientChannel, commandExecutor);
        handler.handleRequest();
    }

    public void stop() {
        running = false;
        if (selector != null) selector.wakeup();
        System.out.println("Сервер остановлен");
    }
}