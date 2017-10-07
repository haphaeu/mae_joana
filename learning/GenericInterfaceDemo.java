/*
 * Generics demos
 *
 * Generic interface
 */
interface MinMax<T extends Comparable<T>> {
   T min();
   T max();
}

// The class implementing a generic interface must have the 
// same generic argument bounds as the generic interface it
// implements. 
// Note that once the bounds have been defined, they don't 
// need to be repeated.
class MyClass <T extends Comparable<T>> implements MinMax<T> {
   T[] vals;   
   MyClass(T[] o) { vals = o; }
   public T min() {
      T v = vals[0];
      for (int i=0; i < vals.length; i++)
         if (vals[i].compareTo(v) < 0) v = vals[i];
      return v;
   }
   public T max() {
      T v = vals[0];
      for (int i=0; i < vals.length; i++)
         if (vals[i].compareTo(v) > 0) v = vals[i];
      return v;
   }   
}

// Here the same class above, but showing an example
// of  non-generic class implementing a generic
// interface by defining its generic type.
class MyClassNonGen implements MinMax<Integer> {
   Integer[] vals;   
   MyClassNonGen(Integer[] o) { vals = o; }
   public Integer min() {
      int v = vals[0];
      for (int i=0; i < vals.length; i++)
         if (vals[i].compareTo(v) < 0) v = vals[i];
      return v;
   }
   public Integer max() {
      int v = vals[0];
      for (int i=0; i < vals.length; i++)
         if (vals[i].compareTo(v) > 0) v = vals[i];
      return v;
   }   
}


class GenericInterfaceDemo {
   public static void main(String[] a) {
      Integer inums[] = {3, 6, 5, 4, 3};
      Character chs[] = {'b', 'h', 'f', 'e', 'j', 'f'};
      
      MyClass<Integer> iob = new MyClass<>(inums);
      MyClass<Character> cob = new MyClass<>(chs);
      
      System.out.println("Max value in inums: " + iob.max());
      System.out.println("Min value in inums: " + iob.min());
      System.out.println("Max value in chs: " + cob.max());
      System.out.println("Min value in chs: " + cob.min());
      
      MyClassNonGen iobng = new MyClassNonGen(inums);
      //MyClassNonGen cobnb = new MyClassNonGen(chs); // not allowed here!
      
      System.out.println("Max value in inums: " + iobng.max());
      System.out.println("Min value in inums: " + iobng.min());
      //System.out.println("Max value in chs: " + cobng.max());
      //System.out.println("Min value in chs: " + cobng.min());
   }
}