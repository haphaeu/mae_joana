/*
 * Generics simple demo
 *
 * Using wildcards
 */
 
 class Stats<T extends Number> {
   // here T is a placehold for whatever class is passed
   // when the class is instantiated. Note that T
   // has an upper bound as Number, so only Integer, Double,
   // Float and other subclasses of Number are allowed.
   T[] nums;
   
   Stats(T[] o) {
      nums = o;
   }
   double average() {
      double sum = 0.0;
      for (int i=0; i < nums.length; i++) {
         sum += nums[i].doubleValue();
      }
      return sum / nums.length;
   }
   // Here we want any Stats instance, but restricting
   // to the class T that has instantiated the object.
   // For example, we want to compare the average of a 
   // Double against an Integer.
   // The wildcard <?> is used - and not T!. Note that the
   // same bounds as T still apply - <?> must extend Number.
   boolean sameAverage(Stats<?> ob) {
      if (average() == ob.average())
         return true;
      return false;
   }
 }
 
 class GenericWildcardDemo {
   public static void main(String[] a) {
      Integer inums[] = {1, 2, 3, 4, 5};
      Stats<Integer> iob = new Stats<>(inums);
      double v = iob.average();
      System.out.println("iob average is: " + v);
      
      Double dnums[] = {1.1, 2.2, 3.3, 4.4, 5.5};
      Stats<Double> dob = new Stats<>(dnums);
      double w = dob.average();
      System.out.println("dob average is: " + w);
      
      System.out.print("The averages of iob and dob ");
      if (iob.sameAverage(dob))
         System.out.println("are the same.");
      else
         System.out.println("differ.");
   }
 }