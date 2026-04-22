package server.io;

import models.*;
import models.enums.*;

import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class JsonHelper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String workerToJson(Worker w) {
        StringBuilder json = new StringBuilder();
        json.append("{");

        json.append("\"id\":").append(w.getId()).append(",");

        json.append("\"name\":\"").append(escapeJson(w.getName())).append("\",");

        json.append("\"coordinates\":").append(coordinatesToJson(w.getCoordinates())).append(",");

        json.append("\"creationDate\":\"").append(w.getCreationDate().format(DATE_FORMATTER)).append("\",");

        json.append("\"salary\":").append(w.getSalary()).append(",");

        json.append("\"endDate\":");
        if (w.getEndDate() != null) {
            json.append("\"").append(w.getEndDate().format(LOCAL_DATE_FORMATTER)).append("\"");
        } else {
            json.append("null");
        }
        json.append(",");

        json.append("\"position\":");
        if (w.getPosition() != null) {
            json.append("\"").append(w.getPosition()).append("\"");
        } else {
            json.append("null");
        }
        json.append(",");

        json.append("\"status\":\"").append(w.getStatus()).append("\",");

        json.append("\"organization\":");
        if (w.getOrganization() != null) {
            json.append(organizationToJson(w.getOrganization()));
        } else {
            json.append("null");
        }

        json.append("}");
        return json.toString();
    }

    private static String coordinatesToJson(Coordinates c) {
        // ИСПРАВЛЕНО: используем точку вместо запятой
        return String.format(Locale.US, "{\"x\":%f,\"y\":%f}", c.getX(), c.getY());
    }

    private static String organizationToJson(Organization o) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"annualTurnover\":").append(o.getAnnualTurnover()).append(",");
        json.append("\"employeesCount\":").append(o.getEmployeesCount()).append(",");
        json.append("\"type\":");
        if (o.getType() != null) {
            json.append("\"").append(o.getType()).append("\"");
        } else {
            json.append("null");
        }
        json.append("}");
        return json.toString();
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // ДОБАВЛЕН: метод для нормализации чисел (замена запятой на точку)
    private static String normalizeNumber(String value) {
        if (value == null) return null;
        // Заменяем запятую на точку в числах
        return value.replace(',', '.');
    }

    public static Worker jsonToWorker(String json) {
        try {
            json = json.trim();
            Map<String, String> fields = parseJsonAdvanced(json);

            if (!fields.containsKey("id") || fields.get("id") == null || "null".equals(fields.get("id"))) {
                throw new IllegalArgumentException("Missing required field: id");
            }

            Integer id = Integer.parseInt(fields.get("id"));

            if (!fields.containsKey("name") || fields.get("name") == null) {
                throw new IllegalArgumentException("Missing required field: name");
            }
            String name = unescapeJson(fields.get("name"));

            if (!fields.containsKey("coordinates") || fields.get("coordinates") == null) {
                throw new IllegalArgumentException("Missing required field: coordinates");
            }
            Coordinates coordinates = jsonToCoordinatesAdvanced(fields.get("coordinates"));

            ZonedDateTime creationDate;
            if (fields.containsKey("creationDate") && fields.get("creationDate") != null && !"null".equals(fields.get("creationDate"))) {
                String dateStr = unescapeJson(fields.get("creationDate"));
                creationDate = ZonedDateTime.parse(dateStr, DATE_FORMATTER);
            } else {
                creationDate = ZonedDateTime.now();
            }

            if (!fields.containsKey("salary") || fields.get("salary") == null) {
                throw new IllegalArgumentException("Missing required field: salary");
            }
            // ИСПРАВЛЕНО: нормализуем число (замена запятой на точку)
            float salary = Float.parseFloat(normalizeNumber(fields.get("salary")));

            LocalDate endDate = null;
            if (fields.containsKey("endDate") && fields.get("endDate") != null && !"null".equals(fields.get("endDate"))) {
                endDate = LocalDate.parse(unescapeJson(fields.get("endDate")), LOCAL_DATE_FORMATTER);
            }

            Position position = null;
            if (fields.containsKey("position") && fields.get("position") != null && !"null".equals(fields.get("position"))) {
                String positionStr = unescapeJson(fields.get("position"));
                // ИСПРАВЛЕНО: убираем кавычки, если они есть
                positionStr = positionStr.replace("\"", "");
                position = Position.valueOf(positionStr);
            }

            Status status = null;
            if (fields.containsKey("status") && fields.get("status") != null && !"null".equals(fields.get("status"))) {
                String statusStr = unescapeJson(fields.get("status"));
                statusStr = statusStr.replace("\"", "");
                status = Status.valueOf(statusStr);
            } else {
                throw new IllegalArgumentException("Missing required field: status");
            }

            Organization organization = null;
            if (fields.containsKey("organization") && fields.get("organization") != null && !"null".equals(fields.get("organization"))) {
                organization = jsonToOrganizationAdvanced(fields.get("organization"));
            }

            return new Worker(id, name, coordinates, salary, endDate, position, status, organization);

        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга JSON: " + e.getMessage() + " | JSON: " + json, e);
        }
    }

    private static Map<String, String> parseJsonAdvanced(String json) {
        Map<String, String> result = new HashMap<>();

        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }

        int i = 0;
        int len = json.length();

        while (i < len) {
            while (i < len && Character.isWhitespace(json.charAt(i))) {
                i++;
            }
            if (i >= len) break;

            if (json.charAt(i) != '"') {
                i++;
                continue;
            }
            i++;

            StringBuilder keyBuilder = new StringBuilder();
            while (i < len && json.charAt(i) != '"') {
                if (json.charAt(i) == '\\' && i + 1 < len) {
                    keyBuilder.append(json.charAt(i + 1));
                    i += 2;
                } else {
                    keyBuilder.append(json.charAt(i));
                    i++;
                }
            }
            i++;
            String key = keyBuilder.toString();

            while (i < len && (Character.isWhitespace(json.charAt(i)) || json.charAt(i) == ':')) {
                i++;
            }
            if (i >= len) break;

            String value;
            if (json.charAt(i) == '{') {
                int braceCount = 1;
                int start = i;
                i++;
                while (i < len && braceCount > 0) {
                    char c = json.charAt(i);
                    if (c == '{') braceCount++;
                    if (c == '}') braceCount--;
                    if (c == '"') {
                        i++;
                        while (i < len && !(json.charAt(i) == '"' && json.charAt(i-1) != '\\')) {
                            i++;
                        }
                    }
                    i++;
                }
                value = json.substring(start, i);
            } else if (json.charAt(i) == '"') {
                i++;
                StringBuilder valueBuilder = new StringBuilder();
                while (i < len && !(json.charAt(i) == '"' && json.charAt(i-1) != '\\')) {
                    valueBuilder.append(json.charAt(i));
                    i++;
                }
                i++;
                value = "\"" + valueBuilder.toString() + "\"";
            } else {
                int start = i;
                while (i < len && json.charAt(i) != ',' && json.charAt(i) != '}') {
                    i++;
                }
                value = json.substring(start, i).trim();
            }

            result.put(key, value);

            while (i < len && (Character.isWhitespace(json.charAt(i)) || json.charAt(i) == ',')) {
                i++;
            }
        }

        return result;
    }

    private static Coordinates jsonToCoordinatesAdvanced(String json) {
        Map<String, String> fields = parseJsonAdvanced(json);

        if (!fields.containsKey("x") || fields.get("x") == null) {
            throw new IllegalArgumentException("Missing x coordinate");
        }
        // ИСПРАВЛЕНО: нормализуем число
        double x = Double.parseDouble(normalizeNumber(fields.get("x")));

        if (!fields.containsKey("y") || fields.get("y") == null) {
            throw new IllegalArgumentException("Missing y coordinate");
        }
        // ИСПРАВЛЕНО: нормализуем число
        float y = Float.parseFloat(normalizeNumber(fields.get("y")));

        return new Coordinates(x, y);
    }

    private static Organization jsonToOrganizationAdvanced(String json) {
        Map<String, String> fields = parseJsonAdvanced(json);

        if (!fields.containsKey("annualTurnover") || fields.get("annualTurnover") == null) {
            throw new IllegalArgumentException("Missing annualTurnover");
        }
        Integer annualTurnover = Integer.parseInt(fields.get("annualTurnover"));

        if (!fields.containsKey("employeesCount") || fields.get("employeesCount") == null) {
            throw new IllegalArgumentException("Missing employeesCount");
        }
        long employeesCount = Long.parseLong(fields.get("employeesCount"));

        OrganizationType type = null;
        if (fields.containsKey("type") && fields.get("type") != null && !"null".equals(fields.get("type"))) {
            String typeStr = fields.get("type");
            if (typeStr.startsWith("\"") && typeStr.endsWith("\"")) {
                typeStr = typeStr.substring(1, typeStr.length() - 1);
            }
            type = OrganizationType.valueOf(typeStr);
        }

        return new Organization(annualTurnover, employeesCount, type);
    }

    private static String unescapeJson(String s) {
        if (s == null) return null;
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
        }
        return s.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
}