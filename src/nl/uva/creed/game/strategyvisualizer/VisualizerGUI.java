package nl.uva.creed.game.strategyvisualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class VisualizerGUI implements ActionListener {

	private JFrame frame = new JFrame();
	private JButton button = new JButton("Visualize");
	private String[] data = { "All C", "All D", "Tit for tat",
			"Tit for 2 tats", "Tat for tit", "Trigger", "Win Stay Lose Shift", "Custom",
			};
	private JComboBox commonStrategies = new JComboBox(data);
	private JTextArea customStrategy = new JTextArea();
	
	public VisualizerGUI() {
		super();

	}

	public void actionPerformed(ActionEvent action) {

		button.setText("I've been clicked");
	
	}

	public void go() {
		button.addActionListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(button);
		frame.getContentPane().add(commonStrategies);
		frame.getContentPane().add(customStrategy);
		
		
		//http://java.comsci.us/examples/awt/BorderLayout.html
		//http://java.comsci.us/examples/swing/
		frame.setSize(300, 300);
		frame.setVisible(true);
	}

}
