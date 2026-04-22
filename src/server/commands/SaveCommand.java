package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;
import server.io.FileManager;

import java.util.HashMap;
/**
 * Команда для сохранения коллекции в файл.
 * Использует FileManager для сериализации коллекции в JSON.
 */

public class SaveCommand extends Command {
    private CommandManager commandManager;

    public SaveCommand(HashMap<String, Worker> collection, CommandManager commandManager) {
        super("save", "сохранить коллекцию в файл (только на сервере)");
        this.commandManager = commandManager;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        commandManager.saveCollection();  // ИСПРАВЛЕНО
        return new CommandResponse(ResponseType.SUCCESS, "Коллекция сохранена");
    }
}