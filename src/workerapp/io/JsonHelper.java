package workerapp.io;


import workerapp.models.Coordinates;
import workerapp.models.Organization;
import workerapp.models.Worker;
import workerapp.models.enums.OrganizationType;
import workerapp.models.enums.Position;
import workerapp.models.enums.Status;

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

        // position (может быть null)
        json.append("\"position\":");
        if (w.getPosition() != null) {
            json.append("\"").append(w.getPosition()).append("\"");
        } else {
            json.append("null");
        }
        json.append(",");

        // status
        json.append("\"status\":\"").append(w.getStatus()).append("\",");

        // organization (может быть null)
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
        return String.format("{\"x\":%f,\"y\":%f}", c.getX(), c.getY());
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

    // Экранирование спецсимволов в JSON
    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    

    // В JsonHelper.java, метод jsonToWorker
    public static Worker jsonToWorker(String json) {
        try {
            json = json.trim();
            if (json.startsWith("{")) json = json.substring(1, json.length() - 1);
            
            Map<String, String> fields = parseJsonFields(json);
            
            Integer id = null;
            if (fields.containsKey("id") && !"null".equals(fields.get("id"))) {
                id = Integer.parseInt(fields.get("id"));
            }

            String name = unescapeJson(fields.get("name"));
            
            Coordinates coordinates = jsonToCoordinates(fields.get("coordinates"));


            ZonedDateTime creationDate = ZonedDateTime.now(); // дефолт для новых
            if (fields.containsKey("creationDate") && !"null".equals(fields.get("creationDate"))) {
                String dateStr = unescapeJson(fields.get("creationDate"));
                creationDate = ZonedDateTime.parse(dateStr, DATE_FORMATTER);
            }

            
            float salary = Float.parseFloat(fields.get("salary"));

            LocalDate endDate = null;
            if (fields.containsKey("endDate") && !"null".equals(fields.get("endDate"))) {
                endDate = LocalDate.parse(unescapeJson(fields.get("endDate")), LOCAL_DATE_FORMATTER);
            }

            
            Position position = null;
            if (fields.containsKey("position") && !"null".equals(fields.get("position"))) {
                position = Position.valueOf(fields.get("position"));
            }

            
            Status status = null;
            if (fields.containsKey("status") && !"null".equals(fields.get("status"))) {
                status = Status.valueOf(fields.get("status"));
            }

            
            Organization organization = null;
            if (fields.containsKey("organization") && !"null".equals(fields.get("organization"))) {
                organization = jsonToOrganization(fields.get("organization"));
            }

            Worker worker = new Worker(id, name, coordinates, salary, endDate, position, status, organization);

            return worker;

        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга JSON: " + e.getMessage());
        }
    }

    private static Coordinates jsonToCoordinates(String json) {
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1, json.length() - 1);

        Map<String, String> fields = parseJsonFields(json);

        double x = Double.parseDouble(fields.get("x"));
        Float y = Float.parseFloat(fields.get("y"));

        return new Coordinates(x, y);
    }

    private static Organization jsonToOrganization(String json) {
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1, json.length() - 1);

        Map<String, String> fields = parseJsonFields(json);

        Integer annualTurnover = Integer.parseInt(fields.get("annualTurnover"));
        long employeesCount = Long.parseLong(fields.get("employeesCount"));

        OrganizationType type = null;
        if (fields.containsKey("type") && !"null".equals(fields.get("type"))) {
            type = OrganizationType.valueOf(fields.get("type"));
        }

        return new Organization(annualTurnover, employeesCount, type);
    }


    private static Map<String, String> parseJsonFields(String json) {
        Map<String, String> result = new HashMap<>();

        boolean inString = false;
        int braceDepth = 0;
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean readingKey = true;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            char prev = (i > 0) ? json.charAt(i-1) : 0;

            // Обработка кавычек (с учётом экранирования)
            if (c == '"' && prev != '\\') {
                inString = !inString;
                continue;
            }

            if (!inString) {

                if (c == '{' || c == '[') braceDepth++;
                if (c == '}' || c == ']') braceDepth--;


                if (c == ':' && braceDepth == 0) {
                    readingKey = false;
                    continue;
                }
                if (c == ',' && braceDepth == 0) {
                    if (key.length() > 0) {
                        result.put(key.toString().trim(), value.toString().trim());
                    }
                    key = new StringBuilder();
                    value = new StringBuilder();
                    readingKey = true;
                    continue;
                }
            }

            if (readingKey) {
                key.append(c);
            } else {
                value.append(c);
            }
        }

        if (key.length() > 0) {
            result.put(key.toString().trim(), value.toString().trim());
        }

        return result;
    }

    private static String unescapeJson(String s) {
        if (s == null) return null;
        return s.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
}
