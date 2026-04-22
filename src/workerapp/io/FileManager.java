package workerapp.io;

import workerapp.models.Worker;
import java.io.*;
import java.util.*;

public class FileManager {

    // Загрузка коллекции из файла
    public static HashMap<String, Worker> loadFromFile(String filename) {
        HashMap<String, Worker> collection = new HashMap<>();

        File file = new File(filename);

        // Проверка существования файла
        if (!file.exists()) {
            System.out.println("Файл не существует: " + filename);
            System.out.println("Будет создана новая коллекция");
            return collection;
        }

        // Проверка прав доступа
        if (!file.canRead()) {
            System.out.println("Ошибка: нет прав на чтение файла " + filename);
            return collection;
        }

        // Чтение файла через Scanner
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder jsonContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine());
            }

            // Парсим JSON массив
            String content = jsonContent.toString().trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();

                if (!content.isEmpty()) {
                    // Разделяем объекты
                    List<String> workerJsons = splitJsonArray(content);

                    for (String workerJson : workerJsons) {
                        try {
                            Worker worker = (Worker) JsonHelper.jsonToWorker(workerJson);
                            collection.put(String.valueOf(worker.getId()), worker);
                        } catch (Exception e) {
                            System.out.println("Ошибка при парсинге работника: " + e.getMessage());
                        }
                    }
                }
            }

            System.out.println("Загружено работников: " + collection.size());

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("Ошибка доступа: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return collection;
    }

    // Сохранение коллекции в файл
    public static void saveToFile(String filename, HashMap<String, Worker> data) {
        if (filename == null || filename.isEmpty()) {
            System.out.println("Имя файла не указано");
            return;
        }

        File file = new File(filename);

        // Создаем родительские директории если нужно
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Проверка прав на запись
        try {
            if (file.exists() && !file.canWrite()) {
                System.out.println("Ошибка: нет прав на запись в файл " + filename);
                return;
            }
        } catch (SecurityException e) {
            System.out.println("Ошибка доступа: " + e.getMessage());
            return;
        }

        // Запись через BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[");
            writer.newLine();

            int count = 0;
            for (Worker worker : data.values()) {
                if (count > 0) {
                    writer.write(",");
                    writer.newLine();
                }
                writer.write("  " + worker.toJson());
                count++;
            }

            writer.newLine();
            writer.write("]");

            System.out.println("Сохранено работников: " + count + " в файл " + filename);

        } catch (IOException e) {
            System.out.println("Ошибка при записи файла: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("Ошибка доступа: " + e.getMessage());
        }
    }

    // Получение имени файла из переменной окружения
    public static String parseEnvVariable(String varName) {
        try {
            return System.getenv(varName);
        } catch (SecurityException e) {
            System.out.println("Ошибка при доступе к переменной окружения: " + e.getMessage());
            return null;
        }
    }

    // Упрощенное разделение JSON массива на объекты
    private static List<String> splitJsonArray(String arrayContent) {
        List<String> result = new ArrayList<>();

        int braceLevel = 0;
        StringBuilder current = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < arrayContent.length(); i++) {
            char c = arrayContent.charAt(i);

            if (c == '"' && (i == 0 || arrayContent.charAt(i-1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{') {
                    braceLevel++;
                }
                if (c == '}') {
                    braceLevel--;
                }
            }

            current.append(c);

            if (braceLevel == 0 && current.length() > 0) {
                String obj = current.toString().trim();
                if (!obj.isEmpty() && !obj.equals(",")) {
                    if (obj.endsWith(",")) {
                        obj = obj.substring(0, obj.length() - 1);
                    }
                    result.add(obj);
                }
                current = new StringBuilder();
            }
        }

        return result;
    }
}
