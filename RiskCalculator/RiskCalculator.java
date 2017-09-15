import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import java.util.Locale;

/*
 * RiskCalculator
 * ==============
 *
 * Given an event duration and either the return period or the probability of
 * non-exceedence, calculates the risk (probability of ocurrence) of this event
 * for a given operation duration.
 *
 * Definitions:
 *
 *    T : event duration
 *    R : event return period
 *    pn: event probability of non-exceedence
 *    To: operation duration
 *    r : risk
 *
 * The risk is defined as:
 *
 *    r = 1 - (1 - 1/R)**To
 *
 * Other equations:
 *
 *    pn = 1 - T/R
 *
 *    R = T / (1 - pn)
 *
 * For example, a typical sea state event duration is 3h. 
 *
 * A 1-year return wave:
 *    R = (1 year) * (365 days/year) * (24 hours/day) = 8760 hours
 *
 * The probability of non-exceedence of a 1-year wave in one sea state (3h) is:
 *    pn = 99.97%
 *
 * For an operation that lasts for 2 days (To = 48 hours), the risk of exceeding
 * a 1-yr wave is:
 *    r = 1 - (1 - 1/8760)**48 = 0.55%
 *
 * Alternativelly, for a 99%-non-exceedence wave:
 *    pn = 99%
 *
 * The return period can be calculated as:
 *    R = 3 / (1- 0.99) = 300 h
 *
 * And the risk:
 *    r = 1 - (1 - 1/300)**48 = 15%
 *
 *
 * @author: Rafael Rossi
 * @date: 24.08.2017
 * 
 */
public class RiskCalculator {
	public static final String version = "v1.0";
	private JFrame frame;
	private JPanel background;
	private final int TEXT_FIELD_COLUMNS = 5;
	private JTextField eventDurationText;
	private JTextField eventRetPeriodText;
	private JTextField ProbNonExcText;
	private JTextField OperDurationText;
	private JTextField riskText;
	private JRadioButton hours;
	private JRadioButton days;
	private JRadioButton months;
	private JRadioButton years;

	private double eventDuration;
	private double eventReturnPeriod;
	private double probabilityNonExceed;
	private double operationDuration;
	private double risk;
	
	private boolean ready;
	
