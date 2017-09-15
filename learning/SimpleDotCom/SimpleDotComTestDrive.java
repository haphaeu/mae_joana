public class SimpleDotComTestDrive {
    public static void main(String[] args) {
		SimpleDotCom dot = new SimpleDotCom();
		int[] locations = {2, 3, 4};
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