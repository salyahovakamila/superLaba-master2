package client.console;

import client.validation.InputValidator;
import workerapp.commands.*;
import workerapp.models.Coordinates;
import workerapp.models.Organization;
import workerapp.models.Worker;
import workerapp.models.enums.OrganizationType;
import workerapp.models.enums.Position;
import workerapp.models.enums.Status;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class CommandParser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private final ConsoleReader reader;

    public CommandParser() {
        this.reader = new ConsoleReader();
    }

    public Command parse(String input) {
        String[] parts = input.trim().split("\\s+", 2);
        String cmdName = parts[0].toUpperCase();
        String args = parts.length > 1 ? parts[1] : "";
        try {
            return switch (cmdName) {
                case "INFO" -> new InfoCommand();
                case "SHOW" -> new ShowCommand();
                case "ADD" -> handleAdd(args);
                case "UPDATE" -> handleUpdate(args);
                case "REMOVE" -> handleRemove(args);
                case "CLEAR" -> new ClearCommand();
                case "EXIT" -> new ExitCommand();
                case "HELP" -> new HelpCommand();
                case "FILTER_BY_POSITION" -> handleFilter(args);
                case "PRINT_FIELD_LESS_THAN_SALARY" -> handleLessThan(args);
                case "SAVE" -> {
                    System.out.println("Команда 'save' доступна только на сервере!");
                    yield null;
                }
                default -> {
                    System.out.println("Неизвестная команда: " + cmdName);
                    System.out.println("Введите 'help' для списка команд.");
                    yield null;
                }
            };
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            return null;
        }
    }

    private Command handleAdd(String args) {
        System.out.println("Создание нового работника:");
        String name = reader.readNonEmpty("Введите имя: ");
        double x = readDouble("Введите X (> -764): ", -764.0, null);
        Float y = readFloat("Введите Y (Enter для пропуска): ", null, null, true);
        Coordinates coords = new Coordinates(x, y);
        float salary = readFloat("Введите зарплату (> 0): ", 0.01f, null, false);
        LocalDate endDate = readDate("Введите дату окончания (ГГГГ-ММ-ДД, Enter для пропуска): ", true);
        Position position = readEnum("Введите должность (MANAGER, DEVELOPER, etc.): ", Position.class, true);
        Status status = readEnum("Введите статус (ACTIVE, FIRED, etc.): ", Status.class, false);
        Organization org = readOrganization();
        Worker worker = new Worker(null, name, coords, salary, endDate, position, status, org);
        InputValidator.validateWorker(worker);
        return new AddCommand(worker);
    }

    private Command handleUpdate(String args) {
        Integer id = parseInt(args);
        if (id == null) {
            throw new IllegalArgumentException("Укажите ID работника для обновления");
        }
        System.out.println("Обновление работника с ID=" + id);
        String name = reader.readNonEmpty("Введите имя: ");
        double x = readDouble("Введите X (> -764): ", -764.0, null);
        Float y = readFloat("Введите Y (Enter для пропуска): ", null, null, true);
        Coordinates coords = new Coordinates(x, y);
        float salary = readFloat("Введите зарплату (> 0): ", 0.01f, null, false);
        LocalDate endDate = readDate("Введите дату окончания (ГГГГ-ММ-ДД, Enter для пропуска): ", true);
        Position position = readEnum("Введите должность: ", Position.class, true);
        Status status = readEnum("Введите статус: ", Status.class, false);
        Organization org = readOrganization();
        Worker worker = new Worker(id, name, coords, salary, endDate, position, status, org);
        InputValidator.validateWorker(worker);
        return new UpdateCommand(worker);
    }

    private Command handleRemove(String args) {
        Integer id = parseInt(args);
        if (id == null) {
            throw new IllegalArgumentException("Укажите ID работника для удаления");
        }
        return new RemoveCommand(id);
    }

    private Command handleFilter(String args) {
        if (args.trim().isEmpty()) {
            throw new IllegalArgumentException("Укажите должность для фильтрации");
        }
        Position position = Position.valueOf(args.trim().toUpperCase());
        return new FilterByPositionCommand(position);
    }

    private Command handleLessThan(String args) {
        Float salary = readFloat("Введите зарплату для сравнения: ", null, null, false);
        return new PrintFieldLessThanSalaryCommand(salary);
    }

    private double readDouble(String prompt, Double min, Double max) {
        while (true) {
            String input = reader.readLine(prompt);
            try {
                double value = Double.parseDouble(input);
                if (min != null && value <= min) {
                    System.out.println("Значение должно быть > " + min);
                    continue;
                }
                if (max != null && value >= max) {
                    System.out.println("Значение должно быть < " + max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    private Float readFloat(String prompt, Float min, Float max, boolean nullable) {
        while (true) {
            String input = reader.readLine(prompt);
            if (input.trim().isEmpty()) {
                if (nullable) return null;
                System.out.println("Ввод обязателен!");
                continue;
            }

            try {
                float value = Float.parseFloat(input);
                if (min != null && value <= min) {
                    System.out.println("Значение должно быть > " + min);
                    continue;
                }
                if (max != null && value >= max) {
                    System.out.println("Значение должно быть < " + max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    private LocalDate readDate(String prompt, boolean nullable) {
        while (true) {
            String input = reader.readLine(prompt);

            if (input.trim().isEmpty()) {
                if (nullable) return null;
                System.out.println("Ввод обязателен!");
                continue;
            }

            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Формат: ГГГГ-ММ-ДД (например, 2024-01-15)");
            }
        }
    }

    private <T extends Enum<T>> T readEnum(String prompt, Class<T> enumClass, boolean nullable) {
        while (true) {
            String input = reader.readLine(prompt);

            if (input.trim().isEmpty()) {
                if (nullable) return null;
                System.out.println("Ввод обязателен!");
                continue;
            }

            try {
                return Enum.valueOf(enumClass, input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Доступные значения: " + java.util.Arrays.toString(enumClass.getEnumConstants()));
            }
        }
    }

    private Organization readOrganization() {
        System.out.println("Данные организации (Enter для пропуска):");

        // Проверяем, хочет ли пользователь вводить организацию
        String answer = reader.readLine("Добавить организацию? (y/n): ");
        if (answer == null || !answer.trim().equalsIgnoreCase("y")) {
            return null;
        }

        Integer turnover = null;
        while (turnover == null) {
            String input = reader.readLine("Введите годовой оборот (> 0): ");
            try {
                turnover = Integer.parseInt(input);
                if (turnover <= 0) {
                    System.out.println("Оборот должен быть > 0");
                    turnover = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
            OrganizationType type = readEnum("Введите тип организации: ", OrganizationType.class, true);

            return new Organization(turnover, employees, type);
        }
    }

    private Integer parseInt(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}