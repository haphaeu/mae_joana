public class QuickPerf {
	private static final int N = (int) 2e8;
	public static void main(String[] args) {
		System.out.println("Starting...");
		System.out.println("Performing " + N + " square roots.");
		long startTime = System.nanoTime();
		doSomething();
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("elapsed time: " + elapsedTime/1e9 + " s");
	}

	public static void doSomething() {
		double tmp;
		int i;
		for(i=0; i<N; i++) {
			tmp = Math.sqrt(i);
		}
	}
}