package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;

/**
 * Команда для завершения программы.
 * Выполняет действие выхода и останавливает главный цикл.
 */

public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "выйти из программы");
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return new CommandResponse(ResponseType.SUCCESS, "До свидания!");
    }
}