public class AssertTest {
    
    void printSqrt(float num) {
        // this will raise an error is num <= 0
        assert (num > 0) : "printSqrt(float num): argument must be positive";
        System.out.println("sqrt(" + num + ") = " + String.format("%.2f", Math.sqrt(num)));
    }
    
    void doTest() {
        float x = 4;
        while (true) {
            printSqrt(x--);
        }
    }
    
    public static void main(String[] args) {
        // we don't need to keep a reference to the instance, so just call new
        // and do not assign it to a varible:
        new AssertTest().doTest();  
    }
}