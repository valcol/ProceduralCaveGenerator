package com.valcol.pcg;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class UI implements ActionListener, ChangeListener  { 
	
	
	private static JFrame frame = new JFrame("PCG");
	private static JPanel panel = new JPanel();
	JTextField seedField;
	
	private Map map;
	
	public UI(long seed, int waterLevel ){
		
		map = new Map(seed, waterLevel);
		frame.setSize(1080, 850);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Graph(map));
		frame.setLayout(null);
		frame.validate();
		
	
		frame.add(new Graph(map));
		frame.add(getPanel());

		frame.setResizable(true);
		frame.validate();
		  
		frame.setVisible(true);
	}
	
	public JPanel getPanel(){
		
		panel.setFocusable(true);
		panel.setLocation(0, 760);
		panel.setSize(850, 180);
		panel.setVisible(true);
		panel.setLayout(new FlowLayout());
		
		JButton b1=(new JButton("Random seed"));
		b1.setActionCommand("r");
		b1.addActionListener(this);
		
		JButton b2=(new JButton("Set Seed"));
		b2.setActionCommand("s");
		b2.addActionListener(this);
		
		JLabel seedLabel = new JLabel();
		seedLabel.setText("Seed(float):");
		
		JLabel wLabel = new JLabel();
		wLabel.setText("          Water Level:");
		
		seedField = new JTextField(String.valueOf(map.getSeed()), 10);
		seedField.addActionListener(this);

		JSlider wlevel = new JSlider(JSlider.HORIZONTAL,
                1, 15, 6);
		wlevel.addChangeListener(this);
		
		panel.add(seedLabel);
		panel.add(seedField);
		panel.add(b2);
		panel.add(b1);
		panel.add(wLabel);
		panel.add(wlevel);
		
		return panel;
	}
	
	
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        int wl = (int)source.getValue();
	        map.setWaterLevel(wl);
	        refresh();
	    }
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if ("r".equals(e.getActionCommand())) 
			map.setSeed(Double.doubleToLongBits(Math.random()));
		else
			map.setSeed(Long.parseLong(seedField.getText()));
		refresh();
	}
	 
	 public void refresh() {
		 map.setMap(map.createMap());
		 frame.invalidate();
		 frame.repaint();
	}
	 
}
