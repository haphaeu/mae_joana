public class TestBox {
    Integer i; // not initialised: i = null
	int j; // not initialised: j = 0
	public static void main(String[] args) {
		TestBox t = new TestBox();
		t.go();
	}
	public void go() {
		//j = i; // can't assign a null Integer to a primitive
		System.out.println(j);
		System.out.println(i);
	}
}