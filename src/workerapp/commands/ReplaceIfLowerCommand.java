package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;

public class ReplaceIfLowerCommand extends Command {

    public ReplaceIfLowerCommand(HashMap<String, Worker> collection) {
        super("replace_if_lower", "заменить по ключу, если новое значение меньше", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (args.isEmpty()) {
            System.out.println("❌ Использование: replace_if_lower ключ");
            return;
        }

        String key = args.trim();
        Worker oldWorker = collection.get(key);
        if (oldWorker == null) {
            System.out.println("❌ Ключ '" + key + "' не найден");
            return;
        }

        System.out.println("\n=== Введите нового работника ===");
        Worker newWorker = inputHandler.readWorker();

        if (newWorker.compareTo(oldWorker) < 0) {
            newWorker.setId(oldWorker.getId()); // сохраняем старый ID
            collection.put(key, newWorker);
            System.out.println("✅ Заменен по ключу: " + key);
        } else {
            System.out.println("ℹ️ Новый работник не меньше старого, замена не выполнена");
        }
    }
}