/*
 * Generic example
 *
 * Generic constructor to a non-generic class
 */
 class GenCons {
   private double val;
   
   // The constructor defines a generics argument bounded to Number
   // Note that the class is not generic.
   // This is the same syntax for generic methods, where the
   // definition of the generic argument comes between the access
   // levels and the return type - but here for constructor it doesn't
   // have any of these. For example, this is the general syntax for a
   // generic method:
   // static <T extends Number> boolean doSomething(T x) {}
   // or using more than one generic argument type
   // static <T extends Comparable<T>, V extends T> boolean isIn(T x, V[] y) {}
   <T extends Number> GenCons(T arg) {
      val = arg.doubleValue();
   }
   void showVal() {
      System.out.println("val: " + val);
   }
 }
 class GenericConstructorDemo {
   public static void main(String[] a) {
      GenCons iob = new GenCons(100);
      GenCons dob = new GenCons(1.2);
      iob.showVal();
      dob.showVal();
   }
 }
 