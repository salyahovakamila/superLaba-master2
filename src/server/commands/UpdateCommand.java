package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.util.HashMap;

/**
 * Команда для обновления существующего работника по его ID.
 * Находит работника по ID, запрашивает новые данные
 * и заменяет его, сохраняя оригинальный ID.
 */

public class UpdateCommand extends Command {
    private HashMap<String, Worker> collection;

    public UpdateCommand(HashMap<String, Worker> collection) {
        super("update", "обновить элемент по ID");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Integer id = request.getId();
        Worker worker = request.getWorker();

        if (id == null) {
            return new CommandResponse(ResponseType.ERROR, "Использование: update id");
        }

        String foundKey = null;
        for (HashMap.Entry<String, Worker> entry : collection.entrySet()) {
            Worker w = entry.getValue();
            if (w.getId() != null && w.getId().equals(id)) {  // ИСПРАВЛЕНО
                foundKey = entry.getKey();
                break;
            }
        }

        if (foundKey == null) {
            return new CommandResponse(ResponseType.ERROR, "ID " + id + " не найден");
        }

        if (worker == null) {
            return new CommandResponse(ResponseType.ERROR, "Данные работника не предоставлены");
        }

        worker.setId(id);
        collection.put(foundKey, worker);

        return new CommandResponse(ResponseType.SUCCESS, "Обновлен");
    }
}