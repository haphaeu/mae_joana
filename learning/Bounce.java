import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Bounce implements MouseListener {
	MyDrawPanel panel;
	int waitTime=20;
	int x=150, y=150, vx=3, vy=2;
	final int size = 10;
	
	public static void main(String[] args) {
		Bounce bounce = new Bounce(); 
		bounce.setup();
	}
	public void setup() {
		JFrame frame = new JFrame();
		panel = new MyDrawPanel();
		frame.getContentPane().add(panel);
		frame.addMouseListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
		start();
	}	
	void start() {
		System.out.println(panel.getWidth() + " " + panel.getHeight());
		while (true) {
			if (x+vx > panel.getWidth()-size || x+vx < 0) {
				vx *= -1;
				System.out.print("vx " + vx);
				vx = perturbate(vx);
				System.out.println(" >> " + vx);
			} 
			if (y+vy > panel.getHeight()-size || y+vy < 0) {
				vy *= -1;
				System.out.print("vy " + vy);
				vy = perturbate(vy);
				System.out.println(" >> " + vy);
			}
			x += vx;
			y += vy;
			panel.repaint();
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException ex) {
				System.out.println("OW NO!");
			}
		}
	}
	int perturbate(int v) {
		if (Math.random() > 0.25) {
			v = (int) (Math.random() * 2 * v + Math.signum(v));
		}
		return v;
	}
	public void mouseClicked(MouseEvent ev) {
		int delta = 0;
		//System.out.println(ev.getButton());
		if (ev.getButton() == MouseEvent.BUTTON1) {
			delta = -1;
		} else if (ev.getButton() == MouseEvent.BUTTON3) {
			delta = 1;
		}
		waitTime += delta;
		if (waitTime < 0) {
			waitTime = 0;
		}
		System.out.println(waitTime);
	}
	public void mouseEntered(MouseEvent ev) {}
	public void mouseExited(MouseEvent ev) {}
	public void mousePressed(MouseEvent ev) {}
	public void mouseReleased(MouseEvent ev) {}
	
	class MyDrawPanel extends JPanel {
		public void paintComponent(Graphics gfx) {
			//gfx.fillRect(0, 0, this.getWidth(), this.getHeight());
			gfx.setColor(Color.red);
			gfx.fillOval(x, y, size, size);
		}
	}
}
