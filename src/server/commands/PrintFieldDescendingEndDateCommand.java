package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода всех дат окончания работы в порядке убывания.
 * Собирает все не-null даты окончания и сортирует их по убыванию.
 */

public class PrintFieldDescendingEndDateCommand extends Command {
    private HashMap<String, Worker> collection;

    public PrintFieldDescendingEndDateCommand(HashMap<String, Worker> collection) {
        super("print_field_descending_end_date", "вывести даты увольнения по убыванию");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        // Используем Stream API для сбора и сортировки дат
        List<String> dates = collection.values().stream()
                .map(Worker::getEndDate)
                .filter(date -> date != null)
                .sorted((d1, d2) -> d2.compareTo(d1)) // по убыванию
                .map(LocalDate::toString)
                .collect(Collectors.toList());

        if (dates.isEmpty()) {
            return new CommandResponse(ResponseType.INFO, "Нет дат увольнения");
        }

        StringBuilder result = new StringBuilder("\n=== ДАТЫ УВОЛЬНЕНИЯ (по убыванию) ===\n");
        for (String date : dates) {
            result.append(date).append("\n");
        }

        return new CommandResponse(ResponseType.DATES, result.toString(), dates, true);
    }
}