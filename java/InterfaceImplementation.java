import java.awt.Graphics;

/*
 * Interfaces
Set of classes that share methods
Declare an interface with the common methods
Can use the interface, without knowing an object?s specific type

Only have methods (mostly true)
Do not provide code, only the definition(called signatures)
A class can implement any number ofinterface
 */
interface ICar {
    boolean isCar = true;
    int getNumWheels();
}

public class InterfaceImplementation implements ICar { 
    public static void main(String[] args) {
        InterfaceImplementation car = new InterfaceImplementation();
        System.out.println(car.getNumWheels());
    }
    public int getNumWheels() { 
        return 18; 
    } 
}