	public static void main(String[] args) {
		new RiskCalculator().start();
	}
	void start() {
		frame = new JFrame("Risk Calculator - " + version + " - by RR");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridLayout grid = new GridLayout(9, 2);
		grid.setVgap(1);
		grid.setHgap(2);
		JPanel textBoxes = new JPanel(grid);
		textBoxes.setAlignmentX(JPanel.LEFT_ALIGNMENT);

		JLabel eventDurationLabel = new JLabel(String.format("%30s", "Event duration [h]:"), SwingConstants.LEFT);
		eventDurationText = new JTextField(TEXT_FIELD_COLUMNS);
		eventDurationText.setToolTipText("Tipical sea state is 3h.");
		eventDurationLabel.setToolTipText(eventDurationText.getToolTipText());
		textBoxes.add(eventDurationLabel);
		textBoxes.add(eventDurationText);

		JLabel eventRetPeriodLabel = new JLabel(String.format("%30s", "Event return period:"), SwingConstants.LEFT);
		eventRetPeriodText = new JTextField(TEXT_FIELD_COLUMNS);
		eventRetPeriodText.setToolTipText("Return period of the design load. Choose unit using options below.");
		eventRetPeriodLabel.setToolTipText(eventRetPeriodText.getToolTipText());
		textBoxes.add(eventRetPeriodLabel);
		textBoxes.add(eventRetPeriodText);
		
		ButtonGroup units = new ButtonGroup();
		hours = new JRadioButton("hours");
		days = new JRadioButton("days");
		months = new JRadioButton("months");
		years = new JRadioButton("years", true);
		units.add(hours);
		units.add(days);
		units.add(months);
		units.add(years);
		textBoxes.add(hours);
		textBoxes.add(days);
		textBoxes.add(months);
		textBoxes.add(years);
		
		JLabel ProbNonExcLabel = new JLabel(String.format("%30s", "Probability non exceedence:"), SwingConstants.LEFT);
		ProbNonExcText = new JTextField(TEXT_FIELD_COLUMNS);
		ProbNonExcText.setToolTipText("For the design load. Leave blank if return period is given.");
		ProbNonExcLabel.setToolTipText(ProbNonExcText.getToolTipText());
		textBoxes.add(ProbNonExcLabel);
		textBoxes.add(ProbNonExcText);

		JLabel OperDurationLabel = new JLabel(String.format("%30s", "Operation duration [h]:"), SwingConstants.LEFT);
		OperDurationText = new JTextField(TEXT_FIELD_COLUMNS);
		OperDurationText.setToolTipText("Design life - planned duration of the exposure to loading.");
		OperDurationLabel.setToolTipText(OperDurationText.getToolTipText());
		textBoxes.add(OperDurationLabel);
		textBoxes.add(OperDurationText);
		
		JLabel riskLabel = new JLabel(String.format("%30s", "Risk:"), SwingConstants.LEFT);
		riskText = new JTextField(TEXT_FIELD_COLUMNS);
		riskText.setEditable(false);
		riskText.setToolTipText("Probability that 1 event exceeding design load occurs during design life.");
		riskLabel.setToolTipText(riskText.getToolTipText());
		textBoxes.add(riskLabel);
		textBoxes.add(riskText);
		
		JButton btnCalc = new JButton("Calculate");
		JButton btnClear = new JButton("Clear");
		JButton btnHelp = new JButton("Help");
		btnCalc.addActionListener(new CalculateListener());
		btnClear.addActionListener(new ClearListener());
		btnHelp.addActionListener(new HelpListener());
		textBoxes.add(btnCalc);
		textBoxes.add(btnClear);
		textBoxes.add(btnHelp);
		
		background = new JPanel();
		background.setLayout(new BorderLayout());
		background.add(BorderLayout.WEST, textBoxes);
		frame.getContentPane().add(background);
		frame.setLocation(200, 200);
		frame.setSize(360, 200);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	void getParams() {
		double eT, eR, pn, oT, r, eR_multi;
		
		try { eT = Double.parseDouble(eventDurationText.getText());  } catch (Exception ex) { eT  = 0; }
		try { eR = Double.parseDouble(eventRetPeriodText.getText()); } catch (Exception ex) { eR  = 0; }
		try { pn = Double.parseDouble(ProbNonExcText.getText());     } catch (Exception ex) { pn  = 0; }
		try { oT = Double.parseDouble(OperDurationText.getText());   } catch (Exception ex) { oT  = 0; }
		
		// converting return period to hours
		if      (hours.isSelected())  { eR_multi = 1.0;          }  
		else if (days.isSelected())   { eR_multi = 24.0;         }  
		else if (months.isSelected()) { eR_multi = 24.0 * 30.42; } 
		else if (years.isSelected())  { eR_multi = 24.0 * 365.0; } 
		else {
			System.out.println("Error in getParams(): unit selection.");
			ready = false;
			return;
		}			
		
		if      (eR > 0) { pn = 1.0 - eT/(eR * eR_multi); }
		else if (pn > 0) { eR = eT / (1 - pn) / eR_multi; }
		else {
			System.out.println("Error: either return period or probability must be set.");
			ready = false;
			return;
		}
		
		r = 1.0 - Math.pow(1.0 - 1.0 / (eR * eR_multi), oT);
		
		eventDuration = eT;
		eventReturnPeriod = eR;
		probabilityNonExceed = pn;
		operationDuration = oT;
		risk = r;
		
		ready = true;
		
	}

	void setParams() {
		if (ready) {
			eventDurationText.setText(String.format(Locale.ROOT, "%f", eventDuration));
			eventRetPeriodText.setText(String.format(Locale.ROOT, "%f",eventReturnPeriod));
			ProbNonExcText.setText(String.format(Locale.ROOT, "%f", probabilityNonExceed));
			OperDurationText.setText(String.format(Locale.ROOT, "%f",operationDuration));
			riskText.setText(String.format(Locale.ROOT, "%f", risk));
		} else {
			eventDurationText.setText("");
			eventRetPeriodText.setText("");
			ProbNonExcText.setText("");
			OperDurationText.setText("");
			riskText.setText("");
		}
	}
	
	/**
	* Listeners for the Calculate and Clear buttons
	*/
	class CalculateListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			getParams();
			setParams();
		}
	}
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			ready = false;
			setParams();
		}
	}
	class HelpListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			RiskCalcHelp help = new RiskCalcHelp();
		}
	}
	
	/****
	* Help information form
	*/
	class RiskCalcHelp {
		private JFrame frame;
		private JTextArea helpText;
		private JScrollPane scrolls;
		
		RiskCalcHelp() {
			frame = new JFrame("Risk Calculator Help");
			//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());

			String txt = "Risk Calculator Help\n\n" 
			           + "Calculates the risk of en event "
					   + "happening during an operation period.\n\n"

					   + "The risk can be defined as the probability of at least one "
					   + "event that exceeds design limits during the expected life of "
					   + "the structure, i.e, the complement of the probability that no "
					   + "events occur which exceed design limits. [1]\n\n"
					   + "r = 1 - (1 - 1/R)**Tref\n\n"
					   + "The relationship between the event duration, return period and "
					   + "probability of non-exceedence is:\n\n"
					   + "pn = 1 - T/R\n\n"
					   + "The inputs are the operation duration and either the "
					   + "event return period or its probability of non-exceedence "
					   + "and its duration.\n\n"
					   + "If the probability of non-exceedence and event duration are "
					   + "input, then the return period is back calculated. If only the "
					   + "return period is input, the event duration is required in order "
					   + "to back calculate the probability of non-exceedence.\n\n"
					   + "If both the return period and the probability of non-exceedence "
                       + "are given, only the return period is used and the later is re-calculated.\n\n"					   
					   + "Event and operation duration to be given in hours.\n\n"
					   + "The units of the return period to be choosen using the radio boxes.\n\n"
					   + "[1] https://en.wikipedia.org/wiki/Return_period#Risk_analysis\n";
					   
			helpText = new JTextArea(txt);
			helpText.setMargin(new Insets(5, 10, 5, 5)); // top, left, bottom, right
			//helpText.setFont(new Font("Serif", Font.ITALIC, 16));
			helpText.setEditable(false);
			helpText.setLineWrap(true);
			helpText.setWrapStyleWord(true);
			
			scrolls = new JScrollPane(helpText);
			
			frame.add(BorderLayout.CENTER, scrolls);
			//frame.getContentPane().add(helpText);

			frame.setLocation(250, 250);
			frame.setSize(400, 350);
			//frame.setResizable(false);
			frame.setVisible(true);
			
		}
		
	}
    
}
