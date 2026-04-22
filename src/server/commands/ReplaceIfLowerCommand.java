package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.util.HashMap;

/**
 * Команда для замены работника по ключу, только если новый работник меньше старого.
 * Сравнение происходит по естественному порядку (метод compareTo в Worker).
 * При замене сохраняется оригинальный ID.
 */

public class ReplaceIfLowerCommand extends Command {
    private HashMap<String, Worker> collection;

    public ReplaceIfLowerCommand(HashMap<String, Worker> collection) {
        super("replace_if_lower", "заменить по ключу, если новое значение меньше");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String key = request.getKey();
        Worker newWorker = request.getWorker();

        if (key == null || key.isEmpty()) {
            return new CommandResponse(ResponseType.ERROR, "Использование: replace_if_lower ключ");
        }

        Worker oldWorker = collection.get(key);
        if (oldWorker == null) {
            return new CommandResponse(ResponseType.ERROR, "Ключ '" + key + "' не найден");
        }

        if (newWorker == null) {
            return new CommandResponse(ResponseType.ERROR, "Данные работника не предоставлены");
        }

        if (newWorker.compareTo(oldWorker) < 0) {
            newWorker.setId(oldWorker.getId());
            collection.put(key, newWorker);
            return new CommandResponse(ResponseType.SUCCESS, "Заменен по ключу: " + key);
        } else {
            return new CommandResponse(ResponseType.INFO, "Новый работник не меньше старого, замена не выполнена");
        }
    }
}