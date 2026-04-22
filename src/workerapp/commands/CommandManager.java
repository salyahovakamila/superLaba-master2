package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private Map<String, Command> commands;
    private HashMap<String, Worker> collection;
    private int nextId;
    private String fileName;
    private BaseInputHandler defaultInputHandler; // Добавляем поле для хранения обработчика

    public CommandManager(HashMap<String, Worker> collection) {
        this.collection = collection;
        this.commands = new HashMap<>();
        this.nextId = calculateNextId();
    }

    public void setDefaultInputHandler(BaseInputHandler inputHandler) {
        this.defaultInputHandler = inputHandler;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private int calculateNextId() {
        int maxId = 0;
        for (Worker w : collection.values()) {
            if (w.getId() > maxId) {
                maxId = w.getId();
            }
        }
        return maxId + 1;
    }

    private int generateId() {
        return nextId++;
    }

    public void registerCommands(BaseInputHandler inputHandler, String fileName, Runnable onExit) {
        this.defaultInputHandler = inputHandler;

        // Информационные команды
        commands.put("help", new HelpCommand(collection, commands));
        commands.put("info", new InfoCommand(collection));
        commands.put("show", new ShowCommand(collection));

        // Команды изменения коллекции
        commands.put("insert", new InsertCommand(collection, this::generateId));
        commands.put("update", new UpdateCommand(collection));
        commands.put("remove_key", new RemoveKeyCommand(collection));
        commands.put("clear", new ClearCommand(collection, () -> nextId = 1));

        // Файловые команды
        SaveCommand saveCmd = new SaveCommand(collection, fileName);
        commands.put("save", saveCmd);

        commands.put("execute_script", new ExecuteScriptCommand(collection, this));

        // Выход
        commands.put("exit", new ExitCommand(collection, onExit));

        // Специальные команды
        commands.put("remove_lower", new RemoveLowerCommand(collection));
        commands.put("replace_if_lower", new ReplaceIfLowerCommand(collection));
        commands.put("remove_lower_key", new RemoveLowerKeyCommand(collection));
        commands.put("average_of_salary", new AverageOfSalaryCommand(collection));
        commands.put("filter_greater_than_status", new FilterGreaterThanStatusCommand(collection));
        commands.put("print_field_descending_end_date", new PrintFieldDescendingEndDateCommand(collection));
    }

    // Существующий метод (для обычного режима)
    public boolean executeCommand(String input) {
        return executeCommand(input, defaultInputHandler);
    }

    // НОВЫЙ метод (для режима скрипта)
    public boolean executeCommand(String input, BaseInputHandler inputHandler) {
        if (input == null || input.trim().isEmpty()) {
            return true;
        }

        String[] parts = input.trim().split(" ", 2);
        String cmdName = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        Command cmd = commands.get(cmdName);
        if (cmd == null) {
            System.out.println("❌ Неизвестная команда. Введите 'help'");
            return true;
        }

        try {
            // Передаем inputHandler в команду
            cmd.execute(args, inputHandler);
        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }

        return !cmdName.equals("exit");
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}