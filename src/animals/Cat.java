package animals;

public class Cat extends Animal {
    public Cat(int id, String name, int age, double weight, String color) {
        super(id, name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Мяу");
    }

    @Override
    public String getType() {
        return "cat";
    }
}