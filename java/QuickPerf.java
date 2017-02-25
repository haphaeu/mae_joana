public class QuickPerf {
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		doSomething();
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println(elapsedTime/1e9);
	}

	public static void doSomething() {
		double tmp;
		int i;
		System.out.println("Starting...");
		for(i=0; i<100000000; i++) {
			tmp = Math.sqrt(i);
		}
			System.out.println("Done");
	}
}