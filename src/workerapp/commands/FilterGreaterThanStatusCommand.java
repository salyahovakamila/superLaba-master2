package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import workerapp.models.enums.Status;
import java.util.HashMap;

public class FilterGreaterThanStatusCommand extends Command {

    public FilterGreaterThanStatusCommand(HashMap<String, Worker> collection) {
        super("filter_greater_than_status", "вывести со статусом больше заданного", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (args.isEmpty()) {
            System.out.println("❌ Использование: filter_greater_than_status СТАТУС");
            System.out.println("Статусы: FIRED, HIRED, RECOMMENDED_FOR_PROMOTION, REGULAR");
            return;
        }

        try {
            Status status = Status.valueOf(args.trim().toUpperCase());

            System.out.println("\n=== Статус > " + status + " ===");
            boolean found = false;

            for (Worker w : collection.values()) {
                if (w.getStatus().compareTo(status) > 0) {
                    System.out.println(w);
                    found = true;
                }
            }

            if (!found) System.out.println("Нет таких");

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Неверный статус");
        }
    }
}