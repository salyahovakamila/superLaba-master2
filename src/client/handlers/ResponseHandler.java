package client.handlers;

import common.utils.CommandResponse;
import common.utils.ResponseType;
import common.utils.SerializationHelper;
import client.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Обработчик ответов от сервера.
 * Зачем: читает байты от сервера, десериализует и выводит результат.
 */
public class ResponseHandler {

    // Канал для чтения
    private final SocketChannel channel;

    // Селектор для ожидания данных
    private final Selector selector;

    // Буфер для чтения
    private final ByteBuffer readBuffer;

    /**
     * Конструктор.
     */
    public ResponseHandler(SocketChannel channel, Selector selector) {
        this.channel = channel;
        this.selector = selector;
        this.readBuffer = ByteBuffer.allocate(8192);
    }

    /**
     * Ждёт и обрабатывает ответ от сервера.
     * @throws IOException если ошибка сети
     */
    public void handleResponse() throws IOException {
        int readyChannels = selector.select(Constants.TIMEOUT_MS);

        if (readyChannels == 0) {
            throw new IOException("Таймаут ожидания ответа от сервера");
        }


        for (SelectionKey key : selector.selectedKeys()) {
            if (key.isReadable()) {
                readBuffer.clear();
                int bytesRead = channel.read(readBuffer);


                if (bytesRead == -1) {
                    throw new IOException("Сервер закрыл соединение");
                }


                if (bytesRead == 0) {
                    continue;
                }


                readBuffer.flip();


                int dataLength = readBuffer.getInt();


                if (readBuffer.remaining() < dataLength) {
                    throw new IOException("Неполные данные от сервера");
                }


                byte[] responseBytes = new byte[dataLength];
                readBuffer.get(responseBytes);


                CommandResponse response = SerializationHelper.deserialize(responseBytes, CommandResponse.class);

                printResponse(response);
            }

            selector.selectedKeys().remove(key);
        }
    }

    /**
     * Выводит ответ в консоль.
     */
    private void printResponse(CommandResponse response) {
        switch (response.getType()) {
            case SUCCESS -> {
                System.out.println( response.getMessage());
                if (response.getData() != null && !response.getData().isEmpty()) {
                    System.out.println("\n Коллекция:");
                    response.getData().forEach(w ->
                            System.out.println("  " + w)
                    );
                }
            }
            case ERROR -> {
                // Ошибки выводим красным
                System.err.println("Ошибка: " + response.getMessage());
            }
            case INFO -> {
                // Информационные сообщения
                System.out.println( response.getMessage());
            }
            case COLLECTION -> {
                // Вывод коллекции
                System.out.println("\n Коллекция работников:");
                if (response.getData() == null || response.getData().isEmpty()) {
                    System.out.println("  (пусто)");
                } else {
                    response.getData().forEach(w ->
                            System.out.println("  " + w)
                    );
                }
            }
            default -> {
                System.out.println(response.getMessage());
            }
        }
    }
}