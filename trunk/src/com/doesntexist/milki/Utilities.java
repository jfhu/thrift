package com.doesntexist.milki;

import java.util.Calendar;

public class Utilities {
	public static void log(String words) {
		Calendar now=Calendar.getInstance();
		String time=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
		+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+ now.get(Calendar.SECOND);
		System.out.println("[" + time + "] " +  words);
	}
}

