package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.time.LocalDate;
import java.util.*;

public class PrintFieldDescendingEndDateCommand extends Command {

    public PrintFieldDescendingEndDateCommand(HashMap<String, Worker> collection) {
        super("print_field_descending_end_date", "вывести даты увольнения по убыванию", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        List<LocalDate> dates = new ArrayList<>();

        for (Worker w : collection.values()) {
            if (w.getEndDate() != null) {
                dates.add(w.getEndDate());
            }
        }

        if (dates.isEmpty()) {
            System.out.println("Нет дат увольнения");
            return;
        }

        dates.sort(Collections.reverseOrder());

        System.out.println("\n=== ДАТЫ УВОЛЬНЕНИЯ (по убыванию) ===");
        for (LocalDate date : dates) {
            System.out.println(date);
        }
    }
}