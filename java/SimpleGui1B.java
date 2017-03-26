import javax.swing.*;
import java.awt.event.*;
// implements the listener for the button object
public class SimpleGui1B implements ActionListener {
	JButton button;
	int numClicks = 0;
	public static void main(String[] args) {
		SimpleGui1B gui = new SimpleGui1B();
		gui.go();
	}
	public void go() {
		JFrame frame = new JFrame();
		button = new JButton("click me");
		
		// add 'this' class as a listener to the button
		button.addActionListener(this);
		
		frame.getContentPane().add(button);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
	// implements the interface "contract"
	// when button is pressed, this method will be called
	public void actionPerformed(ActionEvent event){
		numClicks++;
		button.setText("I've been clicked " + numClicks + " times.");
	}
}