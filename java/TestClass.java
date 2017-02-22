public class TestClass {
  public static void main(String []args) {
    System.out.println("Hello world");
    final double []v1 = {2.0, 2.0, 3.0};
    final double []v2 = {4.0, 2.0, 6.0};
    show("v1", v1);
    show("v2", v2);
    
    // final local variables can be initialised later, but only once
    // note that final is all about the reference, and not the contents...
    final double []v3 = new double[v1.length];
    vmult(v3, v1, v2);
    show("v3", v3);
    vmult(v3, v3, v3); // ... so this works ...
    show("v3^2", v3);
    //v3 = new double[2]; // ... but this doesn't.
    
    final double []v4 = vmult(v1, v2);
    show("v4", v4);
  }
  
  private static void vmult(double []v, double []v1, double []v2) {
    int len = v1.length;
    for(int i=0; i<len; i++) {
      v[i] = v1[i] * v2[i];
    }
  }

  private static double[] vmult(double []v1, double []v2) {
    int len = v1.length;
    double []v = new double[len];
    for(int i=0; i<len; i++) {
      v[i] = v1[i] * v2[i];
    }
    return v;
  }  
  
  private static void show(String nm, double []v) {
    System.out.print(nm + " = ");
    for(double x: v) { System.out.print(x + " "); }
    System.out.println();
  }
}
