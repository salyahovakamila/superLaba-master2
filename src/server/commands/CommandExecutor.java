package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import common.CommandType;
import models.Worker;

import java.util.HashMap;

public class CommandExecutor {
    private HashMap<String, Command> commands;
    private CommandManager commandManager;
    private String fileName;  // ДОБАВЛЕНО поле для имени файла

    public CommandExecutor(HashMap<String, Worker> collection, String fileName) {  // ИЗМЕНЕНО: добавлен fileName
        this.commandManager = new CommandManager(collection);
        this.commandManager.setFileName(fileName);  // Устанавливаем fileName в commandManager
        this.fileName = fileName;  // Сохраняем fileName
        this.commands = new HashMap<>();
        registerCommands();
    }

    private void registerCommands() {
        commands.put("help", new HelpCommand(commands));
        commands.put("info", new InfoCommand(commandManager.getCollection()));
        commands.put("show", new ShowCommand(commandManager.getCollection()));
        commands.put("insert", new InsertCommand(commandManager.getCollection(), commandManager));
        commands.put("update", new UpdateCommand(commandManager.getCollection()));
        commands.put("remove_key", new RemoveKeyCommand(commandManager.getCollection()));
        commands.put("clear", new ClearCommand(commandManager.getCollection(), commandManager));
        commands.put("remove_lower", new RemoveLowerCommand(commandManager.getCollection()));
        commands.put("replace_if_lower", new ReplaceIfLowerCommand(commandManager.getCollection()));
        commands.put("remove_lower_key", new RemoveLowerKeyCommand(commandManager.getCollection()));
        commands.put("average_of_salary", new AverageOfSalaryCommand(commandManager.getCollection()));
        commands.put("filter_greater_than_status", new FilterGreaterThanStatusCommand(commandManager.getCollection()));
        commands.put("print_field_descending_end_date", new PrintFieldDescendingEndDateCommand(commandManager.getCollection()));
        commands.put("save", new SaveCommand(commandManager.getCollection(), commandManager));
        commands.put("execute_script", new ExecuteScriptCommand());
        commands.put("exit", new ExitCommand());
    }

    public CommandResponse execute(CommandRequest request) {
        String cmdName = request.getType().name().toLowerCase();
        Command command = commands.get(cmdName);

        if (command == null) {
            return new CommandResponse(ResponseType.ERROR, "Неизвестная команда");
        }

        try {
            return command.execute(request);
        } catch (Exception e) {
            return new CommandResponse(ResponseType.ERROR, "Ошибка: " + e.getMessage());
        }
    }

    public void saveCollection() {
        commandManager.saveCollection();
    }


}