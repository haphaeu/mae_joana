import java.io.*;
import java.net.*;

public class DailyAdviceServer {
	
	String[] adviceList = {"take smaller bites", "ad2", "ad3", "ad4"};
	
	public static void main(String[] args) {
		DailyAdviceServer server = new DailyAdviceServer();
		server.go();
	}
	private String getAdvice() {
		return adviceList[(int) (Math.random() * adviceList.length)];
	}
	public void go() {
		try {
			ServerSocket ss = new ServerSocket(4242);
			System.out.println("Server started.");
			
			while(true) {
				Socket s = ss.accept();
				PrintWriter writer = new PrintWriter(s.getOutputStream());
				String ad = getAdvice();
				System.out.println("Connection. Sent " + ad);
				writer.println(ad);
				writer.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}