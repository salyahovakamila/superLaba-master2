package workerapp.responses;

/**
 * Типы ответов от сервера.
 */
public enum ResponseType {
    SUCCESS,      // Команда выполнена успешно
    ERROR,        // Произошла ошибка
    INFO,         // Информационное сообщение
    COLLECTION    // Возврат коллекции объектов
}