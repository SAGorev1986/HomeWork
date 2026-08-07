import animals.*;
import factory.AnimalFactory;
import factory.AnimalType;
import tables.AnimalTable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Создаем таблицу для работы с БД (вместо AnimalDao)
        AnimalTable animalTable;
        try {
            animalTable = new AnimalTable();
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к БД: " + e.getMessage());
            return;
        }

        AnimalFactory factory = new AnimalFactory();
        boolean isRunning = true;

        System.out.println("Добро пожаловать в приложение управления животными!");

        while (isRunning) {
            System.out.print("\nКоманды (add/list/filter/update/delete/exit): ");
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "ADD":
                    addAnimal(scanner, animalTable, factory);
                    break;
                case "LIST":
                    listAnimals(animalTable);
                    break;
                case "FILTER":
                    filterAnimals(scanner, animalTable);
                    break;
                case "UPDATE":
                    updateAnimal(scanner, animalTable);
                    break;
                case "DELETE":
                    deleteAnimal(scanner, animalTable);
                    break;
                case "EXIT":
                    System.out.println("Выход из программы. До свидания!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Неизвестная команда. Попробуйте еще раз.");
            }
        }

        animalTable.close();
        scanner.close();
    }

    private static void addAnimal(Scanner scanner, AnimalTable animalTable, AnimalFactory factory) {
        String typeInput = getValidAnimalType(scanner);
        AnimalType type = AnimalType.valueOf(typeInput.toUpperCase());

        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        System.out.print("Возраст: ");
        int age = parseIntSafe(scanner);
        System.out.print("Вес (кг): ");
        double weight = parseDoubleSafe(scanner);
        System.out.print("Цвет: ");
        String color = scanner.nextLine().trim();

        try {
            // Используем фабрику и таблицу напрямую
            Animal animal = factory.createAnimal(type, name, age, weight, color);
            animalTable.addAnimal(animal.getType(), animal.getName(), animal.getAge(), animal.getWeight(), animal.getColor());
            System.out.println("Животное успешно сохранено в БД!");
            System.out.print("Оно говорит: ");
            animal.say();
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении: " + e.getMessage());
        }
    }

    private static void listAnimals(AnimalTable animalTable) {
        try {
            List<Map<String, String>> animals = animalTable.getAllAnimals();
            if (animals.isEmpty()) {
                System.out.println("Список животных пуст.");
                return;
            }
            System.out.println("\n--- Список всех животных из БД ---");
            for (Map<String, String> row : animals) {
                printAnimalFromMap(row);
            }
            System.out.println("----------------------------------");
        } catch (SQLException e) {
            System.out.println("Ошибка при чтении: " + e.getMessage());
        }
    }

    private static void filterAnimals(Scanner scanner, AnimalTable animalTable) {
        System.out.print("Введите тип для фильтрации (cat/dog/duck): ");
        String type = scanner.nextLine().trim().toLowerCase();

        try {
            List<Map<String, String>> filtered = animalTable.getAnimalsByType(type);
            if (filtered.isEmpty()) {
                System.out.println("Животных этого типа не найдено.");
            } else {
                System.out.println("\n--- Отфильтрованный список ---");
                for (Map<String, String> row : filtered) {
                    printAnimalFromMap(row);
                }
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при фильтрации: " + e.getMessage());
        }
    }

    private static void updateAnimal(Scanner scanner, AnimalTable animalTable) {
        System.out.print("Введите ID животного для редактирования: ");
        int id = parseIntSafe(scanner);

        System.out.print("Новое имя: ");
        String name = scanner.nextLine().trim();
        System.out.print("Новый возраст: ");
        int age = parseIntSafe(scanner);
        System.out.print("Новый вес: ");
        double weight = parseDoubleSafe(scanner);
        System.out.print("Новый цвет: ");
        String color = scanner.nextLine().trim();

        try {
            boolean updated = animalTable.updateAnimal(id, name, age, weight, color);
            if (updated) {
                System.out.println("Животное успешно обновлено!");
            } else {
                System.out.println("Животное с ID " + id + " не найдено в базе данных.");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }
    }

    private static void deleteAnimal(Scanner scanner, AnimalTable animalTable) {
        System.out.print("Введите ID животного для удаления: ");
        int id = parseIntSafe(scanner);

        try {
            boolean deleted = animalTable.delete(id);
            if (deleted) {
                System.out.println("Животное успешно удалено!");
            } else {
                System.out.println("Животное с ID " + id + " не найдено в базе данных.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }
    }

    // Вспомогательный метод для красивого вывода Map
    private static void printAnimalFromMap(Map<String, String> row) {
        String id = row.get("id");
        String name = row.get("name");
        String ageStr = row.get("age");
        String weight = row.get("weight");
        String color = row.get("color");

        int age = Integer.parseInt(ageStr);
        String ageWord = getAgeWord(age);

        System.out.printf("[ID: %s] Привет! Меня зовут %s, мне %d %s, я вешу - %s кг, мой цвет - %s.%n",
                id, name, age, ageWord, weight, color);
    }

    private static String getAgeWord(int age) {
        int absAge = Math.abs(age) % 100;
        int n1 = absAge % 10;
        if (absAge > 10 && absAge < 20) return "лет";
        if (n1 > 1 && n1 < 5) return "года";
        if (n1 == 1) return "год";
        return "лет";
    }

    private static String getValidAnimalType(Scanner scanner) {
        while (true) {
            System.out.print("Какое животное добавить из (cat/dog/duck)? ");
            String type = scanner.nextLine().trim().toLowerCase();
            if (type.equals("cat") || type.equals("dog") || type.equals("duck")) {
                return type;
            }
            System.out.println("Неверное животное! Выберите из: cat, dog или duck.");
        }
    }

    private static int parseIntSafe(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Некорректный ввод. Введите число: ");
            }
        }
    }

    private static double parseDoubleSafe(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.print("Некорректный ввод. Введите число: ");
            }
        }
    }
}