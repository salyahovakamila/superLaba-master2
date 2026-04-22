package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;

public class AverageOfSalaryCommand extends Command {

    public AverageOfSalaryCommand(HashMap<String, Worker> collection) {
        super("average_of_salary", "средняя зарплата", collection);
    }

    @Override
    public void execute(String args, BaseInputHandler inputHandler) {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }

        double sum = 0;
        for (Worker w : collection.values()) {
            sum += w.getSalary();
        }

        System.out.printf("💰 Средняя зарплата: %.2f%n", sum / collection.size());
    }
}