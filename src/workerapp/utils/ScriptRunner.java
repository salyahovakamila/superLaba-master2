package workerapp.utils;

import workerapp.commands.CommandManager;
import workerapp.input.BaseInputHandler;
import workerapp.input.ScriptInputHandler;

import java.io.*;
import java.util.*;

public class ScriptRunner {
    private static Set<String> runningScripts = new HashSet<>();

    public static void executeScript(String filename, CommandManager manager) {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("❌ Ошибка: файл '" + filename + "' не найден");
            return;
        }

        if (!file.canRead()) {
            System.out.println("❌ Ошибка: нет прав на чтение файла '" + filename + "'");
            return;
        }

        String absolutePath;
        try {
            absolutePath = file.getCanonicalPath();
        } catch (IOException e) {
            absolutePath = file.getAbsolutePath();
        }

        if (runningScripts.contains(absolutePath)) {
            System.out.println("❌ Ошибка: обнаружена рекурсия в скрипте '" + filename + "'");
            return;
        }

        runningScripts.add(absolutePath);

        try (Scanner fileScanner = new Scanner(file)) {
            System.out.println("\n📜 === ВЫПОЛНЕНИЕ СКРИПТА: " + filename + " ===");

            // Создаем обработчик для скрипта
            ScriptInputHandler scriptHandler = new ScriptInputHandler(fileScanner);

            int lineNumber = 0;
            while (fileScanner.hasNextLine()) {
                lineNumber++;
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                System.out.println("  [" + lineNumber + "] > " + line);

                try {
                    // Используем новый метод с двумя параметрами
                    boolean continueRunning = manager.executeCommand(line, scriptHandler);
                    if (!continueRunning) {
                        System.out.println("⏹️ Команда exit прервала скрипт");
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("❌ Ошибка в строке " + lineNumber + ": " + e.getMessage());
                    System.out.println("⏹️ Выполнение скрипта прервано");
                    break;
                }
            }

            System.out.println("✅ === СКРИПТ ЗАВЕРШЕН ===\n");

        } catch (FileNotFoundException e) {
            System.out.println("❌ Ошибка: файл не найден - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Ошибка при выполнении скрипта: " + e.getMessage());
        } finally {
            runningScripts.remove(absolutePath);
        }
    }
}