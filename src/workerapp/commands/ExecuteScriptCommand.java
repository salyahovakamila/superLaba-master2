package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import workerapp.utils.ScriptRunner;
import java.util.HashMap;

public class ExecuteScriptCommand extends Command {
    private CommandManager commandManager;

    public ExecuteScriptCommand(HashMap<String, Worker> collection, CommandManager commandManager) {
        super("execute_script", "выполнить скрипт из файла", collection);
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (args.isEmpty()) {
            System.out.println("❌ Использование: execute_script файл");
            return;
        }

        ScriptRunner.executeScript(args.trim(), commandManager);
    }
}