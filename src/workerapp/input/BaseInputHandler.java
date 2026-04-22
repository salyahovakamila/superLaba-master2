package workerapp.input;

import workerapp.models.Coordinates;
import workerapp.models.Organization;
import workerapp.models.Worker;
import workerapp.models.enums.OrganizationType;
import workerapp.models.enums.Position;
import workerapp.models.enums.Status;

import java.time.LocalDate;

/**
 * Базовый класс для обработчиков ввода
 */
public abstract class BaseInputHandler {

    public abstract String readLine();

    public abstract String readString(String prompt, boolean nullable);

    public abstract Integer readInt(String prompt, Integer min, Integer max, boolean nullable);

    public abstract Float readFloat(String prompt, Float min, Float max, boolean nullable);

    public abstract Double readDouble(String prompt, Double min, Double max, boolean nullable);

    public abstract Long readLong(String prompt, Long min, Long max, boolean nullable);

    public abstract LocalDate readDate(String prompt, boolean nullable);

    public abstract <T extends Enum<T>> T readEnum(String prompt, Class<T> enumClass, boolean nullable);

    public abstract String readYesNo(String prompt);

    // Общий метод для чтения работника
    public Worker readWorker() {
        String name = readString("Имя: ", false);

        System.out.println("--- Координаты ---");
        double x = readDouble("x (> -764): ", -764.0, null, false);
        Float y = readFloat("y: ", null, null, false);
        Coordinates coords = new Coordinates(x, y);

        float salary = readFloat("Зарплата (> 0): ", 0.01f, null, false);
        LocalDate endDate = readDate("Дата окончания (Enter - null): ", true);
        Position position = readEnum("Должность (Enter - null): ", Position.class, true);
        Status status = readEnum("Статус: ", Status.class, false);

        Organization org = null;
        String addOrg = readYesNo("Добавить организацию?");

        if (addOrg.equals("y") || addOrg.equals("yes") || addOrg.equals("да")) {
            System.out.println("--- Организация ---");
            Integer turnover = readInt("Годовой оборот (> 0): ", 1, null, false);
            long employees = readLong("Количество сотрудников (> 0): ", 1L, null, false);
            OrganizationType type = readEnum("Тип организации (Enter - null): ", OrganizationType.class, true);
            org = new Organization(turnover, employees, type);
        }

        return new Worker(null, name, coords, salary, endDate, position, status, org);
    }
}