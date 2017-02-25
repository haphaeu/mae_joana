/**
 *
 * Exemplo simples de classe, extensão de classe e javadocs.
 *
 *
 * @author      raf
 * @version     0.1b
 * @since       Feb 2017
 * @see         https://en.wikipedia.org/wiki/Javadoc
 */
public class SaySomething {
    public static void main(String args[]) {
        Child greet = new Child();
        greet.print();
    }
}

abstract class GrandParent {
    abstract public void print();
}

class Parent extends GrandParent {
    /**
     * Description of the variable here.
     */
    private String output;

    public Parent(String output) {
        this.output = output;
    }

    public Parent() {
        this("hallo");
    }

    /**
     * Short one line description.                           (1)
     * <p>
     * Longer description. If there were any, it would be    (2)
     * here.
     * <p>
     * And even more explanations to follow in consecutive
     * paragraphs separated by HTML paragraph breaks.
     *
     * @param  variable Description text text text.          (3)
     * @return Description text text text.
     */
    public void print() {
        System.out.println(output );
    }
}

class Child extends Parent {
  public Child() {
    super("child");
  }
}