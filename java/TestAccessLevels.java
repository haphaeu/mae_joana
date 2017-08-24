/*
Test access levels, particularly protected.

 Modifier    class    package    subclass    world
 public        y         y          y          y
 protected     y         y          y          n
    -          y         y          n          n
 private       y         n          n          n
 
 My doubt is why there isn't an access level to make a member available
 for subclasses but not for the package?
 And how does it makes sense to have an access level (no modifier) to make
 a member available for package but not for subclassing?
 
 This example here is too simple to test the 'no modifier' option in subclassing.
 It won't work properly since all is _in the same package_ essentialy.
 
*/
class TestAccessLevels{
	public int imPublic;
	protected int imProtected;
	int imNone;
	private int imPrivate;
	
	public static void main(String[] args) {
		TestAccessLevels a = new TestAccessLevels();
		a.printMe();
		SubClass b = new SubClass();
		b.printMe();
	}
	
	public TestAccessLevels() {
		imPublic = 1;
		imProtected = 2;
		imNone = 3;
		imPrivate = 4;
	}
    void printMe() {
		System.out.println("in " + this.getClass().getName());
		System.out.println("   imPublic = " + imPublic);
		System.out.println("   imProtected = " + imProtected);
		System.out.println("   imNone = " + imNone);
		System.out.println("   imPrivate = " + imPrivate);
	}
}

class SubClass extends TestAccessLevels{
	public SubClass() {
		imPublic = 10;
		imProtected = 20;
		imNone = 30;
		//imPrivate = 40;
	}
	void printMe() { // should this fail is extended in another package???
	    System.out.println("in " + this.getClass().getName());
		System.out.println("   imPublic = " + imPublic);
		System.out.println("   imProtected = " + imProtected);
		System.out.println("   imNone = " + imNone);
		//System.out.println("   imPrivate = " + imPrivate);
		super.printMe();
	}
}
	