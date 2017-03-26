class A {
	void say() {
		System.out.println("I'm A");
	}
}

class B extends A {
	void say() {
		System.out.println("I'm B");
	}
	void sayAgain() {
		System.out.println("I'm B again");
	}
}
class PolymorphicReference {
	public static void main(String[] args) {
		A a = new A();
		B b = new B();
		A w = new B();
		
		a.say();
		b.say();
		b.sayAgain();
		w.say();
		
		// the line below wont compile
		// type A doesn't have this method
		// PolymorphicReference.java:33: error: cannot find symbol
		//        w.sayAgain(); 
		//         ^
        // symbol:   method sayAgain()
        // location: variable w of type A
		//
		// w.sayAgain(); 
	}
}