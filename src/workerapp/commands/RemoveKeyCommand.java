package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;

public class RemoveKeyCommand extends Command {

    public RemoveKeyCommand(HashMap<String, Worker> collection) {
        super("remove_key", "удалить элемент по ключу", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (args.isEmpty()) {
            System.out.println("❌ Использование: remove_key ключ");
            return;
        }

        String key = args.trim();
        if (collection.remove(key) != null) {
            System.out.println("✅ Ключ '" + key + "' удален");
        } else {
            System.out.println("❌ Ключ '" + key + "' не найден");
        }
    }
}