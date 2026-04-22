package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.*;

public class RemoveLowerCommand extends Command {

    public RemoveLowerCommand(HashMap<String, Worker> collection) {
        super("remove_lower", "удалить элементы, меньшие заданного", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        System.out.println("\n=== Введите работника для сравнения ===");
        Worker worker = inputHandler.readWorker();

        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, Worker> entry : collection.entrySet()) {
            if (entry.getValue().compareTo(worker) < 0) {
                toRemove.add(entry.getKey());
            }
        }

        for (String key : toRemove) {
            collection.remove(key);
        }

        System.out.println("🗑️ Удалено: " + toRemove.size());
    }
}