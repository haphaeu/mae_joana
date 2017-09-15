/* Encapsulation is not leaving the instance vriables accsible,
instead, they'ree declared as private and public Getter and Setters
are implemented.
*/
class EncapsulatedDog {
    private int size;
	private String name;
	
	public void setSize(int s) {
	    size = s;
	}
	public int getSize() {
	    return size;
	}
	public void setName(String nm) {
	    name = nm;
	}
	public String getName() {
	    return name;
	}
	void bark() {
	    if (size > 30) {
			System.out.println(name + ": Woof! Woof!");
		} else if (size > 10) {
			System.out.println(name + ": Ruf! Ruf!");
		} else {
			System.out.println(name + ": Yip! Yip!");
		}
	}
}

public class EncapsulatedDogTest {
    public static void main(String[] args) {
	    EncapsulatedDog d1 = new EncapsulatedDog();
		EncapsulatedDog d2 = new EncapsulatedDog();
		EncapsulatedDog d3 = new EncapsulatedDog();
		d1.setSize(35);
		d1.setName("Rufus");
		d2.setSize(15);
		d2.setName("Chuli");
		d3.setSize(5);
		d3.setName("Ratinho");
		d1.bark();
		d2.bark();
		d3.bark();
	}
}