package common;

import models.Worker;
import models.enums.Status;
import java.io.Serializable;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final CommandType type;
    private final String key;
    private final Integer id;
    private final Worker worker;
    private final Status status;

    private CommandRequest(Builder builder) {
        this.type = builder.type;
        this.key = builder.key;
        this.id = builder.id;
        this.worker = builder.worker;
        this.status = builder.status;
    }

    public CommandType getType() { return type; }
    public String getKey() { return key; }
    public Integer getId() { return id; }
    public Worker getWorker() { return worker; }
    public Status getStatus() { return status; }

    public static class Builder {
        private final CommandType type;
        private String key;
        private Integer id;
        private Worker worker;
        private Status status;

        public Builder(CommandType type) {
            this.type = type;
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withWorker(Worker worker) {
            this.worker = worker;
            return this;
        }

        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public CommandRequest build() {
            return new CommandRequest(this);
        }
    }
}