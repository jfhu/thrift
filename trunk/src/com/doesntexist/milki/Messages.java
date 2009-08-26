package com.doesntexist.milki;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
	private static ResourceBundle resourceBundle;
	
	public Messages() {
//		Locale.setDefault(new Locale("en", "US")); //$NON-NLS-1$ //$NON-NLS-2$
		Locale locale = Locale.getDefault();
		Utilities.log("Current Language is " + locale.getCountry() + "_" + locale.getLanguage()); //$NON-NLS-1$ //$NON-NLS-2$
		resourceBundle = ResourceBundle.getBundle("thrift", locale); //$NON-NLS-1$
	}
	
	public static String getString(String key) {
		return resourceBundle.getString(key);
	}
}
