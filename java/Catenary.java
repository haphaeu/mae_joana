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

import static java.lang.Math.cosh;

public class Catenary {
	public static final String version = "v0.2";
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
	private double[] normalisedPointsX;
	private double[] normalisedPointsY;
	
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
		double ta, vd, hd, l, mbr;
		try { ta  = Double.parseDouble(tText.getText()); } catch (Exception ex) { ta  = 0; }
		try { vd  = Double.parseDouble(vText.getText()); } catch (Exception ex) { vd  = 0; }
		try { hd  = Double.parseDouble(hText.getText()); } catch (Exception ex) { hd  = 0; }
		try { l   = Double.parseDouble(lText.getText()); } catch (Exception ex) { l   = 0; }
		try { mbr = Double.parseDouble(mText.getText()); } catch (Exception ex) { mbr = 0; }
		
		if      (ta > 0 && vd  > 0) { cat = CatenaryLib.CatenaryCalcTAV(ta, vd); }
		else if (ta > 0 && hd  > 0) { cat = CatenaryLib.CatenaryCalcTAH(ta, hd); }
		else if (ta > 0 && l   > 0) { cat = CatenaryLib.CatenaryCalcTAL(ta, l); }
		else if (ta > 0 && mbr > 0) { cat = CatenaryLib.CatenaryCalcTAMBR(ta, mbr); }
		else if (vd > 0 && hd  > 0) { cat = CatenaryLib.CatenarySolveV_HLMBR(vd, hd, "H");}
		else if (vd > 0 && l   > 0) { cat = CatenaryLib.CatenarySolveV_HLMBR(vd, l, "L");}
		else if (vd > 0 && mbr > 0) { cat = CatenaryLib.CatenarySolveV_HLMBR(vd, mbr, "MBR");}
		else if (hd > 0 && l   > 0) { cat = CatenaryLib.CatenarySolveH_LMBR(hd, l, "L"); }
		else if (hd > 0 && mbr > 0) { cat = CatenaryLib.CatenarySolveH_LMBR(hd, mbr, "MBR"); }
		else if (l  > 0 && mbr > 0) { cat = CatenaryLib.CatenarySolveL_MBR(l, mbr); }
		else { cat = null; }
	}
	void setParams() {
		if (cat != null) {
			double[] params = cat.getParameters();
			tText.setText(String.valueOf(params[0]));
			vText.setText(String.valueOf(params[1]));
			hText.setText(String.valueOf(params[2]));
			lText.setText(String.valueOf(params[3]));
			mText.setText(String.valueOf(params[4]));
		} else {
			tText.setText("");
			vText.setText("");
			hText.setText("");
			lText.setText("");
			mText.setText("");
		}
	}
	
	void calculateNormalisedPoints() {
	//Calculate the catenary points in the input coordinates"""

		int numPts = drawPanel.getNumPts();
		
		double[] params = cat.getParameters();
		double ta = params[0];  // not used
		double vd = params[1];  
		double hd = params[2];
		double l = params[3];   // not used
		double mbr = params[4]; 
		
		double Xmin = 0.0;
		double Xmax = hd;
		double xstep = (Xmax - Xmin) / (numPts - 1.0);
		double[] X = new double[numPts];
		double[] Y = new double[numPts]; 
		normalisedPointsX = new double[numPts];
		normalisedPointsY = new double[numPts];
		double Ymin = mbr;
		double Ymax = mbr + vd;
		double rangeX = Xmax - Xmin;
		double rangeY = Ymax - Ymin;

		for (int i=0; i < numPts; i++) {
			// points in catenary scale
			X[i] = Xmin + i * xstep;
			Y[i] = mbr * cosh(X[i] / mbr);
			// normalised points, from 0 to 1
			// these will be resized to draw in canvas
			normalisedPointsX[i] = (X[i] - Xmin) / rangeX;
			normalisedPointsY[i] = -(Y[i] - Ymax) / rangeY;
		}
	}
	
	/**
	* Listeners for the Calculate and Clear buttons
	*/
	class CalculateListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			getParams();
			setParams();
			if (cat != null) {
				calculateNormalisedPoints();
				drawPanel.setIsDrawingReady(true);
				drawPanel.calcPoints();
				drawPanel.repaint();
			}
		}
	}
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			tText.setText("");
			vText.setText("");
			hText.setText("");
			lText.setText("");
			mText.setText("");
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
		public int getNumPts() {
			return NUM_PTS;
		}
		void calcPoints() {
			// """Calculate the points to be drawn in the canvas
            //The aspect ratio of the real catenary is preserved
			System.out.println("recalculating pixels");
			int w = this.getWidth();
			int h = this.getHeight();
			
			double[] params = cat.getParameters();
			double ta = params[0];  // not used
			double vd = params[1];  
			double hd = params[2];
			double l = params[3];   // not used
			double mbr = params[4]; // not used
			
			// Calculate aspect ratio of the Catenary and the Canvas
			float CatenaryAspectRatio = (float) (hd / vd);
			float CanvasAspectRatio = (float) w / h;
			
			// Calculate the multiplication and shift to be applied to the points
			// so that the fit the canvas keeping the original catenary aspect ratio
			float Xfactor;
			float Yfactor;
			float Xshift;
			float Yshift;
			if (CatenaryAspectRatio > CanvasAspectRatio) {
				Xfactor = 1.0f;
				Xshift = 0.0f;
				Yfactor = CanvasAspectRatio / CatenaryAspectRatio;
				Yshift = (1.0f - Yfactor) * (float) h / 2.0f;
			} else if (CatenaryAspectRatio < CanvasAspectRatio) {
				Xfactor = CatenaryAspectRatio / CanvasAspectRatio;
				Xshift = (1.0f - Xfactor) * (float) w / 2.0f;
				Yfactor = 1.0f;
				Yshift = 0.0f;
			} else {
				Xfactor = 1.0f;
				Xshift = 0.0f;
				Yfactor = 1.0f;
				Yshift = 0.0f;
			}

			System.out.println("Canvas w x h: " + w + ", " + h);
			System.out.println("Canvas aspect ratio: " + CanvasAspectRatio);
			System.out.println("Catenary aspect ratio: " + CatenaryAspectRatio);
			System.out.println("X and Y factors: " + Xfactor + ", " + Yfactor);
			System.out.println("X and Y shifts: " + Xshift + ", " + Yshift);
			// and finally, calculates the points to be drawn in the canvas
			// by multipling the normalized points to the factors and shifts
			// allocate memory for the points to plot
			for (int i=0; i < NUM_PTS; i++) {
				//System.out.println("norm points X and Y: " + normalisedPointsX[i] + ", " + normalisedPointsY[i]);
				ptsX[i] = (int) (normalisedPointsX[i] * w * Xfactor + Xshift);
				ptsY[i] = (int) (normalisedPointsY[i] * h * Yfactor + Yshift);
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
