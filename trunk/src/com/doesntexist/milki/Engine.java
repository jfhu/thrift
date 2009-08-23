package com.doesntexist.milki;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Engine {
	File fileNameData = new File("entryData.data");
	File fileNamePreference = new File("config.pref");
	private int optionGetExchangeRateDelay = 30000;
	
	private ArrayList<Entry> data = new ArrayList<Entry>();
	private ExchangeRate exchangeRate = new ExchangeRate(optionGetExchangeRateDelay);
	Thread threadExchangeRate = new Thread(exchangeRate);
	private JCalendar calendar;
	
	public Engine() {
		calendar = new JCalendar();
		 
		try {
			loadPreferences();
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
			Utilities.log("Error loading file.");
			data.clear();
		}
		
		threadExchangeRate.start();
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
}
