import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingComponentsExample implements ActionListener {
	
	JTextField ltext;
	JTextArea text;
	JCheckBox check;
	JList list;
	
	public static void main(String[] args) {
		SwingComponentsExample gui = new SwingComponentsExample();
		gui.go();
	}
	
	public void go() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel fieldPanel = new JPanel();
		JLabel label = new JLabel("This is a JTextField:");
		ltext = new JTextField(20);
		fieldPanel.add(label);
		fieldPanel.add(ltext);
		
		check = new JCheckBox("This is a JCheckBox");
		mainPanel.add(check);
		
		list = new JList(new String[] {"one", "two", "three", "four", "five"});
		list.setVisibleRowCount(3);
		JScrollPane scroller2 = new JScrollPane(list);
		scroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(scroller2);
		
		text = new JTextArea(10, 20);
		text.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(scroller);
		
		JButton button = new JButton("Click it");
		button.addActionListener(new ButtonListener());
		
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, button);
		frame.setSize(350, 300);
		frame.setVisible(true);
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event){
			text.append("button clicked\n");
			text.requestFocus();
		}
	}
}