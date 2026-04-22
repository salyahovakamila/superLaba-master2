package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.util.HashMap;

/**
 * Команда для полной очистки коллекции.
 * Удаляет всех работников и сбрасывает счетчик ID.
 */

public class ClearCommand extends Command {
    private HashMap<String, Worker> collection;
    private CommandManager commandManager;

    public ClearCommand(HashMap<String, Worker> collection, CommandManager commandManager) {
        super("clear", "очистить коллекцию");
        this.collection = collection;
        this.commandManager = commandManager;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        collection.clear();
        commandManager.setNextId(1);
        return new CommandResponse(ResponseType.SUCCESS, "Коллекция очищена");
    }
}