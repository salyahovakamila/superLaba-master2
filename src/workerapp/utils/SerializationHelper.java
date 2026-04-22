package workerapp.utils;

import java.io.*;

/**
 * Помощник для сериализации объектов.
 * Зачем: единый метод для клиента и сервера, чтобы не дублировать код.
 */
public class SerializationHelper {

    /**
     * Сериализует объект в байты.
     * @param obj объект для сериализации
     * @return массив байтов
     * @throws IOException если ошибка
     */
    public static byte[] serialize(Object obj) throws IOException {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();

             ObjectOutputStream oos = new ObjectOutputStream(baos)) {


            oos.writeObject(obj);


            return baos.toByteArray();
        }
    }

    /**
     * Десериализует байты в объект.
     * @param data байты для десериализации
     * @param clazz класс ожидаемого объекта
     * @return объект типа T
     * @throws IOException если ошибка
     * @throws ClassNotFoundException если класс не найден
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data, Class<T> clazz)
            throws IOException, ClassNotFoundException {


        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);

             ObjectInputStream ois = new ObjectInputStream(bais)) {

            
            return (T) ois.readObject();
        }
    }
}
