package client.network;

import client.utils.Constants;
import common.CommandRequest;
import common.CommandResponse;
import common.SerializationHelper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestSender {

    public CommandResponse sendAndReceive(CommandRequest request) throws IOException {
        SocketChannel channel = null;
        try {
            channel = TCPClient.connect();
            // Отправка
            byte[] requestBytes = SerializationHelper.serialize(request);
            ByteBuffer writeBuffer = ByteBuffer.allocate(4 + requestBytes.length);
            writeBuffer.putInt(requestBytes.length);
            writeBuffer.put(requestBytes);
            writeBuffer.flip();
            while (writeBuffer.hasRemaining()) {
                channel.write(writeBuffer);
            }

            // Приём ответа
            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            long deadline = System.currentTimeMillis() + Constants.TIMEOUT_MS;
            while (lengthBuffer.hasRemaining()) {
                if (System.currentTimeMillis() > deadline) {
                    throw new IOException("Таймаут чтения длины ответа");
                }
                int read = channel.read(lengthBuffer);
                if (read == -1) throw new IOException("Сервер закрыл соединение");
                if (read == 0) Thread.yield();
            }
            lengthBuffer.flip();
            int dataLength = lengthBuffer.getInt();
            if (dataLength <= 0 || dataLength > Constants.BUFFER_SIZE) {
                throw new IOException("Некорректный размер данных: " + dataLength);
            }

            ByteBuffer dataBuffer = ByteBuffer.allocate(dataLength);
            deadline = System.currentTimeMillis() + Constants.TIMEOUT_MS;
            while (dataBuffer.hasRemaining()) {
                if (System.currentTimeMillis() > deadline) {
                    throw new IOException("Таймаут чтения данных ответа");
                }
                int read = channel.read(dataBuffer);
                if (read == -1) throw new IOException("Сервер закрыл соединение");
                if (read == 0) Thread.yield();
            }
            dataBuffer.flip();
            byte[] responseBytes = new byte[dataLength];
            dataBuffer.get(responseBytes);

            CommandResponse response = SerializationHelper.deserialize(responseBytes, CommandResponse.class);
            if (response == null) throw new IOException("Не удалось десериализовать ответ");
            return response;
        } finally {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        }
    }
}