package workerapp;

import workerapp.commands.CommandManager;
import workerapp.input.ConsoleInputHandler;
import workerapp.io.FileManager;
import workerapp.models.Worker;

import java.util.HashMap;

/**
 * Главный класс программы
 */
public class Main {
    public static void main(String[] args) {
        // 1. Получаем имя файла из переменной окружения или используем по умолчанию
        String fileName = System.getenv("WORKER_DATA");
        if (fileName == null) {
            System.out.println("Используется файл по умолчанию: data/workers.json");
            fileName = "data/workers.json";
        }

        // 2. Создаем FileManager и загружаем коллекцию
        FileManager fileManager = new FileManager();
        HashMap<String, Worker> collection = fileManager.loadFromFile(fileName);

        System.out.println("=== Программа управления работниками ===");
        System.out.println("Введите 'help' для списка команд");

        // 3. Создаем CommandManager с загруженной коллекцией
        CommandManager commandManager = new CommandManager(collection);
        commandManager.setFileName(fileName); // передаем имя файла для save

        // 4. Создаем InputHandler
        ConsoleInputHandler inputHandler = new ConsoleInputHandler();

        // 5. ВАЖНО: Регистрируем все команды!
        commandManager.registerCommands(
                inputHandler,           // для ввода данных
                fileName,               // для сохранения
                () -> {                  // действие при exit
                    System.out.println("👋 До свидания!");
                    System.exit(0);
                }
        );

        // 6. Запускаем главный цикл обработки команд
        runCommandLoop(inputHandler, commandManager);
    }

    /**
     * Главный цикл обработки команд
     */
    private static void runCommandLoop(ConsoleInputHandler inputHandler, CommandManager commandManager) {
        while (true) {
            System.out.print("\n> ");
            String input = inputHandler.readLine();

            if (input.trim().isEmpty()) {
                continue;
            }

            boolean shouldContinue = commandManager.executeCommand(input);
            if (!shouldContinue) {
                break; // выход из цикла
            }
        }
    }
}