package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.*;

public class RemoveLowerKeyCommand extends Command {

    public RemoveLowerKeyCommand(HashMap<String, Worker> collection) {
        super("remove_lower_key", "удалить ключи меньше заданного", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (args.isEmpty()) {
            System.out.println("❌ Использование: remove_lower_key ключ");
            return;
        }

        String key = args.trim();
        List<String> toRemove = new ArrayList<>();

        for (String k : collection.keySet()) {
            if (k.compareTo(key) < 0) {
                toRemove.add(k);
            }
        }

        for (String k : toRemove) {
            collection.remove(k);
        }

        System.out.println("🗑️ Удалено ключей: " + toRemove.size());
    }
}