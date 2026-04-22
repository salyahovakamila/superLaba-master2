package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;
import models.enums.Status;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для фильтрации работников по статусу.
 * Выводит всех работников, у которых статус больше заданного.
 * Сравнение статусов происходит по порядку объявления в enum.
 */

public class FilterGreaterThanStatusCommand extends Command {
    private HashMap<String, Worker> collection;

    public FilterGreaterThanStatusCommand(HashMap<String, Worker> collection) {
        super("filter_greater_than_status", "вывести со статусом больше заданного");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Status status = request.getStatus();

        if (status == null) {
            return new CommandResponse(ResponseType.ERROR,
                    "Использование: filter_greater_than_status СТАТУС\nСтатусы: FIRED, HIRED, RECOMMENDED_FOR_PROMOTION, REGULAR");
        }

        // Используем Stream API для фильтрации
        List<Worker> filtered = collection.values().stream()
                .filter(w -> w.getStatus().compareTo(status) > 0)
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return new CommandResponse(ResponseType.INFO, "Нет работников со статусом > " + status);
        }

        StringBuilder result = new StringBuilder("\n=== Статус > " + status + " ===\n");
        for (Worker w : filtered) {
            result.append(w).append("\n");
        }

        return new CommandResponse(ResponseType.COLLECTION, result.toString(), filtered);
    }
}