import java.util.HashMap;
/*
 * Maps
Stores a (key, value) pair of objects
Look up the key, get back the value
Example: Address Book
  Map from names to email addresses
TreeMap: Sorted (lowest to highest)
HashMap: Unordered (pseudo-random)
 */
class HashMapExample {
       public static void main(String[] arguments){ 
           HashMap<String, String> strings = new HashMap<String, String>(); 
           strings.put("Evan", "email1@mit.edu"); 
           strings.put("Eugene", "email2@mit.edu"); 
           strings.put("Adam", "email3@mit.edu");
           System.out.println(strings.size());
           strings.remove("Evan");
           System.out.println(strings.get("Eugene"));
           for (String s : strings.keySet()) {
               System.out.println(s);
           }
           for (String s : strings.values()) {
               System.out.println(s);
           }
           for (HashMap.Entry<String, String> pairs : strings.entrySet()) {
               System.out.println(pairs); 
           }
       }
}
                       