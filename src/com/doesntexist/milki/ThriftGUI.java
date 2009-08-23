package com.doesntexist.milki;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ThriftGUI extends JFrame {
	private static String title = new String("Thrift - The simplest money logger on the earth.");
	private static final int width = 1000;
	private static final int height = 700;
	
	private JPanel dateSelector = new JPanel();
	private JPanel pieChart = new JPanel();
	private JPanel entryList = new JPanel();
	private JPanel statusBar = new JPanel();
	
	private static Engine engine;
	
	public ThriftGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
		setSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());
		
		setPanels();
		setMenus();
		
		setVisible(true);
	}

	private void setPanels() {
		dateSelector.add(engine.getCalendar());
		
		add(dateSelector);
	}
	
	private void setMenus() {
		
	}
	
	public static void createAndShowGUI() {
		engine = new Engine();
		new ThriftGUI();
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (System.getProperty("os.name").contains("Mac")) {
					Utilities.log("Change to Mac look and feel.");
					try {
						UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				createAndShowGUI();
			}
		});
	}
}
