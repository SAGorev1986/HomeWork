package factory;

import animals.*;

public class AnimalFactory {

    public Animal createAnimal(AnimalType type, String name, int age, double weight, String color) {
        // ID = 0, так как он генерируется в БД
        switch (type) {
            case CAT:
                return new Cat(0, name, age, weight, color);
            case DOG:
                return new Dog(0, name, age, weight, color);
            case DUCK:
                return new Duck(0, name, age, weight, color);
            default:
                throw new IllegalArgumentException("Неизвестный тип животного: " + type);
        }
    }
}