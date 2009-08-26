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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * The Class ExchangeRate.
 */
public class ExchangeRate implements Runnable {
	
	/** The delay. */
	private static int delay;
	
	/** The rate string. */
	private static String rateStr = new String(Messages.getString("ExchangeRate.Loading")); //$NON-NLS-1$
	
	/** The date string. */
	private static String dateStr = new String("0000.00.00 00:00:00"); //$NON-NLS-1$
	
	/** The date. */
	private static Date date = Calendar.getInstance().getTime();
	
	/** The amount string. */
	private static String amountStr = new String("100"); //$NON-NLS-1$
	
	/** The from string. */
	private static String fromStr = new String("CAD"); //$NON-NLS-1$
	
	/** The to string. */
	private static String toStr = new String("CNY"); //$NON-NLS-1$
	
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
	 * @return the content
	 */
	public final String getContent() {
		try {
			URL url = new URL("http://www.xe.com/ucc/convert.cgi"); //$NON-NLS-1$
			System.setProperty("http.agent", "Mozilla/4.0"); //$NON-NLS-1$ //$NON-NLS-2$
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("Amount=" + amountStr + "&From=" + fromStr + "&To="  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ toStr	+ "&image.x=16&image.y=11&image=Submit"); //$NON-NLS-1$
			out.flush();
			out.close();
			
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = ""; //$NON-NLS-1$
			sTotalString = ""; //$NON-NLS-1$
			InputStream lurlStream;
			lurlStream = connection.getInputStream();
			BufferedReader lReader = new BufferedReader(
					new InputStreamReader(lurlStream));
			while ((sCurrentLine = lReader.readLine()) != null) {
				if (sCurrentLine.contains("Live rates at")) { //$NON-NLS-1$
					dateStr = sCurrentLine.substring(
									sCurrentLine.indexOf("XEsmall") + 23, //$NON-NLS-1$
									sCurrentLine.lastIndexOf(" UTC</span>")); //$NON-NLS-1$
					lReader.readLine();
					lReader.readLine();
					lReader.readLine();
					sCurrentLine = lReader.readLine();
					rateStr = sCurrentLine.substring(
									sCurrentLine.indexOf("\"XE\">") + 5, //$NON-NLS-1$
									sCurrentLine.lastIndexOf(toStr + "<!--")); //$NON-NLS-1$
					sTotalString = dateStr + " " + rateStr; //$NON-NLS-1$
					break;
				}
			}
			
			/* date string -> Date in local time zone */
			SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"); //$NON-NLS-1$
			Calendar cal = Calendar.getInstance();
			Calendar calUTC;
			TimeZone oldTimeZone = cal.getTimeZone();
			try {
				cal.setTime(df.parse(dateStr));
				cal.setTimeZone(TimeZone.getTimeZone("UTC")); //$NON-NLS-1$
				calUTC = (Calendar) cal.clone();
				cal.setTimeZone(oldTimeZone);
				calUTC.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
						cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
				calUTC.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
				cal.getTime();
				date = calUTC.getTime();
			} catch (ParseException e) {
				Utilities.log("Error parsing date."); //$NON-NLS-1$
			} 
			
			/* return for printing */
			return sTotalString;
		} catch (Exception ex) {
			ex.printStackTrace();
			dateStr = new String(Messages.getString("ExchangeRate.UnknownDate")); //$NON-NLS-1$
			rateStr = new String(Messages.getString("ExchangeRate.UnknownRate")); //$NON-NLS-1$
			return "Error: connection failed."; //$NON-NLS-1$
		}
	}
	/**
	 * Gets the date.
	 * @return the date
	 */
	public static Date getDate() {
		return date;
	}

	/**
	 * Gets the rate.
	 * @return the rate
	 */
	public static String getRate() {
		return rateStr;
	}

	/**
	 * Gets the long string.
	 * @return the long string
	 */
	public static String getLongString() {
		return amountStr + " " + fromStr + " = " + getRate() + toStr; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Instantiates a new exchange rate.
	 * @param delay the delay
	 */
	public ExchangeRate(final int delay) {
		ExchangeRate.delay = delay;
	}
	
	/**
	 * The main method.
	 * @param args the arguments
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
				getContent();
				Utilities.log(getDate() + " " + getLongString()); //$NON-NLS-1$
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
