package server.commands;

import models.Worker;
import server.io.FileManager;

import java.util.HashMap;

public class CommandManager {
    private HashMap<String, Worker> collection;
    private int nextId;
    private String fileName;

    public CommandManager(HashMap<String, Worker> collection) {
        this.collection = collection;
        this.nextId = calculateNextId();
    }

    private int calculateNextId() {
        int maxId = 0;
        for (Worker w : collection.values()) {
            if (w.getId() > maxId) {
                maxId = w.getId();
            }
        }
        return maxId + 1;
    }

    public synchronized int generateId() {
        return nextId++;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void saveCollection() {
        if (fileName != null && !fileName.isEmpty()) {
            FileManager fileManager = new FileManager();
            fileManager.saveToFile(fileName, collection);
            System.out.println("Коллекция сохранена в " + fileName);
        }
    }

    public HashMap<String, Worker> getCollection() {
        return collection;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}