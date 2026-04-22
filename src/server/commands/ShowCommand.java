package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Команда для отображения всех элементов коллекции.
 * Выводит работников в отсортированном по ID порядке
 * с указанием их ключей в коллекции.
 */

 public class ShowCommand extends Command {
    private HashMap<String, Worker> collection;

    public ShowCommand(HashMap<String, Worker> collection) {
        super("show", "показать всех работников");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (collection.isEmpty()) {
            return new CommandResponse(ResponseType.INFO, "Коллекция пуста");
        }

        // Используем Stream API для сортировки
        List<Map.Entry<String, Worker>> entries = collection.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        StringBuilder result = new StringBuilder("\n=== РАБОТНИКИ ===\n");
        for (Map.Entry<String, Worker> entry : entries) {
            result.append("  [").append(entry.getKey()).append("] ").append(entry.getValue()).append("\n");
        }

        return new CommandResponse(ResponseType.COLLECTION_WITH_KEYS, result.toString(), collection);
    }
}