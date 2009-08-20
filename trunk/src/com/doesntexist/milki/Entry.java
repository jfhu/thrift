package com.doesntexist.milki;

import java.util.Date;

public class Entry implements Comparable<Entry>{
	private boolean valid;
	private String accountId;
	private String categoryId;
	private int amount;
	private String currencyId;
	private String remark;
	private Date date;
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public int getAmount() {
		return amount;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public String getRemark() {
		return remark;
	}

	public Date getDate() {
		return date;
	}

	public Entry(boolean valid, String accountId, String categoryId,
			int amount, String currencyId, String remark, Date date) {
		this.valid = valid;
		this.accountId = accountId;
		this.categoryId = categoryId;
		this.amount = amount;
		this.currencyId = currencyId;
		this.remark = remark;
		this.date = date;
	}

	public int compareTo(Entry o) {
		if (date.before(o.getDate()))
			return 1;
		else
			return 0;
	}
	
}
