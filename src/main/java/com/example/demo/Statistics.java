
package com.example.demo;
public class Statistics {
    private final DataType type;

    private int count;

    // Для чисел
    private double sum;
    private double min;
    private double max;

    // Для строк
    private int minLength;
    private int maxLength;

    public Statistics(DataType type) {
        this.type = type;
        this.count = 0;
        this.sum = 0.0;
        this.min = Double.MAX_VALUE;
        this.max = Double.MIN_VALUE;
        this.minLength = Integer.MAX_VALUE;
        this.maxLength = Integer.MIN_VALUE;
    }

    public void update(String value) {
        count++;

        switch (type) {
            case INTEGER:
            case FLOAT:
                double number = Double.parseDouble(value);
                sum += number;


                if (number < min) {
                    min = number;
                }
                if (number > max) {
                    max = number;
                }
                break;
            case STRING:
                int length = value.length();
                if (length < minLength) {
                    minLength = length;
                }
                if (length > maxLength) {
                    maxLength = length;
                }
                break;
        }
    }

    public int getCount() {
        return count;
    }

    public void printStatistics(boolean fullStats) {
        System.out.println("Статистика для " + type.toString().toLowerCase() + "s:");
        System.out.println("Количество: " + count);
        if (fullStats) {
            switch (type) {
                case INTEGER:
                case FLOAT:
                    System.out.println("Минимальное значение: " + min);
                    System.out.println("Максимальное значение: " + max);
                    System.out.println("Сумма: " + sum);
                    System.out.println("Среднее: " + (sum / count));
                    break;
                case STRING:
                    System.out.println("Длина самой короткой строки: " + minLength);
                    System.out.println("Длина самой длинной строки: " + maxLength);
                    break;
            }
        }
        System.out.println();
    }
}
