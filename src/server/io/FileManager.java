package server.io;

import models.Worker;
import java.io.*;
import java.util.HashMap;

public class FileManager {

    public HashMap<String, Worker> loadFromFile(String fileName) {
        HashMap<String, Worker> collection = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Файл не найден: " + fileName);
            return collection;
        }

        if (file.length() == 0) {
            System.out.println("Файл пуст");
            return collection;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line.trim());
            }

            String content = jsonContent.toString();
            if (content.isEmpty()) return collection;

            collection = parseWorkersArray(content);
            System.out.println("Загружено работников: " + collection.size());

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return collection;
    }

    private HashMap<String, Worker> parseWorkersArray(String json) {
        HashMap<String, Worker> collection = new HashMap<>();

        try {
            json = json.trim();
            if (!json.startsWith("[") || !json.endsWith("]")) {
                System.err.println("Ошибка: файл должен содержать JSON массив");
                return collection;
            }

            String content = json.substring(1, json.length() - 1).trim();
            if (content.isEmpty()) return collection;

            int braceDepth = 0;
            int start = -1;

            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);

                if (c == '{') {
                    if (braceDepth == 0) start = i;
                    braceDepth++;
                } else if (c == '}') {
                    braceDepth--;
                    if (braceDepth == 0 && start != -1) {
                        String workerJson = content.substring(start, i + 1);
                        try {
                            Worker worker = JsonHelper.jsonToWorker(workerJson);
                            if (worker != null && worker.getId() != null) {
                                collection.put(String.valueOf(worker.getId()), worker);
                            }
                        } catch (Exception e) {
                            System.err.println("Ошибка парсинга работника: " + e.getMessage());
                        }
                        start = -1;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка разбора JSON: " + e.getMessage());
        }

        return collection;
    }

    public boolean saveToFile(String fileName, HashMap<String, Worker> collection) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            StringBuilder jsonArray = new StringBuilder();
            jsonArray.append("[");

            boolean first = true;
            for (Worker worker : collection.values()) {
                if (!first) jsonArray.append(",");
                jsonArray.append(JsonHelper.workerToJson(worker));
                first = false;
            }

            jsonArray.append("]");
            writer.write(jsonArray.toString());
            writer.flush();
            return true;

        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
            return false;
        }
    }
}