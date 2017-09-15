public class GuessGame {
    Player p1;
    Player p2;
    Player p3;

    public void startGame() throws InterruptedException {
        p1 = new Player();
        p2 = new Player();
        p3 = new Player();

        int guessp1 = 0;
        int guessp2 = 0;
        int guessp3 = 0;

        boolean p1isRight = false;
        boolean p2isRight = false;
        boolean p3isRight = false;

        int targetNumber = (int) (Math.random() * 10);
        System.out.println("I'm thinking of a number between 0 and 9...");
		Thread.sleep(2000);
        System.out.println("The number is " + targetNumber);
		Thread.sleep(1500);

        while(true) {
            p1.guess();
            p2.guess();
            p3.guess();

            guessp1 = p1.number;
            guessp2 = p2.number;
            guessp3 = p3.number;
            
            System.out.println("Player one guessed " + guessp1);
			Thread.sleep(1000);
            System.out.println("Player two guessed " + guessp2);
			Thread.sleep(2000);
            System.out.println("Player three guessed " + guessp3);
			Thread.sleep(1000);

            if (guessp1 == targetNumber) {
                p1isRight = true;
            }
            if (guessp2 == targetNumber) {
                p2isRight = true;
            }
            if (guessp3 == targetNumber) {
                p3isRight = true;
            }
            
            if (p1isRight || p2isRight || p3isRight) {
                System.out.println("We have a winner!");
				Thread.sleep(350);
                if (p1isRight) System.out.println("Player one got it right!");
				Thread.sleep(200);
                if (p2isRight) System.out.println("Player two got it right!");
				Thread.sleep(200);
                if (p3isRight) System.out.println("Player three got it right!");
				Thread.sleep(200);
                System.out.println("Game is over.");
				break;
			} else {
				System.out.println("Players will have to try again.");
				Thread.sleep(3000);
			}
		}
	}
}