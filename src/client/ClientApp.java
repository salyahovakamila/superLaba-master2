package client;

import client.console.CommandParser;
import client.console.ConsoleReader;
import client.handlers.ErrorHandler;
import client.handlers.ResponseHandler;
import client.network.TCPClient;
import client.network.RequestSender;
import client.utils.Constants;
import workerapp.commands.Command;

import java.io.IOException;

public class ClientApp {
    private TCPClient client;
    private RequestSender requestSender;
    private ResponseHandler responseHandler;
    private ConsoleReader consoleReader;
    private CommandParser commandParser;
    private boolean running = true;
    public static void main(String[] args){
        ClientApp client =new ClientApp();
        try {
            client.start();
        } catch (IOException e){
            System.err.println("Ошибка запуска клиента: " + e.getMessage());
            System.exit(1);
        }
    }
public void start() throws IOException {
        System.out.println("Клиент запущен!");
        System.out.println("Подключение к серверу " + Constants.HOST + ":" + Constants.PORT);
        client = new TCPClient(Constants.HOST, Constants.PORT);
        requestSender= new RequestSender(client.getChannel());
        responseHandler =new ResponseHandler(client.getChannel(), client.getSelector());
        consoleReader=new ConsoleReader();
        commandParser =new CommandParser();
        System.out.println(" Подключено к серверу!");
        System.out.println("Введите 'help' для списка команд, 'exit' для выхода\n");
        runClientLoop();
    }
    private void runClientLoop(){
        while (running) {
            try {
                System.out.print("> ");
                String input =consoleReader.readLine();
                if (input == null || input.trim().isEmpty()){
                    continue;
                }
                Command command=commandParser.parser(input);
                if (command == null) {
                    continue;
                }
                if (command.getType() ==CommandType.EXIT){
                    System.out.println("До свидания! Сервер продолжает работать.");
                    running = false;
                    break;
                }
                requestSender.send(command);
                responseHandler.handleResponse();
            } catch (Exception e){
                ErrorHandler.handle(e);
            }
        }
        close();
    }
    private void close() {
        System.out.println("Закрытие соединения...");
        try{
            if (selector != null && selector.isOpen()){
                selector.close();
            }
            if (client != null) {
                client.close();
            }
            System.out.println("Соединение закрыто.");
        } catch (IOException e){
            System.err.println("Ошибка при закрытии: " + e.getMessage());
        }
    }
}
