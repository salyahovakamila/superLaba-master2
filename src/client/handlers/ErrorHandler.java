package client.handlers;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.UnresolvedAddressException;

/**
 * Обработчик ошибок.
 * Зачем: корректно обрабатывает недоступность сервера без падения программы.
 */
public class ErrorHandler {

    /**
     * Обрабатывает исключение.
     * @param e исключение для обработки
     */
    public static void handle(Exception e) {
        if (e instanceof ConnectException) {
            // Сервер не отвечает на подключение
            System.err.println("Сервер недоступен. Проверьте, запущен ли он.");
            System.err.println("Адрес: localhost:2424");
        }
        else if (e instanceof SocketTimeoutException) {
            // Сервер не ответил вовремя
            System.err.println("Превышено время ожидания ответа.");
            System.err.println("Попробуйте повторить команду.");
        }
        else if (e instanceof ClosedChannelException) {
            // Канал закрыт
            System.err.println("Соединение разорвано.");
            System.err.println("Перезапустите клиента для подключения.");
        }
        else if (e instanceof UnresolvedAddressException) {
            // Неверный адрес
            System.err.println("Неверный адрес сервера.");
            System.err.println("Проверьте настройки хоста и порта.");
        }
        else if (e instanceof IOException) {
            // Общая ошибка I/O
            System.err.println("Ошибка сети: " + e.getMessage());
        }
        else {
            // Любая другая ошибка
            System.err.println("Ошибка: " + e.getMessage());
        }

    }
}