package workerapp.input;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Обработчик ввода из файла скрипта
 */
public class ScriptInputHandler extends BaseInputHandler {
    private Scanner scanner;
    private int lineNumber = 0;

    public ScriptInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    private String readNextLine() {
        if (!scanner.hasNextLine()) {
            throw new RuntimeException("❌ Ошибка: неожиданный конец файла скрипта");
        }
        lineNumber++;
        return scanner.nextLine().trim();
    }

    @Override
    public String readLine() {
        return readNextLine();
    }

    @Override
    public String readString(String prompt, boolean nullable) {
        String input = readNextLine();
        System.out.println(prompt + " " + input);

        if (input.isEmpty() && !nullable) {
            throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): значение не может быть пустым");
        }
        if (input.isEmpty() && nullable) {
            return null;
        }
        return input;
    }

    @Override
    public Integer readInt(String prompt, Integer min, Integer max, boolean nullable) {
        String input = readNextLine();
        System.out.println(prompt + " " + input);

        if (input.isEmpty() && nullable) {
            return null;
        }

        try {
            int value = Integer.parseInt(input);

            if (min != null && value < min) {
                throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): должно быть >= " + min);
            }
            if (max != null && value > max) {
                throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): должно быть <= " + max);
            }
            return value;

        } catch (NumberFormatException e) {
            throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): ожидалось целое число, получено: " + input);
        }
    }

    @Override
    public Float readFloat(String prompt, Float min, Float max, boolean nullable) {
        String input = readNextLine();
        System.out.println(prompt + " " + input);

        if (input.isEmpty() && nullable) {
            return null;
        }

        try {
            float value = Float.parseFloat(input);

            if (min != null && value < min) {
                throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): должно быть > " + min);
            }
            if (max != null && value > max) {
                throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): должно быть < " + max);
            }
            return value;

        } catch (NumberFormatException e) {
            throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): ожидалось число, получено: " + input);
        }
    }

    @Override
    public Double readDouble(String prompt, Double min, Double max, boolean nullable) {
        String input = readNextLine();
        System.out.println(prompt + " " + input);

        if (input.isEmpty() && nullable) {
            return null;
        }

        try {
            double value = Double.parseDouble(input);

            if (min != null && value <= min) {
                throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): должно быть > " + min);
            }
            if (max != null && value > max) {
                throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): должно быть < " + max);
            }
            return value;

        } catch (NumberFormatException e) {
            throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): ожидалось число, получено: " + input);
        }
    }

    @Override
    public Long readLong(String prompt, Long min, Long max, boolean nullable) {
        String input = readNextLine();
        System.out.println(prompt + " " + input);

        if (input.isEmpty() && nullable) {
            return null;
        }

        try {
            long value = Long.parseLong(input);

            if (min != null && value < min) {
                throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): должно быть > " + min);
            }
            if (max != null && value > max) {
                throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): должно быть < " + max);
            }
            return value;

        } catch (NumberFormatException e) {
            throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): ожидалось целое число, получено: " + input);
        }
    }

    @Override
    public LocalDate readDate(String prompt, boolean nullable) {
        String input = readNextLine();
        System.out.println(prompt + " " + input);

        if (input.isEmpty() && nullable) {
            return null;
        }

        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): ожидалась дата ГГГГ-ММ-ДД, получено: " + input);
        }
    }

    @Override
    public <T extends Enum<T>> T readEnum(String prompt, Class<T> enumClass, boolean nullable) {
        String input = readNextLine().toUpperCase();
        System.out.println(prompt + " " + input);

        if (input.isEmpty() && nullable) {
            return null;
        }

        try {
            return Enum.valueOf(enumClass, input);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("❌ Ошибка в скрипте (строка " + lineNumber + "): неверное значение. Ожидалось одно из: " +
                    java.util.Arrays.toString(enumClass.getEnumConstants()));
        }
    }

    @Override
    public String readYesNo(String prompt) {
        String input = readNextLine().toLowerCase();
        System.out.println(prompt + " (y/n): " + input);
        return input;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}