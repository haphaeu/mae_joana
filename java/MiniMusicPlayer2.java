import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MiniMusicPlayer2 {
	
	static JFrame f = new JFrame("My First Music Video");
	static MyDrawPanel ml;
	
	public static void main(String[] args) {
		MiniMusicPlayer2 mini = new MiniMusicPlayer2();
		mini.go();
	}
	
	public void setupGUI() {
		ml = new MyDrawPanel();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(ml);
		f.setBounds(30, 30, 300, 300);
		f.setVisible(true);
	}
	
	public void go() {
		
		setupGUI();
		
		try {
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addControllerEventListener(ml, new int[] {127});
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			int r = 0;
			for (int i=0; i < 120; i+=4) {
				r = (int) ((Math.random() * 50) + 1);
				track.add(makeEvent(144, 1, r, 100, i));
				track.add(makeEvent(176, 1, 127, 0, i));
				track.add(makeEvent(128, 1, r, 100, i+2));
			}
			sequencer.setSequence(seq);
			sequencer.setTempoInBPM(120);
			sequencer.start();
		} catch (Exception ex) {ex.printStackTrace();}
	}
	
	public static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch (Exception e) {	}
		return event;
	}
	
	class MyDrawPanel extends JPanel implements ControllerEventListener {
		boolean msg = false;
		
		public void controlChange(ShortMessage event) {
			msg = true;
			repaint();
			System.out.println("la");
		}
		public void paintComponent(Graphics gfx) {
			if (msg) {
				int r = (int) (Math.random()*250);
				int g = (int) (Math.random()*250);
				int b = (int) (Math.random()*250);
				gfx.setColor(new Color(r, g, b));
				
				int ht = (int) ((Math.random()*120) + 10);
				int wd = (int) ((Math.random()*120) + 10);
				int x = (int) ((Math.random()*40) + 10);
				int y = (int) ((Math.random()*40) + 10);
				gfx.fillRect(x, y, ht, wd);
				msg = false;
			}
		}
	}
}