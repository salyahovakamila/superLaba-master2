package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для удаления всех элементов, у которых ключ меньше заданного.
 * Сравнение ключей происходит лексикографически (по алфавиту).
 */

public class RemoveLowerKeyCommand extends Command {
    private HashMap<String, Worker> collection;

    public RemoveLowerKeyCommand(HashMap<String, Worker> collection) {
        super("remove_lower_key", "удалить ключи меньше заданного");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        // ИСПРАВЛЕНО: используем getKey() вместо getArgs()
        String key = request.getKey();

        if (key == null || key.isEmpty()) {
            return new CommandResponse(ResponseType.ERROR, "Использование: remove_lower_key ключ");
        }

        // Используем Stream API для поиска ключей для удаления
        List<String> toRemove = collection.keySet().stream()
                .filter(k -> k.compareTo(key) < 0)
                .collect(Collectors.toList());

        for (String k : toRemove) {
            collection.remove(k);
        }

        return new CommandResponse(ResponseType.SUCCESS, "Удалено ключей: " + toRemove.size());
    }
}