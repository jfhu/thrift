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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.doesntexist.milki.abstractModel.Account;
import com.doesntexist.milki.abstractModel.Category;
import com.doesntexist.milki.abstractModel.Entry;
import com.doesntexist.milki.abstractModel.EntryTableModel;

/**
 * The Class Engine.
 */
public class Engine {
	/* System settings */
	/** The file name data. */
	private File fileNameData = new File("entryData.data");
	
	/** The file name preference. */
	private File fileNamePreference = new File("config.pref");
	
	/* System Preferences */
	/** The option get exchange rate delay. */
	private int optionGetExchangeRateDelay = 30000;
	
	/* Data */
	/** The data entries. */
	private ArrayList<Entry> data = new ArrayList<Entry>();
	/** The Account list. */
	private ArrayList<Account> accountList = new ArrayList<Account>();
	/** The category list. */
	private ArrayList<Category> categoryList = new ArrayList<Category>();
	
	/* Functionality */
	/** The exchange rate. */
	private ExchangeRate exchangeRate = 
		new ExchangeRate(optionGetExchangeRateDelay);
	
	/** The thread exchange rate. */
	private Thread threadExchangeRate = new Thread(exchangeRate);
	
	/** The calendar. */
	private JCalendar calendar;
	
	/** The pie chart. */
	private PieChart pieChart;
	
	/** The table model. */
	private EntryTableModel entryTableModel;
	
	/* Methods */
	/**
	 * Instantiates a new engine.
	 */
	public Engine() {
		calendar = new JCalendar();
		pieChart = new PieChart();
		entryTableModel = new EntryTableModel();
		entryTableModel.setEngine(this);
		 
		try {
			loadPreferences();
		} catch (Exception e1) {
			e1.printStackTrace();
			Utilities.log("Error loading preferences.");
			accountList.clear();
			categoryList.clear();
			accountList.add(new Account("默认账户", 0));
			categoryList.add(new Category("默认分类"));
		} 
		try {
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
			Utilities.log("Error loading data.");
			data.clear();
		}

		
		
		try {
			savePreference();
			saveData();
		} catch (Exception e) {
			e.printStackTrace();
			Utilities.log("Error saving file.");
		}
		
		threadExchangeRate.start();
	}
	
	/**
	 * Load preferences.
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void loadPreferences() throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(
				new FileInputStream(fileNamePreference));
		optionGetExchangeRateDelay = in.readInt();
		//other options goes here
		
		/* Read Accounts Info */
		int n = in.readInt();
		for (int i = 0; i < n; i++) {
			Account o = (Account) in.readObject();
			accountList.add(o);
		}
		/* Read Category Info */
		n = in.readInt();
		for (int i = 0; i < n; i++) {
			Category o = (Category) in.readObject();
			categoryList.add(o);
		}
		/* Close Input Stream */
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
		//other options goes here
		
		/* Write Accounts Info */
		out.writeInt(accountList.size());
		for (Account ac : accountList) {
			out.writeObject(ac);
		}
		/* Read Category Info */
		out.writeInt(categoryList.size());
		for (Category ca : categoryList) {
			out.writeObject(ca);
		}
		/* Close Output Stream */
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
	public final void saveData() throws IOException {
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
	}
	
	/* getters */
	/**
	 * Gets the calendar.
	 * @return the calendar
	 */
	public final JCalendar getCalendar() {
		return calendar;
	}
	
	/**
	 * Gets the pie chart.
	 * @return the pie chart
	 */
	public final PieChart getPieChart() {
		return pieChart;
	}
	
	public EntryTableModel getEntryTableModel() {
		return entryTableModel;
	}

	
	/**
	 * Gets the exchange rate.
	 * @return the exchange rate
	 */
	public final String getExchangeRate() {
		return ExchangeRate.getRate();
	}
	
	/**
	 * Gets the exchange date.
	 * @return the exchange date
	 */
	public final String getExchangeDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ExchangeRate.getDate());
	}
	
	/**
	 * Gets the exchange rate long string.
	 * @return the exchange rate long string
	 */
	public final String getExchangeRateLongString() {
		return ExchangeRate.getLongString();
	}
	
	public ArrayList<Entry> getData() {
		return data;
	}

	public ArrayList<Account> getAccountList() {
		return accountList;
	}

	public ArrayList<Category> getCategoryList() {
		return categoryList;
	}

	public Entry checkFilterTextMatch(String filterText) {
		int countMatch1 = 0;
		String matchString = "";
		Entry o = new Entry(false, "", "", 0, "", "新增加的记录", new Date());
		if (filterText.equals("")) {
			return o;
		}
		for (Account ac : accountList) {
			if (ac.getId().contains(filterText)) {
				countMatch1++;
				matchString = ac.getId();
				Utilities.log("Account " + ac.getId() + " matches the string " + filterText + ".");
			}
		}
		if (countMatch1 == 1) {
			o.setAccountId(matchString);
		}
		
		int countMatch2 = 0;
		matchString = "";
		for (Category ca : categoryList) {
			if (ca.getId().contains(filterText)) {
				countMatch2++;
				matchString = ca.getId(); 
				Utilities.log("Category " + ca.getId() + " matches the string " + filterText + ".”");
			}
		}
		if (countMatch2 == 1) {
			o.setCategoryId(matchString);
		}
		
		if (countMatch1 == 0 && countMatch2 == 0) {
			o.setRemark(filterText);
		}
		return o;
	}
}
