package com.maqs.moneytracker.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.types.AccountType;

@JsonInclude(Include.NON_NULL)
public class Account extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	public static final String NAME = "name";

	public static final String ACCT_TYPE = "accountTypeCode";

	public static final String ID = "id";

	private String name;
	
	private BigDecimal balance = BigDecimal.ZERO;
	
	private AccountType accountType = AccountType.valueOf(AccountType.CASH);
	
	private String accountTypeCode = AccountType.CASH;
	
	private boolean displayable = true;
	
	private String displayName;
		
	public Account() {
		
	}
	
	public Account(String name) {
		setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		updateDisplayName();
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;		
	}
	
	public boolean isDisplayable() {
		return displayable;
	}
	
	public void setDisplayable(boolean displayable) {
		this.displayable = displayable;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getAccountTypeCode() {
		return accountTypeCode;
	}
	
	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
		setAccountType(AccountType.valueOf(accountTypeCode));
		updateDisplayName();
	}
	
	private void updateDisplayName() {
		String displayText = "";
		if (accountType != null) {
			displayText += accountType.getName() + "-";
		}
		if (name != null) {
			displayText += name;
		} 
		setDisplayName(displayText);
	}

	@Override
	public String toString() {
		return getName();
	}
}
