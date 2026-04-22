package server.commands;

import common.CommandRequest;
import common.CommandResponse;
import common.ResponseType;
import models.Worker;

import java.util.HashMap;

/**
 * Команда для вычисления средней зарплаты всех работников.
 * Суммирует зарплаты всех работников и делит на их количество.
 */

public class AverageOfSalaryCommand extends Command {
    private HashMap<String, Worker> collection;

    public AverageOfSalaryCommand(HashMap<String, Worker> collection) {
        super("average_of_salary", "средняя зарплата");
        this.collection = collection;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (collection.isEmpty()) {
            return new CommandResponse(ResponseType.ERROR, "Коллекция пуста");
        }

        // Используем Stream API
        double sum = collection.values().stream()
                .mapToDouble(Worker::getSalary)
                .sum();

        double average = sum / collection.size();

        return new CommandResponse(ResponseType.AVERAGE,
                String.format("Средняя зарплата: %.2f", average), average);
    }
}