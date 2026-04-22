package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import common.CommandType;

import java.util.HashMap;
import java.util.Map;

/**
 * Команда для отображения справки по всем доступным командам.
 * Выводит список всех зарегистрированных команд с их описаниями.
 */

public class HelpCommand extends Command {
    private Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        super("help", "вывести справку по командам");
        this.commands = commands;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== ДОСТУПНЫЕ КОМАНДЫ ===\n");

        for (Command cmd : commands.values()) {
            // Пропускаем команды, доступные только на сервере
            if (cmd.getName().equals("save")) {
                continue;
            }
            sb.append("  ").append(cmd.getName()).append(" - ").append(cmd.getDescription()).append("\n");
        }

        sb.append("\n  exit - выйти из программы");

        return new CommandResponse(ResponseType.INFO, sb.toString());
    }
}