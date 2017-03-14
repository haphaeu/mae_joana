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
interface Drawable {
    void draw(Graphics surface);
    void setColor(Color color);
}

class Flower implements Drawable {
    // ... other stuff ...
    public void draw(Graphics surface){
    // ... code to draw a flower here ...
    }
}
