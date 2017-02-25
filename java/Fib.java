/**
 * @author raf
 *
 */
public class Fib {

    public static void main(String[] args) {
        long res;
        long startTime = System.nanoTime();
        int n = 35;
        res = fib(n);
        double elapsedTime = (System.nanoTime() - startTime)/1e6;
        System.out.println("fib(" + n + ") = " + res);
        System.out.println("elapsed time = " + elapsedTime + " ms");
    }

    private static long fib(int n) {
        if (n == 0) 
            return 0l;
        else if (n == 1)
            return 1l;
        else
            return fib(n-1) + fib(n-2);
    }
}
