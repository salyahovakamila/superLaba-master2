package client.network;

import client.utils.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Неблокирующий коннектор для подключения к серверу.
 * Зачем: реализует требование "сетевые каналы в неблокирующем режиме".
 */
public class TCPClient {

    // Канал для связи с сервером (аналог сокета, но NIO)
    private final SocketChannel channel;

    // Селектор для мониторинга событий (готовность к чтению/записи)
    private final Selector selector;

    // Флаг: подключены ли мы к серверу
    private boolean connected = false;

    /**
     * Конструктор: создаёт подключение.
     * @param host адрес сервера
     * @param port порт сервера
     * @throws IOException если не удалось подключиться
     */
    public TCPClient(String host, int port) throws IOException {
        // Шаг 1: Открываем новый SocketChannel
        this.channel = SocketChannel.open();


        this.channel.configureBlocking(false);


        this.selector = Selector.open();

        // Шаг 4: Начинаем асинхронное подключение
        // connect() возвращает сразу, не ждёт ответа сервера
        boolean connectedImmediately = channel.connect(new InetSocketAddress(host, port));

        // Шаг 5: Если подключение не завершилось сразу — ждём через селектор
        if (!connectedImmediately) {
            // Регистрируем канал на событие OP_CONNECT (завершение подключения)
            channel.register(selector, SelectionKey.OP_CONNECT);

            // Ждём события подключения (таймаут 10 секунд)
            int readyChannels = selector.select(Constants.TIMEOUT_MS);

            // Если ничего не готово — таймаут
            if (readyChannels == 0) {
                throw new IOException("Таймаут подключения к серверу");
            }


            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isConnectable()) {
                    if (channel.finishConnect()) {
                        connected = true;
                    } else {
                        throw new IOException("Не удалось завершить подключение");
                    }
                }

                selector.selectedKeys().remove(key);
            }
        } else {

            connected = true;
        }


        channel.register(selector, SelectionKey.OP_READ);
    }


     // Возвращает SocketChannel.

    public SocketChannel getChannel() {
        return channel;
    }


    public Selector getSelector() {
        return selector;
    }

    public boolean isConnected() {
        return connected && channel.isConnected();
    }

    /**
     * Закрывает соединение.
     */
    public void close() throws IOException {
        // Закрываем селектор
        if (selector != null && selector.isOpen()) {
            selector.close();
        }

        // Закрываем канал
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }
}