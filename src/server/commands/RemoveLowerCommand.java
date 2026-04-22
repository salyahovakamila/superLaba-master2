package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Команда для удаления всех работников, которые меньше заданного.
 * Сравнение происходит по естественному порядку (метод compareTo в Worker).
 */

public class RemoveLowerCommand extends Command {
    private HashMap<String, Worker> collection;

    public RemoveLowerCommand(HashMap<String, Worker> collection) {
        super("remove_lower", "удалить элементы, меньшие заданного");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        // ИСПРАВЛЕНО: используем getWorker() (это уже правильно)
        Worker worker = request.getWorker();

        if (worker == null) {
            return new CommandResponse(ResponseType.ERROR, "Данные работника не предоставлены");
        }

        // Используем Stream API для поиска ключей для удаления
        List<String> toRemove = collection.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(worker) < 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (String key : toRemove) {
            collection.remove(key);
        }

        return new CommandResponse(ResponseType.SUCCESS, "Удалено: " + toRemove.size());
    }
}