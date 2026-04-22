package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;
import java.util.function.Supplier;

public class InsertCommand extends Command {
    private Supplier<Integer> idGenerator;

    public InsertCommand(HashMap<String, Worker> collection, Supplier<Integer> idGenerator) {
        super("insert", "добавить элемент с ключом", collection);
        this.idGenerator = idGenerator;
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (args.isEmpty()) {
            System.out.println("❌ Использование: insert ключ");
            return;
        }

        String key = args.trim();
        if (collection.containsKey(key)) {
            System.out.println("❌ Ключ '" + key + "' уже существует");
            return;
        }

        System.out.println("\n=== НОВЫЙ РАБОТНИК ===");
        Worker worker = inputHandler.readWorker();
        worker.setId(idGenerator.get());

        collection.put(key, worker);
        System.out.println("✅ Добавлен с ID: " + worker.getId() + " по ключу: " + key);
    }
}