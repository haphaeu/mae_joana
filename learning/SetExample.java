import java.util.TreeSet;
/*
 * Sets
Like an ArrayList, but
Only one copy of each object, and
No array index Features
Add objects to the set
Remove objects from the set
Is an object in the set?
TreeSet: Sorted (lowest to highest) 
HashSet: Unordered (pseudo-random)
 */
class SetExample {
    public static void main(String[] arguments){ 
        TreeSet<String> strings = new TreeSet<String>(); 
        strings.add("Evan"); 
        strings.add("Eugene"); 
        strings.add("Adam");
        System.out.println(strings.size());
        System.out.println(strings.first());
        System.out.println(strings.last());
        strings.remove("Eugene");
        for (String s : strings){ 
            System.out.println(s); 
        } 
    } 
}