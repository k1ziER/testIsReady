package com.example.demo;

import org.apache.commons.cli.*;




import java.util.*;

public class Main {

    public static void main(String[] args) {

        CommandLine cmd = parseCommandLine(args);
        if (cmd == null) {
            return;
        }


        boolean appendMode = cmd.hasOption("a");
        boolean shortStats = cmd.hasOption("s");
        boolean fullStats = cmd.hasOption("f");
        String outputPath = cmd.getOptionValue("o", "");
        String prefix = cmd.getOptionValue("p", "");
        List<String> inputFiles = cmd.getArgList();

        if (inputFiles.isEmpty()) {
            System.err.println("Не указаны входные файлы.");
            return;
        }


        FileProcessor fileProcessor = new FileProcessor(appendMode, outputPath, prefix);


        for (String fileName : inputFiles) {
            fileProcessor.processFile(fileName);
        }


        if (shortStats || fullStats) {
            fileProcessor.printStatistics(fullStats);
        }
    }

    private static CommandLine parseCommandLine(String[] args) {
        Options options = new Options();

        options.addOption("a", false, "Режим добавления в существующие файлы");
        options.addOption("s", false, "Вывести краткую статистику");
        options.addOption("f", false, "Вывести полную статистику");
        options.addOption("o", true, "Путь для выходных файлов");
        options.addOption("p", true, "Префикс имен выходных файлов");
        options.addOption("h", "help", false, "Показать помощь");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("java -jar file-filter-utility.jar [options] <inputFiles>", options);
                return null;
            }

            return cmd;
        } catch (ParseException e) {
            System.err.println("Ошибка при разборе аргументов командной строки: " + e.getMessage());
            formatter.printHelp("java -jar file-filter-utility.jar [options] <inputFiles>", options);
            return null;
        }
    }
}

