
public class AbstractExample {
    
    public static void main(String []args) {
        SimplePoint p = new SimplePoint(3, 4);
        p.showPoint();
    }
    
}

/* abstrat classes contain abstract method */
abstract class Point {
    int x, y;
    
    void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    abstract void alert();
}

// this class has no abstract methods but extends an abstract class and do not
// implements the abstract method, so it is abstract
abstract class ColorPoint extends Point {
    int color;
}

// here the abstract method alert() is implemented
class SimplePoint extends ColorPoint {
    SimplePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    void alert() {
        System.out.println("This is an alert");
        showPoint();
    }
    
    public void showPoint () {
        System.out.println("" + this.x + ", " + this.y);
    }
}
