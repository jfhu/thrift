package com.doesntexist.milki;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class ThriftGUI extends JFrame {
	private static String title = new String("Thrift - The simplest money logger on the earth.");
	private static final int width = 1000;
	private static final int height = 700;
	
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
	
	}
	
	private void setMenus() {
		
	}
	
	public static void createAndShowGUI() {
		@SuppressWarnings("unused")
		ThriftGUI main = new ThriftGUI();
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
