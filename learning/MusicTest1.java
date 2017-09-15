import javax.sound.midi.*;

public class MusicTest1 {
	
	public int play() {
		try {
		    Sequencer sequencer = MidiSystem.getSequencer();
			System.out.println("We got a sequencer.");
			return 0;
		} catch (MidiUnavailableException ex) {
			System.out.println("No play Jack boy...");
			return -1;
		} finally {  // this will run no matter what happens above
			System.out.println("End of play.");
		}		
	}

	public static void main(String[] args) {
		MusicTest1 mt = new MusicTest1();
		int retval;
		retval = mt.play();
		System.out.println("retval is " + retval);
		
	}
}