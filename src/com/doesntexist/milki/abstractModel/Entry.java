/**
 * @author milki
 */
package com.doesntexist.milki.abstractModel;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class Entry.
 */
public class Entry implements Comparable<Entry>, Serializable{
	
	/** The valid. */
	private boolean valid;
	
	/** The account id. */
	private String accountId;
	
	/** The category id. */
	private String categoryId;
	
	/** The amount. */
	private double amount;
	
	/** The currency id. */
	private String currencyId;
	
	/** The remark. */
	private String remark;
	
	/** The date. */
	private Date date;
	
	/**
	 * Checks if is valid.
	 * @return true, if is valid
	 */
	public final boolean isValid() {
		return valid;
	}

	/**
	 * Sets the valid.
	 * @param valid the new valid
	 */
	public final void setValid(final boolean valid) {
		this.valid = valid;
	}

	/**
	 * Gets the account id.
	 * @return the account id
	 */
	public final String getAccountId() {
		return accountId;
	}

	/**
	 * Gets the category id.
	 * @return the category id
	 */
	public final String getCategoryId() {
		return categoryId;
	}

	/**
	 * Gets the amount.
	 * @return the amount
	 */
	public final double getAmount() {
		return amount;
	}

	/**
	 * Gets the currency id.
	 * @return the currency id
	 */
	public final String getCurrencyId() {
		return currencyId;
	}

	/**
	 * Gets the remark.
	 * @return the remark
	 */
	public final String getRemark() {
		return remark;
	}

	/**
	 * Gets the date.
	 * @return the date
	 */
	public final Date getDate() {
		return date;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setAmount(Double value) {
		this.amount = value;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Instantiates a new entry.
	 * 
	 * @param valid the valid
	 * @param accountId the account id
	 * @param categoryId the category id
	 * @param d the amount
	 * @param currencyId the currency id
	 * @param remark the remark
	 * @param date the date
	 */
	public Entry(final boolean valid, final String accountId, final String categoryId,
			final double d, final String currencyId, final String remark, final Date date) {
		this.valid = valid;
		this.accountId = accountId;
		this.categoryId = categoryId;
		this.amount = d;
		this.currencyId = currencyId;
		this.remark = remark;
		this.date = date;
	}

	/**
	 * Compare to.
	 * @param o the o
	 * @return the int
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public final int compareTo(final Entry o) {
		if (date.before(o.getDate())) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		return "" + ((Boolean)isValid()).toString() + "," + getAccountId() + "," + getCategoryId() + "," //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			+ getAmount() + "," + getCurrencyId() + "," + getRemark() + "," + getDate(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
}
