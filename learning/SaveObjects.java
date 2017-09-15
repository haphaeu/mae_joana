import java.io.*;

/**
 * Example of how to SAVE and LOAD objects states.
 *
 * The Serializable interface marks a class to be serialized (saved).
 *
 * Notes on serialization:
 *
 * - Classes need to implement Serializable to be serializable - duh!
 *
 * - Instance variables referencing to other objects which don't implement
 *   Serializable themselves cannot be serialized. In other words, all classes
 *   instantialised as instance variables must implement Serializable.
 *
 * - transient variables are skipped. This is a (bad) way to serialize objects 
 *   referencing to non-serializable objects.
 *
 * - A non-serializable class, and non-final, can be subclassed to implement
 *   Serializable. However, see note on de-serialization.
 *
 * - static variables are not serialized. They don't need to be - as long as
 *   the implementation doesn't change it value.
 *
 * Notes on de-serialization:
 *
 * - The constructor does not run.
 *
 * - If the object has a non-serializable superclass, the constructor 
 *   for that superclass (and all above it) will run.
 *
 * @author        Raf
 * @since         2017.03.26
 * @see           Head First Java - 2nd Edition - Chapter 14.
 */
class SaveObjects implements Serializable {
	
	// let's forget about enapsuation in this example, to spare us of setters and getters
	int a;
	double x;

	// transient variables are NOT serialized
	// this is to be used for instance variables referencing to classes
	// that do not implement the Serializable interface and cannot be 
	// subclassed (final).
	transient float y = 9.99f; 
	
	SaveObjects(int a, double x) {
		this.a = a;
		this.x = x;
	}
	SaveObjects() {
		this(1, 3.14);
	}
	
	void saveMe(String fname) {
		try {
			FileOutputStream fs = new FileOutputStream(fname);
			ObjectOutputStream objOut = new ObjectOutputStream(fs);
			objOut.writeObject(this);
			objOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	static SaveObjects loadMe(String fname) {
		Object obj = null;
		try {
			FileInputStream fs = new FileInputStream(fname);
			ObjectInputStream objIn = new ObjectInputStream(fs);
			obj = objIn.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (SaveObjects) obj;
	}
	
	public static void main(String[] args) {
		SaveObjects sobj;
		
        sobj = new SaveObjects();
		System.out.print("Before: ");
		System.out.println(sobj.a + " " + sobj.x + " " + sobj.y);
		
		String fname = "SaveObjects.ser";
		sobj.saveMe(fname);
		
		sobj = null;
		System.out.print("Null: ");
		System.out.println(sobj);
		
		sobj = SaveObjects.loadMe(fname);
		System.out.print("After: ");
		System.out.println(sobj.a + " " + sobj.x + " " + sobj.y);
	}
}