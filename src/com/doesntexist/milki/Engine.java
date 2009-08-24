/**
 * @author milki
 */
package com.doesntexist.milki;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The Class Engine.
 */
public class Engine {
	
	/** The file name data. */
	private File fileNameData = new File("entryData.data");
	
	/** The file name preference. */
	private File fileNamePreference = new File("config.pref");
	
	/** The option get exchange rate delay. */
	private int optionGetExchangeRateDelay = 30000;
	
	/** The data. */
	private ArrayList<Entry> data = new ArrayList<Entry>();
	
	/** The exchange rate. */
	private ExchangeRate exchangeRate = 
		new ExchangeRate(optionGetExchangeRateDelay);
	
	/** The thread exchange rate. */
	private Thread threadExchangeRate = new Thread(exchangeRate);
	
	/** The calendar. */
	private JCalendar calendar;
	
	/**
	 * Instantiates a new engine.
	 */
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
	
	/**
	 * Load preferences.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void loadPreferences() throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(
				new FileInputStream(fileNamePreference));
		optionGetExchangeRateDelay = in.readInt();
		in.close();
	}

	/**
	 * Save preference.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void savePreference() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(fileNamePreference));
		out.writeInt(optionGetExchangeRateDelay);
		out.close();
	}

	/**
	 * Load data.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void loadData() throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(
				new FileInputStream(fileNameData));
		int n = in.readInt();
		for (int i = 0; i < n; i++) {
			Entry o = (Entry) in.readObject();
			data.add(o);
		}
		in.close();
	}
	
	/**
	 * Save data.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void saveData() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(fileNameData));
		out.writeInt(data.size());
		for (Entry e : data) {
			out.writeObject(e);
		}
		out.close();
	}

	/**
	 * Sort data.
	 */
	private void sortData() {
		Collections.sort(data);
	}
	
	/**
	 * Adds the entry.
	 * 
	 * @param o the o
	 */
	public final void addEntry(final Entry o) {
		data.add(o);
		sortData();
		try {
			saveData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//getters
	/**
	 * Gets the calendar.
	 * 
	 * @return the calendar
	 */
	public final JCalendar getCalendar() {
		return calendar;
	}
	
	/**
	 * Gets the exchange rate.
	 * 
	 * @return the exchange rate
	 */
	public final String getExchangeRate() {
		return ExchangeRate.getRate();
	}
	
	/**
	 * Gets the exchange date.
	 * 
	 * @return the exchange date
	 */
	public final String getExchangeDate() {
		return ExchangeRate.getDate();
	}
	
	/**
	 * Gets the exchange rate long string.
	 * 
	 * @return the exchange rate long string
	 */
	public final String getExchangeRateLongString() {
		return ExchangeRate.getLongString();
	}
}
