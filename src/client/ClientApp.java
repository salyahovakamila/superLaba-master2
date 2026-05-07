package client;

import client.console.CommandParser;
import client.console.ConsoleReader;
import client.handlers.ErrorHandler;
import client.network.RequestSender;

import java.io.IOException;

public class ClientApp {
    private RequestSender requestSender;
    private ConsoleReader consoleReader;
    private CommandParser commandParser;
    private boolean running = true;

    public static void main(String[] args) {
        ClientApp client = new ClientApp();
        client.start();
    }

    public void start() {
        System.out.println("Клиент запущен!");
        System.out.println("Сервер: " + client.utils.Constants.HOST + ":" + client.utils.Constants.PORT);
        requestSender = new RequestSender();
        consoleReader = new ConsoleReader();
        commandParser = new CommandParser(requestSender);
        System.out.println("Введите 'help' для списка команд, 'exit' для выхода\n");
        runClientLoop();
    }

    private void runClientLoop() {
        while (running) {
            try {
                System.out.print("> ");
                String input = consoleReader.readLine();
                if (input == null || input.trim().isEmpty()) continue;

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
        System.out.println("Завершение работы клиента.");
    }
}