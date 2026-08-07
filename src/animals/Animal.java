package animals;

public class Animal {
    private int id;
    private String name;
    private int age;
    private double weight;
    private String color;

    public Animal(int id, String name, int age, double weight, String color) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.color = color;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getType() {
        return "Неизвестное";
    }

    public void say() {
        System.out.println("Я говорю");
    }

    public void go() {
        System.out.println("Я иду");
    }

    public void drink() {
        System.out.println("Я пью");
    }

    public void eat() {
        System.out.println("Я ем");
    }

    private String getAgeWord(int age) {
        int absAge = Math.abs(age) % 100;
        int n1 = absAge % 10;
        if (absAge > 10 && absAge < 20) return "лет";
        if (n1 > 1 && n1 < 5) return "года";
        if (n1 == 1) return "год";
        return "лет";
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] Привет! Меня зовут %s, мне %d %s, я вешу - %s кг, мой цвет - %s.",
                id, name, age, getAgeWord(age), weight, color);
    }
}