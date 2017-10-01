/**
 * Example of thread synchronization
 *
 * The method modified 'synchronized' only lets 1 thread
 * to run that method each time, avoiding concurrency.
 *
 * Note that also synchronized blocks can be done within
 * a method only for the statements that need to be synced:
 *
 *     synchronized(this) {
 *         statemets...
 *         methodCalls()...
 *     }
 *
 * The argument 'this' is the object that needs to be "locked".
 *
 * @author: Raf
 * @since: 2017.03.31
 * @see: Java Head First, Chapter 15.
*/

public class TestThreads implements Runnable {
    Cobaia cob = new Cobaia();
    public static void main(String[] args) {
        TestThreads job = new TestThreads();
        Thread t1 = new Thread(job);
        Thread t2 = new Thread(job);
        Thread t3 = new Thread(job);
        Thread t4 = new Thread(job);
        Thread t5 = new Thread(job);
        Thread t6 = new Thread(job);
        Thread t7 = new Thread(job);
        Thread t8 = new Thread(job);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
    }
    public void run() {
        double ret = 0;
        for (int i=0; i<1000; i++) {
            ret = cob.doIt();
        }
        System.out.println("from thread " + Thread.currentThread().getName() + ": " + ret);
    }
    void sleep(int n) {
        try {
            Thread.sleep(n);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class Cobaia {
    private double a = 1.0;
    public double getA() {
        return a;
    }
    public void doubleA() {
        a *= 2.0;
    }
    public void halfA() {
        a /= 2.0;
    }
    /**
     * Here is where the magic happens.
     *
     * Remove the modifier synchronized to see the value of
     * a going bananas...
     */
    public synchronized double doIt() {
        doubleA();
        halfA();
        return a;
    }
}