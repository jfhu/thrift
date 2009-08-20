package com.doesntexist.milki;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Engine implements ActionListener {
	private int optionGetExchangeRateInterval = 30000;
	
	private ArrayList<Entry> data;
	private ExchangeRate exchangeRate = new ExchangeRate();
	private Timer timer;
	private JCalendar calendar = new JCalendar();
	
	public Engine() {
		getData();
		timer  = new Timer(optionGetExchangeRateInterval, this);
		timer.setInitialDelay(0);
		timer.start();
	}
	
	private void getData() {
		
	}

	public JCalendar getCalendar() {
		return calendar;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			System.out.println(exchangeRate.getContent());
		}
	}
}
