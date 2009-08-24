/**
 * @author milki
 */
package com.doesntexist.milki;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

// TODO: Auto-generated Javadoc
/**
 * The Class ExchangeRate.
 */
public class ExchangeRate implements Runnable {
	
	/** The delay. */
	private static int delay;
	
	/** The date. */
	private static String date = new String();
	
	/** The rate. */
	private static String rate = new String("(Loading...)");
	
	/** The amount str. */
	private static String amountStr = new String("100");
	
	/** The from str. */
	private static String fromStr = new String("CAD");
	
	/** The to str. */
	private static String toStr = new String("CNY");
	
//	public String getContent(String strUrl) {
//		try {
//			URL url = new URL(strUrl);
//			BufferedReader buf = new BufferedReader(
//					new InputStreamReader(url.openStream()));
//			String t = "";
//			StringBuffer sb = new StringBuffer("");
//			while ((t = buf.readLine()) != null) {
//				if (t.contains("CAD</td>")) {
//					String currency = t.substring(92,  t.lastIndexOf('<'));
//					sb.append(currency + " ");
//					buf.readLine();
//					buf.readLine();
//					t = buf.readLine();
//					String rate = t.substring(92,  t.lastIndexOf('<'));
//					sb.append(rate + " ");
//					buf.readLine();
//					t = buf.readLine();
//					String date = t.substring(93,  t.lastIndexOf('&'));
//					t = buf.readLine();
//					sb.append(date + " ");
//					date = t.substring(2, t.lastIndexOf('<'));
//					sb.append(date);
//					break;
//				}
//			}
//			buf.close();
//			return sb.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "Error: connection failed.";
//		}
//	}

	/**
	 * Gets the content.
	 * 
	 * @return the content
	 */
	public final String getContent() {
		try {
			URL url = new URL("http://www.xe.com/ucc/convert.cgi");
			System.setProperty("http.agent", "Mozilla/4.0");
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("Amount=" + amountStr + "&From=" + fromStr + "&To=" 
					+ toStr	+ "&image.x=16&image.y=11&image=Submit");
			out.flush();
			out.close();
			
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			InputStream lurlStream;
			lurlStream = connection.getInputStream();
			BufferedReader lReader = new BufferedReader(
					new InputStreamReader(lurlStream));
			while ((sCurrentLine = lReader.readLine()) != null) {
				if (sCurrentLine.contains("Live rates at")) {
					date = sCurrentLine.substring(
									sCurrentLine.indexOf("XEsmall") + 23,
									sCurrentLine.lastIndexOf("</span>"));
					lReader.readLine();
					lReader.readLine();
					lReader.readLine();
					sCurrentLine = lReader.readLine();
					rate = sCurrentLine.substring(
									sCurrentLine.indexOf("\"XE\">") + 5, 
									sCurrentLine.lastIndexOf(toStr + "<!--"));
					sTotalString = date + " " + rate;
					break;
				}
			}
			return sTotalString;
		} catch (Exception ex) {
			ex.printStackTrace();
			date = new String("0000.00.00 00:00:00 UTC");
			rate = new String("0");
			return "Error: connection failed.";
		}
	}
	
	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public static String getDate() {
		return date;
	}

	/**
	 * Gets the rate.
	 * 
	 * @return the rate
	 */
	public static String getRate() {
		return rate;
	}

	/**
	 * Gets the long string.
	 * 
	 * @return the long string
	 */
	public static String getLongString() {
		return amountStr + " " + fromStr + " = " + getRate() + toStr;
	}
	
	/**
	 * Instantiates a new exchange rate.
	 * 
	 * @param delay the delay
	 */
	public ExchangeRate(final int delay) {
		ExchangeRate.delay = delay;
	}
	
	/**
	 * The main method.
	 * 
	 * @param Args the arguments
	 */
	public static void main(final String[] args) {
		ExchangeRate er = new ExchangeRate(3000);
		Thread thread1 = new Thread(er);
		thread1.start();
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public final void run() {
		while (true) {
			try {
				Utilities.log(getContent());
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
