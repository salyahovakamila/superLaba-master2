package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;
import java.util.Date;

public class InfoCommand extends Command {
    private Date initDate;

    public InfoCommand(HashMap<String, Worker> collection) {
        super("info", "информация о коллекции", collection);
        this.initDate = new Date();
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        System.out.println("📊 Тип коллекции: HashMap<String, Worker>");
        System.out.println("📅 Дата инициализации: " + initDate);
        System.out.println("📦 Количество элементов: " + collection.size());
    }
}