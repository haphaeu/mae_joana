import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;


public class Catenary {
	private JFrame frame;
	private JPanel background;
	private DrawPanel drawPanel;
	private final int TEXT_FIELD_COLUMNS = 5;
	
	public static void main(String[] args) {
		new Catenary().start();
	}
	void start() {
		frame = new JFrame("Catenary");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridLayout grid = new GridLayout(6, 2);
		grid.setVgap(1);
		grid.setHgap(2);
		JPanel textBoxes = new JPanel(grid);
		textBoxes.setAlignmentX(JPanel.LEFT_ALIGNMENT);

		JLabel hLabel = new JLabel(String.format("%30s", "Horizontal distance H [m]:"), SwingConstants.LEFT);
		JTextField hText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(hLabel);
		textBoxes.add(hText);

		JLabel vLabel = new JLabel(String.format("%30s", "Vertical distance V [m]:"), SwingConstants.LEFT);
		JTextField vText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(vLabel);
		textBoxes.add(vText);

		JLabel tLabel = new JLabel(String.format("%30s", "Top angle TA [deg]:"), SwingConstants.LEFT);
		JTextField tText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(tLabel);
		textBoxes.add(tText);

		JLabel lLabel = new JLabel(String.format("%30s", "Length [m]:"), SwingConstants.LEFT);
		JTextField lText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(lLabel);
		textBoxes.add(lText);
		
		JLabel mLabel = new JLabel(String.format("%30s", "MBR [m]:"), SwingConstants.LEFT);
		JTextField mText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(mLabel);
		textBoxes.add(mText);
		
		JButton btnCalc = new JButton("Calculate");
		JButton btnClear = new JButton("Clear");
		btnCalc.addActionListener(new CalculateListener());
		btnClear.addActionListener(new ClearListener());
		textBoxes.add(btnCalc);
		textBoxes.add(btnClear);
		
		HolderPanel drawPanelHolder = new HolderPanel(new BorderLayout());
		drawPanelHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		drawPanel = new DrawPanel();
		drawPanelHolder.add(drawPanel, BorderLayout.CENTER);
		
		background = new JPanel();
		background.setLayout(new BorderLayout());
		background.add(BorderLayout.WEST, textBoxes);
		background.add(BorderLayout.CENTER, drawPanelHolder);
		frame.getContentPane().add(background);
		frame.setLocation(200, 200);
		frame.setSize(500, 200);
		frame.setVisible(true);
	}
	
	/**
	* Listeners for the Calculate and Clear buttons
	*/
	class CalculateListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			//calculate();
			drawPanel.setIsDrawingReady(true);
			drawPanel.calcPoints();
			drawPanel.repaint();
		}
	}
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			//clear();
			drawPanel.setIsDrawingReady(false);
			drawPanel.repaint();
		}
	}
	/**
	* Draws the catenary shape.
	*/
	class DrawPanel extends JPanel {
		private final int NUM_PTS = 50;
		private int[] ptsX = new int[NUM_PTS];
		private int[] ptsY = new int[NUM_PTS];
		private boolean isDrawingReady;
		
		public DrawPanel() {
			super();
			this.addComponentListener(new DrawPanelResizeListener());
			isDrawingReady = false;
		}
		public void setIsDrawingReady(boolean b) {
			isDrawingReady = b;
		}
		void calcPoints() {
			System.out.println("recalculating pixels");
			float stepX = (float) this.getWidth() / NUM_PTS;
			float stepY = (float) this.getHeight() / NUM_PTS;
			for (int i=0; i < NUM_PTS; i++) {
				ptsX[i] = (int) stepX*i;
				ptsY[i] = (int) stepY*i;
			}
		}
		public void paintComponent(Graphics gfx) {
			System.out.println("redrawing canvas");
			int w = this.getWidth();
			int h = this.getHeight();
			gfx.setColor(Color.black);
			gfx.fillRect(0, 0, w, h);
			gfx.setColor(Color.white);
			if (isDrawingReady) gfx.drawPolyline(ptsX, ptsY, NUM_PTS);
		}
		// listener for resize events
		class DrawPanelResizeListener extends ComponentAdapter {
			public void componentResized(ComponentEvent e) {
				System.out.println("resizing window");
				if (isDrawingReady) calcPoints();
			}
		}
		
	}
	class HolderPanel extends JPanel {
		public HolderPanel(LayoutManager layout) {
			super(layout);
		}
		public void paintComponent(Graphics gfx) {
			int w = this.getWidth();
			int h = this.getHeight();
			gfx.setColor(Color.black);
			gfx.fillRect(0, 0, w, h);
		}
	}
}
