package client;

import client.console.CommandParser;
import client.console.ConsoleReader;
import client.handlers.ErrorHandler;
import client.network.TCPClient;
import client.network.RequestSender;
import client.utils.Constants;

import java.io.IOException;

public class ClientApp {
    private TCPClient client;
    private RequestSender requestSender;
    private ConsoleReader consoleReader;
    private CommandParser commandParser;
    private boolean running = true;

    public static void main(String[] args) {
        ClientApp app = new ClientApp();
        try {
            app.start();
        } catch (IOException e) {
            System.err.println("Ошибка запуска клиента: " + e.getMessage());
            System.exit(1);
        }
    }

    public void start() throws IOException {
        System.out.println("Клиент запущен!");
        System.out.println("Подключение к серверу " + Constants.DEFAULT_HOST + ":" + Constants.DEFAULT_PORT);
        
        
        client = new TCPClient(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT, Constants.MAX_RETRIES);
        
        
        requestSender = new RequestSender(client);
        
        consoleReader = new ConsoleReader();
        commandParser = new CommandParser(requestSender);
        
        System.out.println("Подключено к серверу!");
        System.out.println("Введите 'help' для списка команд, 'exit' для выхода\n");
        
        runClientLoop();
    }

    private void runClientLoop() {
        while (running) {
            try {
                System.out.print("> ");
                String input = consoleReader.readLine();
                if (input == null || input.trim().isEmpty()) {
                    continue;
                }

                String cmdName = input.trim().toLowerCase().split("\\s+")[0];
                if (cmdName.equals("exit")) {
                    System.out.println("До свидания!");
                    running = false;
                    break;
                }

                commandParser.parse(input);
            } catch (Exception e) {
                ErrorHandler.handle(e);
            }
        }
        close();
    }

    private void close() {
        System.out.println("Закрытие соединения...");
        try {
            if (requestSender != null) {
                requestSender.close();
            }
            System.out.println("Соединение закрыто.");
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии: " + e.getMessage());
        }
    }
}
