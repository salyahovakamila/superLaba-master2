package client.utils;

import client.console.CommandParser;
import client.network.RequestSender;
import common.CommandRequest;
import common.CommandResponse;
import common.CommandType;
import models.*;
import models.enums.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ScriptRunner {
    private static final Set<String> runningScripts = new HashSet<>();
    private final RequestSender requestSender;
    private final CommandParser commandParser;

    public ScriptRunner(RequestSender requestSender, CommandParser commandParser) {
        this.requestSender = requestSender;
        this.commandParser = commandParser;
    }

    public void executeScript(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("Ошибка: файл '" + filename + "' не найден");
            return;
        }

        if (!file.canRead()) {
            System.out.println("Ошибка: нет прав на чтение файла '" + filename + "'");
            return;
        }

        String absolutePath;
        try {
            absolutePath = file.getCanonicalPath();
        } catch (IOException e) {
            absolutePath = file.getAbsolutePath();
        }

        if (runningScripts.contains(absolutePath)) {
            System.out.println("Ошибка: обнаружена рекурсия в скрипте '" + filename + "'");
            return;
        }

        runningScripts.add(absolutePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            System.out.println("ВЫПОЛНЕНИЕ СКРИПТА: " + filename);

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                System.out.println("  [" + lineNumber + "] > " + line);

                String[] parts = line.split("\\s+", 2);
