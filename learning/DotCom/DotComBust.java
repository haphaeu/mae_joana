import java.util.*;

public class DotComBust {
    private GameHelper helper = new GameHelper();
	private ArrayList<DotCom> dotComsList = new ArrayList<DotCom>();
	private int numOfGuesses = 0;
	
	private void setupGame() {
		DotCom dot1 = new DotCom();
		DotCom dot2 = new DotCom();
		DotCom dot3 = new DotCom();
		dot1.setName("Google.com");
		dot2.setName("Bing.com");
		dot3.setName("Facebook.com");
		dotComsList.add(dot1);
		dotComsList.add(dot2);
		dotComsList.add(dot3);
		
		System.out.println("Your goal is to sink three dot coms.");
		System.out.println("Try to sink them all in the fewest number of guesses.");
		
		for (DotCom dotComToSet: dotComsList) {
			ArrayList<String> newLocation = helper.placeDotCom(3);
			dotComToSet.setLocationCells(newLocation);
		} 
	}
	
	private void startPlaying() {
		while (!dotComsList.isEmpty()) {
			String userGuess = helper.getUserInput("Enter a cell guess:");
			checkUserGuess(userGuess);
		}
		finishGame();
	}
	
	private void checkUserGuess(String userGuess) {
		numOfGuesses++;
		String result = "miss";
		
		for (DotCom dotComToTest: dotComsList) {
			result = dotComToTest.checkYourself(userGuess);
			if (result.equals("hit")) {
				break;
			}
			if (result.equals("kill")) {
				dotComsList.remove(dotComToTest);
				break;
			}
		}
		System.out.println(result);
	}
	
	private void finishGame() {
		System.out.println("All freaking .coms are dead!");
		if (numOfGuesses < 18) {
			System.out.println("It took you only " + numOfGuesses + " guesses.");
			System.out.println("Not bad. Surely only luck...");
		} else {
			System.out.println("It took you insane " + numOfGuesses + " guesses.");
			System.out.println("Jeez that was bogus, dude.");
		}
	}
	
	public static void main(String[] args) {
		DotComBust game = new DotComBust();
		game.setupGame();
		game.startPlaying();
	}
}