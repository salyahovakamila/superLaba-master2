package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.util.Date;
import java.util.HashMap;

/**
 * Команда для отображения информации о коллекции.
 * Показывает тип коллекции, дату инициализации и количество элементов.
 */

 public class InfoCommand extends Command {
    private HashMap<String, Worker> collection;
    private Date initDate;

    public InfoCommand(HashMap<String, Worker> collection) {
        super("info", "информация о коллекции");
        this.collection = collection;
        this.initDate = new Date();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String info = String.format(
                "Тип коллекции: HashMap<String, Worker>%n" +
                        "Дата инициализации: %s%n" +
                        "Количество элементов: %d",
                initDate, collection.size()
        );
        return new CommandResponse(ResponseType.INFO, info);
    }
}