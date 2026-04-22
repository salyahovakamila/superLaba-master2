package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.util.HashMap;

/**
 * Команда для добавления нового работника в коллекцию.
 * Запрашивает у пользователя все необходимые данные и создает работника.
 */

public class InsertCommand extends Command {
    private HashMap<String, Worker> collection;
    private CommandManager commandManager;

    public InsertCommand(HashMap<String, Worker> collection, CommandManager commandManager) {
        super("insert", "добавить элемент с ключом");
        this.collection = collection;
        this.commandManager = commandManager;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String key = request.getKey();
        Worker worker = request.getWorker();

        if (key == null || key.isEmpty()) {
            return new CommandResponse(ResponseType.ERROR, "Использование: insert ключ");
        }

        if (worker == null) {
            return new CommandResponse(ResponseType.ERROR, "Данные работника не предоставлены");
        }

        if (collection.containsKey(key)) {
            return new CommandResponse(ResponseType.ERROR, "Ключ '" + key + "' уже существует");
        }

        int id = commandManager.generateId();
        worker.setId(id);

        collection.put(key, worker);

        return new CommandResponse(ResponseType.SUCCESS,
                String.format("Добавлен с ID: %d по ключу: %s", id, key));
    }
}