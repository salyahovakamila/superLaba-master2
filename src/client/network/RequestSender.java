package client.network;

import client.utils.Constants;
import java.io.*;
import java.util.Objects;

public class RequestSender {
    private final TCPClient tcpClient;
    private final String host;
    private final int port;


    public RequestSender() throws IOException {
        this(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT);
    }


    public RequestSender(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.tcpClient = new TCPClient(host, port, Constants.MAX_RETRIES);
    }


    public RequestSender(TCPClient tcpClient) {
        this.tcpClient = Objects.requireNonNull(tcpClient);
        this.host = Constants.DEFAULT_HOST;
        this.port = Constants.DEFAULT_PORT;
    }

    @SuppressWarnings("unchecked")
    public <T> T sendAndReceive(Serializable request, Class<T> responseType)
            throws IOException, ClassNotFoundException {

        byte[] requestBytes = serialize(request);

        try {
            tcpClient.send(requestBytes);
            byte[] responseBytes = tcpClient.receive();
            return (T) deserialize(responseBytes);
        } catch (IOException e) {
            // Обработка временной недоступности
            if (tcpClient.reconnect(host, port)) {
                tcpClient.send(requestBytes);
                byte[] responseBytes = tcpClient.receive();
                return (T) deserialize(responseBytes);
            }
            throw new IOException("Сервер недоступен после попытки переподключения", e);
        }
    }

    public void sendOnly(Serializable request) throws IOException {
        try {
            tcpClient.send(serialize(request));
        } catch (IOException e) {
            if (tcpClient.reconnect(host, port)) {
                tcpClient.send(serialize(request));
            } else {
                throw e;
            }
        }
    }

    private byte[] serialize(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }

    public boolean isConnected() {
        return tcpClient.isConnected();
    }

    public void close() throws IOException {
        tcpClient.close();
    }
}
