import java.io.*;
import java.util.concurrent.*;

/**
 * Example working with text files
*/
class TextFilesTest {
	int numFiles;
	static int defaultNumFiles = 5;
	
	public static void main(String[] args) {
		int firstArg;
		if (args.length > 0) {
			try {
				firstArg = Integer.parseInt(args[0]);
				TextFilesTest tft = new TextFilesTest(firstArg);
			} catch (NumberFormatException e) {
				System.err.println("Argument" + args[0] + " must be an integer.");
				System.exit(1);
			}
		} else {
			TextFilesTest tft = new TextFilesTest();
		}
	}
	TextFilesTest() {
		this(defaultNumFiles);
	}
	TextFilesTest(int n) {
		numFiles = n;
		runit();
	}
	void runit() {
		// doing it serially
		
		System.out.println("Serial creation of " + numFiles + " random text files...");
		
		long startTime = System.nanoTime();
		
		TextFile txt;
		for (int i=0; i < numFiles; i++) {
			txt = new TextFile("sFile" + (i+1) + ".txt");
			txt.createContents();
			txt.save();
			txt = null;
		}
		
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("elapsed time: " + elapsedTime/1e9 + " s");
		
		// and using threads
		System.out.println("Threaded creation of " + numFiles + " random text files...");
		
		startTime = System.nanoTime();
		
		Job[] job = new Job[numFiles];
		int nThreads = 1500;
		ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
		for (int i=0; i < numFiles; i++) {
			job[i] = new Job(i+1);
			threadPool.submit(job[i]);
			//t[i] = new Thread(job[i]);
			//t[i].start();
		}
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch(Exception e) {
			e.printStackTrace();
		} 
		
		elapsedTime = System.nanoTime() - startTime;
		System.out.println("elapsed time: " + elapsedTime/1e9 + " s");
	}
}

class Job implements Runnable {
	int id;
	TextFile txt;
	
	Job(int id) {
		this.id = id;
	}
	
	public void run() {
			txt = new TextFile("tFile" + id + ".txt");
			txt.createContents();
			txt.save();
	}
}
