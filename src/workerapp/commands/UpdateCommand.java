package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;

public class UpdateCommand extends Command {

    public UpdateCommand(HashMap<String, Worker> collection) {
        super("update", "обновить элемент по ID", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (args.isEmpty()) {
            System.out.println("❌ Использование: update id");
            return;
        }

        try {
            int id = Integer.parseInt(args.trim());

            // Ищем ключ по ID
            String foundKey = null;
            for (HashMap.Entry<String, Worker> entry : collection.entrySet()) {
                if (entry.getValue().getId() == id) {
                    foundKey = entry.getKey();
                    break;
                }
            }

            if (foundKey == null) {
                System.out.println("❌ ID " + id + " не найден");
                return;
            }

            System.out.println("\n=== ОБНОВЛЕНИЕ РАБОТНИКА ID=" + id + " ===");
            Worker worker = inputHandler.readWorker();
            worker.setId(id); // сохраняем старый ID

            collection.put(foundKey, worker);
            System.out.println("✅ Обновлен");

        } catch (NumberFormatException e) {
            System.out.println("❌ ID должен быть числом");
        }
    }
}