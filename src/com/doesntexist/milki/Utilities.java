/**
 * @author milki
 */
package com.doesntexist.milki;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Utilities.
 */
public final class Utilities {
	
	/**
	 * No instantiating a new utilities.
	 */
	private Utilities() {
	}
	
	/**
	 * Log.
	 * @param words the message
	 */
	public static void log(final Object words) {
		Date current = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); //$NON-NLS-1$
		String time = format.format(current);
		System.out.println("[" + time + "] " +  words); //$NON-NLS-1$ //$NON-NLS-2$
	}
}

