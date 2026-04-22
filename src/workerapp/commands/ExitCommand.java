package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;

public class ExitCommand extends Command {
    private Runnable onExit;

    public ExitCommand(HashMap<String, Worker> collection, Runnable onExit) {
        super("exit", "выйти из программы", collection);
        this.onExit = onExit;
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        System.out.println("👋 До свидания!");
        onExit.run();
    }
}