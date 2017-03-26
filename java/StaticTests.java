import static java.lang.System.out;
class StaticSuper {
	static {
		out.println("super static block");
	}
	StaticSuper () {
		out.println("super constructor");
	}
}
class StaticSub extends StaticSuper {
	static int rand;
	static {
		rand = (int) (Math.random()*6);
		out.println("sub static block " + rand);
	}
	StaticSub () {
		out.println("sub constructor");
	}
}
public class StaticTests {
	static int rand;
	static {
		rand = (int) (Math.random()*6);
		out.println("static block " + rand); 
	}
	StaticTests () {
		out.println("constructor");
	}
	public static void main(String[] args) {
		out.println("main");
		StaticSub t = new StaticSub();
	}
}