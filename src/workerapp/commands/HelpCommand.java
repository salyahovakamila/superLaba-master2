package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends Command {
    private Map<String, Command> commands;

    public HelpCommand(HashMap<String, Worker> collection, Map<String, Command> commands) {
        super("help", "вывести справку по командам", collection);
        this.commands = commands;
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        System.out.println("\n=== ДОСТУПНЫЕ КОМАНДЫ ===");
        for (Command cmd : commands.values()) {
            System.out.printf("  %-25s - %s%n", cmd.getName(), cmd.getDescription());
        }
    }
}