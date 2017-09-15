import java.util.ArrayList;

class ArrayListExample {
    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("This");
        strings.add("is");
        strings.add("a");
        strings.add("string");
        
        System.out.println(strings.size());

        for (int i=0; i < strings.size(); i++) {
            System.out.print(strings.get(i) + " ");
        }
        System.out.println();
        
        strings.set(strings.size()-1, "ArrayList");
        
        for(String s: strings) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
    