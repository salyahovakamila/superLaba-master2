package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;

public class ExecuteScriptCommand extends Command {

    public ExecuteScriptCommand() {
        super("execute_script", "выполнить скрипт из файла");
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return new CommandResponse(ResponseType.ERROR,
                "execute_script выполняется на клиенте, а не на сервере");
    }
}