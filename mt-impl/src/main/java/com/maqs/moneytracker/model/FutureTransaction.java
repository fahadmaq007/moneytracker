package com.maqs.moneytracker.model;

import com.maqs.moneytracker.types.Period;

public class FutureTransaction extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	public static final String TRAN_ID = "transactionId";

	private Transaction transaction;

	private Long transactionId;

	private Period period;

	private String periodCode;

	private Integer howMany;

	public FutureTransaction() {
		this(Period.valueOf(Period.MONTHLY));
	}

	public FutureTransaction(Period period) {
		setPeriod(Period.valueOf(Period.MONTHLY));
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
		if (period != null) {
			setPeriodCode(period.getCode());
		}
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getHowMany() {
		return howMany;
	}

	public void setHowMany(Integer howMany) {
		this.howMany = howMany;
	}

	@Override
	public String toString() {
		return getTransaction() + " for " + getHowMany() + " " + getPeriodCode();
	}
}
