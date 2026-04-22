package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import models.Worker;

import java.util.HashMap;

/**
 * Абстрактный базовый класс для всех команд.
 * Определяет общую структуру команд и метод их выполнения.
 */

 public abstract class Command {
    protected String name;
    protected String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public abstract CommandResponse execute(CommandRequest request);
}