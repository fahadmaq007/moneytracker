package com.maqs.moneytracker.dto;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.maqs.moneytracker.types.AccountType;

public class AccountDto {

	public static final String TRAN_TYPE = "tranType";

	private BigDecimal amount;

	private String tranType;

	private String acctName;

	private Long catId;

	private BigDecimal expAmt;

	private BigDecimal incAmt;

	private String accountTypeCode = AccountType.CASH;

	public AccountDto() {
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}

	public BigDecimal getExpAmt() {
		return expAmt;
	}

	public void setExpAmt(BigDecimal expAmt) {
		this.expAmt = expAmt;
	}

	public BigDecimal getIncAmt() {
		return incAmt;
	}

	public void setIncAmt(BigDecimal incAmt) {
		this.incAmt = incAmt;
	}

	public String getAccountTypeCode() {
		return accountTypeCode;
	}

	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
