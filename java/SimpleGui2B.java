import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
public class SimpleGui2B {
	JFrame frame;
	JLabel label;
	
	public static void main(String[] args) {
		SimpleGui2B gui = new SimpleGui2B();
		gui.go();
	}
	public void go() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton labelButton = new JButton("Change label");
		labelButton.addActionListener(new LabelListener());
		
		JButton colorButton = new JButton("Change Color");
		colorButton.addActionListener(new ColorListener());
		
		label = new JLabel("I'm a label", JLabel.CENTER);
		MyDrawPanel panel = new MyDrawPanel();
		
		frame.getContentPane().add(BorderLayout.SOUTH, colorButton);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.getContentPane().add(BorderLayout.EAST, labelButton);
		frame.getContentPane().add(BorderLayout.WEST, label);
		
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
	
	class LabelListener implements ActionListener {
		private String[] labelList = {"Ouch!", "I've changed!", "Hello!", 
		                               "Hi!", "Hello world.", "S'up?",
									   "Hei Dude.", "I'm on the road again"};
		private int numLabels = labelList.length;
		
		public void actionPerformed(ActionEvent ev) {
			int i = (int) (Math.random() * numLabels);
			label.setText(String.format("%-24s", labelList[i]));
		}
	}
	
	class ColorListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			frame.repaint();
		}
	}
}

class MyDrawPanel extends JPanel {
	public void paintComponent(Graphics gfx) {
		Graphics2D g2d = (Graphics2D) gfx;
		
		gfx.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		Color startColor = new Color(r, g, b);
		
		r = (int) (Math.random() * 255);
		g = (int) (Math.random() * 255);
		b = (int) (Math.random() * 255);
		Color endColor = new Color(r, g, b);
		
		GradientPaint gradient = new GradientPaint(70, 70, startColor, 150, 150, endColor);
		g2d.setPaint(gradient);
		g2d.fillOval(70, 70, 100, 100);
	}
}