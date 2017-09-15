import java.util.ArrayList;
import java.util.Arrays;

public class DotComTestDrive {
    public static void main(String[] args) {
		DotCom dot = new DotCom();
		ArrayList<String> locations = new ArrayList<String>(Arrays.asList("2", "3", "4"));
		dot.setLocationCells(locations);
		
		String[] userGuess = {"2", "3", "5", "4"};
		String[] expected = {"hit", "hit", "miss", "kill"};
		
		String testResult = "passed";
		for(byte i=0; i < userGuess.length; i++) {
			if( ! expected[i].equals(dot.checkYourself(userGuess[i]))) {
				testResult = "failed";
				break;
			}
		}
		System.out.println(testResult);
	}
}