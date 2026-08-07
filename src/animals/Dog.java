package animals;

public class Dog extends Animal {
    public Dog(int id, String name, int age, double weight, String color) {
        super(id, name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Гав");
    }

    @Override
    public String getType() {
        return "dog";
    }
}