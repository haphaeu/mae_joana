public class TestOverrideToString {
    public static void main(String[] args) {
        Duck d = new Duck("Donald");
        d.quack();
        System.out.println(d);
    }
    
}

class Duck {
    String name;
    Duck (String nm) {
        name = nm;
    }
    public void quack() {
        System.out.println("Quack! I'm " + name);
    }
    @Override
    public String toString() {
        return "I'm " + name;
    }
}