package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.*;

public class ShowCommand extends Command {

    public ShowCommand(HashMap<String, Worker> collection) {
        super("show", "показать всех работников", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (collection.isEmpty()) {
            System.out.println("📭 Коллекция пуста");
            return;
        }

        System.out.println("\n=== РАБОТНИКИ ===");
        List<Worker> sorted = new ArrayList<>(collection.values());
        Collections.sort(sorted);

        for (Worker w : sorted) {
            String key = getKeyByWorker(w);
            System.out.println("  [" + key + "] " + w);
        }
    }

    private String getKeyByWorker(Worker worker) {
        for (Map.Entry<String, Worker> entry : collection.entrySet()) {
            if (entry.getValue().equals(worker)) {
                return entry.getKey();
            }
        }
        return "?";
    }
}