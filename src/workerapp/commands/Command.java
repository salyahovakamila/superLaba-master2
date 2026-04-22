package workerapp.commands;

import workerapp.input.BaseInputHandler;
import workerapp.models.Worker;
import java.util.HashMap;

public abstract class Command {
    protected String name;
    protected String description;
    protected HashMap<String, Worker> collection;

    public Command(String name, String description, HashMap<String, Worker> collection) {
        this.name = name;
        this.description = description;
        this.collection = collection;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public abstract void execute(String args, BaseInputHandler inputHandler);
}