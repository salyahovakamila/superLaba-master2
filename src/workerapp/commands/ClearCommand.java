package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;

public class ClearCommand extends Command {
    private Runnable onClear;

    public ClearCommand(HashMap<String, Worker> collection, Runnable onClear) {
        super("clear", "очистить коллекцию", collection);
        this.onClear = onClear;
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        collection.clear();
        onClear.run(); // сброс nextId
        System.out.println("🧹 Коллекция очищена");
    }
}