package workerapp.input;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Обработчик ввода с консоли (клавиатура)
 */
public class ConsoleInputHandler extends BaseInputHandler {
    private Scanner scanner;

    public ConsoleInputHandler() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public String readString(String prompt, boolean nullable) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty() && nullable) {
                return null;
            }
            if (input.isEmpty() && !nullable) {
                System.out.println("❌ Ошибка: значение не может быть пустым");
                continue;
            }
            return input;
        }
    }

    @Override
    public Integer readInt(String prompt, Integer min, Integer max, boolean nullable) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if (input.isEmpty() && nullable) {
                    return null;
                }

                int value = Integer.parseInt(input);

                if (min != null && value < min) {
                    System.out.println("❌ Ошибка: должно быть >= " + min);
                    continue;
                }
                if (max != null && value > max) {
                    System.out.println("❌ Ошибка: должно быть <= " + max);
                    continue;
                }
                return value;

            } catch (NumberFormatException e) {
                System.out.println("❌ Ошибка: введите целое число");
            }
        }
    }

    @Override
    public Float readFloat(String prompt, Float min, Float max, boolean nullable) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if (input.isEmpty() && nullable) {
                    return null;
                }

                float value = Float.parseFloat(input);

                if (min != null && value < min) {
                    System.out.println("❌ Ошибка: должно быть > " + min);
                    continue;
                }
                if (max != null && value > max) {
                    System.out.println("❌ Ошибка: должно быть < " + max);
                    continue;
                }
                return value;

            } catch (NumberFormatException e) {
                System.out.println("❌ Ошибка: введите число");
            }
        }
    }

    @Override
    public Double readDouble(String prompt, Double min, Double max, boolean nullable) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if (input.isEmpty() && nullable) {
                    return null;
                }

                double value = Double.parseDouble(input);

                if (min != null && value <= min) {
                    System.out.println("❌ Ошибка: должно быть > " + min);
                    continue;
                }
                if (max != null && value > max) {
                    System.out.println("❌ Ошибка: должно быть < " + max);
                    continue;
                }
                return value;

            } catch (NumberFormatException e) {
                System.out.println("❌ Ошибка: введите число");
            }
        }
    }

    @Override
    public Long readLong(String prompt, Long min, Long max, boolean nullable) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if (input.isEmpty() && nullable) {
                    return null;
                }

                long value = Long.parseLong(input);

                if (min != null && value < min) {
                    System.out.println("❌ Ошибка: должно быть > " + min);
                    continue;
                }
                if (max != null && value > max) {
                    System.out.println("❌ Ошибка: должно быть < " + max);
                    continue;
                }
                return value;

            } catch (NumberFormatException e) {
                System.out.println("❌ Ошибка: введите целое число");
            }
        }
    }

    @Override
    public LocalDate readDate(String prompt, boolean nullable) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if (input.isEmpty() && nullable) {
                    return null;
                }

                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {
                System.out.println("❌ Ошибка: введите дату в формате ГГГГ-ММ-ДД");
            }
        }
    }

    @Override
    public <T extends Enum<T>> T readEnum(String prompt, Class<T> enumClass, boolean nullable) {
        T[] constants = enumClass.getEnumConstants();
        System.out.print("📋 Доступные значения: ");
        for (int i = 0; i < constants.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(constants[i]);
        }
        System.out.println();

        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim().toUpperCase();

                if (input.isEmpty() && nullable) {
                    return null;
                }

                return Enum.valueOf(enumClass, input);

            } catch (IllegalArgumentException e) {
                System.out.println("❌ Ошибка: неверное значение");
            }
        }
    }

    @Override
    public String readYesNo(String prompt) {
        System.out.print(prompt + " (y/n): ");
        return scanner.nextLine().trim().toLowerCase();
    }
}