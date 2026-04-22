package server.network;

import common.CommandRequest;
import common.CommandResponse;
import common.SerializationHelper;
import server.commands.CommandExecutor;
import server.utils.Constants;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientHandler {
    private final SocketChannel channel;
    private final CommandExecutor commandExecutor;
    private final ByteBuffer readBuffer;
    private final ByteBuffer writeBuffer;

    public ClientHandler(SocketChannel channel, CommandExecutor commandExecutor) {
        this.channel = channel;
        this.commandExecutor = commandExecutor;
        this.readBuffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        this.writeBuffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
    }

    public void handleRequest() throws IOException {
        readBuffer.clear();
        readBuffer.limit(4);

        int bytesRead = channel.read(readBuffer);
        if (bytesRead == -1) {
            System.out.println("Клиент отключился: " + channel.getRemoteAddress());
            channel.close();
            return;
        }

        if (bytesRead < 4) return;

        readBuffer.flip();
        int dataLength = readBuffer.getInt();

        if (dataLength <= 0 || dataLength > Constants.BUFFER_SIZE - 4) {
            sendErrorResponse("Некорректный размер данных");
            return;
        }

        readBuffer.clear();
        readBuffer.limit(dataLength);
        bytesRead = channel.read(readBuffer);

        if (bytesRead != dataLength) {
            sendErrorResponse("Неполучены данные команды");
            return;
        }

        readBuffer.flip();
        byte[] requestBytes = new byte[dataLength];
        readBuffer.get(requestBytes);

        CommandRequest request = SerializationHelper.deserialize(requestBytes, CommandRequest.class);
        if (request == null) {
            sendErrorResponse("Не удалось десериализовать запрос");
            return;
        }

        System.out.println("Команда: " + request.getType() + " от " + channel.getRemoteAddress());

        CommandResponse response = commandExecutor.execute(request);
        sendResponse(response);
    }

    private void sendResponse(CommandResponse response) throws IOException {
        byte[] responseBytes = SerializationHelper.serialize(response);

        writeBuffer.clear();
        writeBuffer.putInt(responseBytes.length);
        writeBuffer.put(responseBytes);
        writeBuffer.flip();

        while (writeBuffer.hasRemaining()) {
            channel.write(writeBuffer);
        }
    }

    private void sendErrorResponse(String errorMessage) throws IOException {
        CommandResponse response = new CommandResponse(common.ResponseType.ERROR, errorMessage);
        sendResponse(response);
    }
}