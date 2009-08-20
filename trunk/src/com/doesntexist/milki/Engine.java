package com.doesntexist.milki;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Timer;

public class Engine implements ActionListener {
	File fileNameData = new File("entryData.data");
	File fileNamePreference = new File("config.pref");
	private int optionGetExchangeRateDelay = 3000;
	
	private ArrayList<Entry> data = new ArrayList<Entry>();
	private ExchangeRate exchangeRate = new ExchangeRate();
	private Timer timer;
	private JCalendar calendar = new JCalendar();
	
	public Engine() {
		try {
			loadPreferences();
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
			log("Error loading file.");
			data.clear();
		}
		
		startTimer();
		
		try {
			saveData();
			savePreference();
		} catch (IOException e) {
			log("Error saving file.");
		}
	}
	
	private void loadPreferences() throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileNamePreference));
		optionGetExchangeRateDelay = in.readInt();
		in.close();
	}

	private void savePreference() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileNamePreference));
		out.writeInt(optionGetExchangeRateDelay);
		out.close();
	}

	private void loadData() throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileNameData));
		int n = in.readInt();
		for (int i = 0; i < n; i++) {
			Entry o = (Entry) in.readObject();
			data.add(o);
		}
		in.close();
	}
	
	private void saveData() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileNameData));
		out.writeInt(data.size());
		for (Entry e : data) {
			out.writeObject(e);
		}
		out.close();
	}

	private void sortData() {
		Collections.sort(data);
	}
	
	private void startTimer() {
		timer  = new Timer(optionGetExchangeRateDelay, this);
		timer.setInitialDelay(0);
		timer.start();
	}
	
	public void addEntry(Entry o) {
		data.add(o);
		sortData();
		try {
			saveData();
		} catch (IOException e) {
		}
	}
	
	public JCalendar getCalendar() {
		return calendar;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			log(exchangeRate.getContent());
		}
	}
	
	public void log(String words) {
		Calendar now=Calendar.getInstance();
		String time=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
		+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+ now.get(Calendar.SECOND);
		System.out.println("[" + time + "] " + words);
	}
}
