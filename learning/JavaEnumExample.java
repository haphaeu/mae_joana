import java.util.EnumMap;
import java.util.Map;

/**
 * Java program to show How to use Enum in Java via a Simple Example
 *
 * @author Javin Paul
 * http://www.java67.com/2016/01/a-practical-example-of-enum-in-java.html
 */
public class JavaEnumExample {

    public static void main(String args[]) {
      
        // EnumMap is a special high performance map to store Enum constrants
        Map<SoftDrink, Integer> store = new EnumMap<SoftDrink, Integer>(SoftDrink.class);
       
        // Let's initialize store, by storing 10 canes of each drink
        // Enum provides an implicit values() method, which can be used to iterate over Enum
        for(SoftDrink drink : SoftDrink.values()){
            store.put(drink, 10);
        }
        // let's print what is in EnumStore     
        for(Map.Entry<SoftDrink, Integer> entry: store.entrySet()){
            System.out.println(entry.getKey() + " Qty: " + entry.getValue() 
                  + " Price: " + entry.getKey().getPrice());
        }
    }
}

enum SoftDrink{
    COKE("Coke", 75), PEPSI("Pepsi", 75), SODA("Soda", 90), LIME("Lime", 50);
   
    // Java Enum can have member variables
    private String title;
    private int price; // in cents
   
    // You can declare constructor for Enum in Java
    private SoftDrink(String title, int price){
        this.title = title;
        this.price = price;
    }
    // Enum can have methods in Java   
    public String getTitle(){
        return title;
    }
    public int getPrice(){
        return price;
    }
    // Enum can override methods in Java
    @Override
    public String toString() {
        return title;
    }    
}
