import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer {
	ArrayList clientOutputStreams;
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		public void run() {
			String message;
			try {
				while((message=reader.readLine()) != null) {
					System.out.println("read " + message);
					tellEveryone(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		new VerySimpleChatServer().go();
	}
	public void go() {
		clientOutputStreams = new ArrayList();
		try {
			ServerSocket serverSock = new ServerSocket(5000);
			System.out.println("Server listening on port 5000.");
			while (true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a new connetion.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void tellEveryone(String message) {
		Iterator iter = clientOutputStreams.iterator();
		while (iter.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) iter.next();  //why cast? can't do ArrayList<PrintWriter>??
				writer.println(message);
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
	}
}