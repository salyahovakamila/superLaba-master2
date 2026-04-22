package workerapp.responses;

import common.models.Worker;

import java.io.Serializable;
import java.util.List;

/**
 * Ответ от сервера на команду клиента.
 */
public class CommandResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private ResponseType type;        // Тип ответа
    private String message;           // Сообщение
    private List<Worker> data;        // Данные (если возвращается коллекция)

    /**
     * Конструктор для ответа с сообщением.
     */
    public CommandResponse(ResponseType type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Конструктор для ответа с данными (коллекцией).
     */
    public CommandResponse(ResponseType type, List<Worker> data) {
        this.type = type;
        this.data = data;
        this.message = null;
    }

    /**
     * Конструктор для ответа с сообщением и данными.
     */
    public CommandResponse(ResponseType type, String message, List<Worker> data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    // Геттеры
    public ResponseType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public List<Worker> getData() {
        return data;
    }
}