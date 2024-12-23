package com.example.demo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileProcessor {
    private final boolean appendMode;
    private final String outputPath;

    private final String prefix;

    private final Map<DataType, List<String>> dataMap;
    private final Map<DataType, Statistics> statsMap;

    public FileProcessor(boolean appendMode, String outputPath, String prefix) {
        this.appendMode = appendMode;
        this.outputPath = outputPath;
        this.prefix = prefix;

        dataMap = new EnumMap<>(DataType.class);
        statsMap = new EnumMap<>(DataType.class);

        for (DataType type : DataType.values()) {
            dataMap.put(type, new ArrayList<>());
            statsMap.put(type, new Statistics(type));
        }
    }

    public void processFile(String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла " + fileName + ": " + e.getMessage());
        }

        writeDataToFile();
    }

    private void processLine(String line) {
        DataType type = determineDataType(line);

        if (type != null) {
            dataMap.get(type).add(line);
            statsMap.get(type).update(line);
        }
    }

    private DataType determineDataType(String line) {
        // Проверка на целое число
        try {
            Integer.parseInt(line);
            return DataType.INTEGER;
        } catch (NumberFormatException ignored) {
        }

        // Проверка на вещественное число
        try {
            Double.parseDouble(line);
            return DataType.FLOAT;
        } catch (NumberFormatException ignored) {
        }

        // Иначе, это строка
        if (!line.trim().isEmpty()) {
            return DataType.STRING;
        }
        return null;
    }

    private void writeDataToFile() {
        for (DataType type : DataType.values()) {
            List<String> dataList = dataMap.get(type);
            if (dataList.isEmpty()) {
                continue;
            }

            String fileName = prefix + type.getFileName();
            String fullPath = outputPath.isEmpty() ? fileName : outputPath + File.separator + fileName;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath, appendMode))) {
                for (String data : dataList) {
                    writer.write(data);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Ошибка при записи в файл " + fullPath + ": " + e.getMessage());
            }
        }
    }

    public void printStatistics(boolean fullStats) {
        for (DataType type : DataType.values()) {
            Statistics stats = statsMap.get(type);
            if (stats.getCount() > 0) {
                stats.printStatistics(fullStats);
            }
        }
    }
}

