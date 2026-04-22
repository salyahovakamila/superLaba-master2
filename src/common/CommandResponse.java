package common;

import models.Worker;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class CommandResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ResponseType type;
    private final String message;
    private final HashMap<String, Worker> collection;
    private final List<Worker> workers;
    private final Double averageSalary;
    private final List<String> dates;

    public CommandResponse(ResponseType type, String message) {
        this(type, message, null, null, null, null);
    }

    public CommandResponse(ResponseType type, String message, HashMap<String, Worker> collection) {
        this(type, message, collection, null, null, null);
    }

    public CommandResponse(ResponseType type, String message, List<Worker> workers) {
        this(type, message, null, workers, null, null);
    }

    public CommandResponse(ResponseType type, String message, Double averageSalary) {
        this(type, message, null, null, averageSalary, null);
    }

    public CommandResponse(ResponseType type, String message, List<String> dates, boolean isDates) {
        this(type, message, null, null, null, dates);
    }

    private CommandResponse(ResponseType type, String message, HashMap<String, Worker> collection,
                            List<Worker> workers, Double averageSalary, List<String> dates) {
        this.type = type;
        this.message = message;
        this.collection = collection;
        this.workers = workers;
        this.averageSalary = averageSalary;
        this.dates = dates;
    }

    public ResponseType getType() { return type; }
    public String getMessage() { return message; }
    public HashMap<String, Worker> getCollection() { return collection; }
    public List<Worker> getWorkers() { return workers; }
    public Double getAverageSalary() { return averageSalary; }
    public List<String> getDates() { return dates; }
}