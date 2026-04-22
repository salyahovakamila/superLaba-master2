package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;
import java.util.HashMap;

/**
 * Команда для удаления работника по его ключу.
 * Просто удаляет пару ключ-значение из коллекции.
 */

public class RemoveKeyCommand extends Command {
    private HashMap<String, Worker> collection;

    public RemoveKeyCommand(HashMap<String, Worker> collection) {
        super("remove_key", "удалить элемент по ключу");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String key = request.getKey();

        if (key == null || key.isEmpty()) {
            return new CommandResponse(ResponseType.ERROR, "Использование: remove_key ключ");
        }

        if (collection.remove(key) != null) {
            return new CommandResponse(ResponseType.SUCCESS, "Ключ '" + key + "' удален");
        } else {
            return new CommandResponse(ResponseType.ERROR, "Ключ '" + key + "' не найден");
        }
    }
}