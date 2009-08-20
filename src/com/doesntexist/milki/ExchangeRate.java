package com.doesntexist.milki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;



public class ExchangeRate {
	private String date = new String();
	private String rate = new String();
	
//	public String getContent(String strUrl) {
//		try {
//			URL url = new URL(strUrl);
//			BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));
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

	public String getContent() {
		try {
			URL url = new URL("http://www.xe.com/ucc/convert.cgi");
			System.setProperty("http.agent", "Mozilla/4.0");
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1");
			out.write("Amount=100&From=CAD&To=CNY&image.x=16&image.y=11&image=Submit");
			out.flush();
			out.close();
			
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			InputStream l_urlStream;
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				if (sCurrentLine.contains("Live rates at")) {
					date = sCurrentLine.substring(
												sCurrentLine.indexOf("XEsmall") + 23,
												sCurrentLine.lastIndexOf("</span>"));
					l_reader.readLine();
					l_reader.readLine();
					l_reader.readLine();
					sCurrentLine = l_reader.readLine();
					rate = sCurrentLine.substring(
												sCurrentLine.indexOf("\"XE\">") + 5, 
												sCurrentLine.lastIndexOf("CNY<!--"));
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
	
	public String getDate() {
		return date;
	}

	public String getRate() {
		return rate;
	}

	public ExchangeRate() {
	}
	
	public static void main(String[] Args) {
		ExchangeRate er = new ExchangeRate(); 
		System.out.println(er.getContent());
	}
}
