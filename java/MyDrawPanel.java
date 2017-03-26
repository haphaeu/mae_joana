import javax.swing.*;
import java.awt.*;
class MyDrawPanel extends JPanel {
	public static void main(String[] args) {
		MyDrawPanel gui = new MyDrawPanel(); 
		gui.go();
	}
	public void go() {
		JFrame frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
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