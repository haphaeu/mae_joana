import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Graphics;

public class Catenary {
	private JFrame frame;
	private JPanel background;
	private DrawPanel drawPanel;
	
	public static void main(String[] args) {
		new Catenary().start();
	}
	void start() {
		frame = new JFrame("Catenary");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel textBoxes = new JPanel();
		textBoxes.setLayout(new BoxLayout(textBoxes, BoxLayout.Y_AXIS));
		
		JPanel hPanel = new JPanel();
		JLabel hLabel = new JLabel(String.format("%30s", "Horizontal distance H [m]:"), SwingConstants.LEFT);
		JTextField hText = new JTextField(10);
		hPanel.add(hLabel);
		hPanel.add(hText);
		textBoxes.add(hPanel);
		
		JPanel vPanel = new JPanel();
		JLabel vLabel = new JLabel(String.format("%30s", "Vertical distance V [m]:"), SwingConstants.LEFT);
		JTextField vText = new JTextField(10);
		vPanel.add(vLabel);
		vPanel.add(vText);
		textBoxes.add(vPanel);
		
		JPanel tPanel = new JPanel();
		JLabel tLabel = new JLabel(String.format("%30s", "Top angle TA [deg]:"), SwingConstants.LEFT);
		JTextField tText = new JTextField(10);
		tPanel.add(tLabel);
		tPanel.add(tText);
		textBoxes.add(tPanel);
		
		JPanel lPanel = new JPanel();
		JLabel lLabel = new JLabel(String.format("%30s", "Length [m]:"), SwingConstants.LEFT);
		JTextField lText = new JTextField(10);
		lPanel.add(lLabel);
		lPanel.add(lText);
		textBoxes.add(lPanel);
		
		drawPanel = new DrawPanel();
		
		background = new JPanel();
		background.setLayout(new BorderLayout());
		background.add(BorderLayout.WEST, textBoxes);
		background.add(BorderLayout.CENTER, drawPanel);
		frame.getContentPane().add(background);
		frame.setSize(300, 200);
		frame.setVisible(true);
	}
}

class DrawPanel extends JPanel {
	public void paintComponent(Graphics gfx) {
		gfx.fillRect(0, 0, this.getWidth(), this.getHeight());
		int r = (int) (Math.random()*255);
		int g = (int) (Math.random()*255);
		int b = (int) (Math.random()*255);
		int x = (int) (Math.random()*(this.getWidth()-100));
		int y = (int) (Math.random()*(this.getHeight()-100));
		gfx.setColor(new Color(r, g, b));
		gfx.fillOval(x, y, 100, 100);
		int[] ptsX = {10, 150, 75, 25};
		int[] ptsY = {10, 25, 120, 80};
		gfx.fillPolygon(ptsX, ptsY, 4);
	}
}