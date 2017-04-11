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
	public static final String version = "v0.1beta";
	private JFrame frame;
	private JPanel background;
	private DrawPanel drawPanel;
	private final int TEXT_FIELD_COLUMNS = 5;
	private JTextField tText;
	private JTextField vText;
	private JTextField hText;
	private JTextField lText;
	private JTextField mText;
	private CatenaryParameters cat;
	
	public static void main(String[] args) {
		new Catenary().start();
	}
	void start() {
		frame = new JFrame("Catenary - " + version);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridLayout grid = new GridLayout(6, 2);
		grid.setVgap(1);
		grid.setHgap(2);
		JPanel textBoxes = new JPanel(grid);
		textBoxes.setAlignmentX(JPanel.LEFT_ALIGNMENT);

		JLabel tLabel = new JLabel(String.format("%30s", "Top angle TA [deg]:"), SwingConstants.LEFT);
		tText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(tLabel);
		textBoxes.add(tText);

		JLabel vLabel = new JLabel(String.format("%30s", "Vertical distance V [m]:"), SwingConstants.LEFT);
		vText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(vLabel);
		textBoxes.add(vText);
		
		JLabel hLabel = new JLabel(String.format("%30s", "Horizontal distance H [m]:"), SwingConstants.LEFT);
		hText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(hLabel);
		textBoxes.add(hText);

		JLabel lLabel = new JLabel(String.format("%30s", "Length [m]:"), SwingConstants.LEFT);
		lText = new JTextField(TEXT_FIELD_COLUMNS);
		textBoxes.add(lLabel);
		textBoxes.add(lText);
		
		JLabel mLabel = new JLabel(String.format("%30s", "MBR [m]:"), SwingConstants.LEFT);
		mText = new JTextField(TEXT_FIELD_COLUMNS);
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
	
	void getParams() {
		// SOLUCAO TEMPORARIA
		// AQUI PRECISA CHECAR QUAIS PARAMETROS FORAM ENTRADOS
		// E CHAMAR A FUNCAO CERTA...
		// TEMP:
		double ta, vd, hd, l, mbr;
		try { ta  = Double.parseDouble(tText.getText()); } catch (Exception ex) { ta  = 0; }
		try { vd  = Double.parseDouble(vText.getText()); } catch (Exception ex) { vd  = 0; }
		try { hd  = Double.parseDouble(hText.getText()); } catch (Exception ex) { hd  = 0; }
		try { l   = Double.parseDouble(lText.getText()); } catch (Exception ex) { l   = 0; }
		try { mbr = Double.parseDouble(mText.getText()); } catch (Exception ex) { mbr = 0; }

		cat = CatenaryLib.CatenaryCalcTAV(ta, vd);
	}
	void setParams() {
		double[] params = cat.getParameters();
		tText.setText(String.valueOf(params[0]));
		vText.setText(String.valueOf(params[1]));
		hText.setText(String.valueOf(params[2]));
		lText.setText(String.valueOf(params[3]));
		mText.setText(String.valueOf(params[4]));
	}
	/**
	* Listeners for the Calculate and Clear buttons
	*/
	class CalculateListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			getParams();
			setParams();
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
