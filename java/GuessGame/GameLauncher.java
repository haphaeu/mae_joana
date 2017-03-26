public class GameLauncher {
    public static void main(String[] args) {
	    GuessGame g = new GuessGame();
		try {
		    g.startGame();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}