package com.maqs.moneytracker.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.types.RecurringType;

@JsonInclude(Include.NON_NULL)
public class Transaction extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7786679565872835789L;

	public static final String ID = "id";
	
	public static final String CAT_ID = "categoryId";

	public static final String RECURRING = "recurring";

	public static final String ON_DATE = "onDate";

	public static final String CHECKSUM = "checksum";

	public static final String DATE_STRING = "dateString";

	private Date onDate;

	private BigDecimal amount;

	private BigDecimal prevAmount;
	
	private Category category;

	private Long categoryId;

	private Long accountId;
	
	private Long fromAccountId;
	
	private Account account;

	private Account fromAccount;
	
	private String description;

	private String recurringWhen;
	
	private boolean recurring;

	private RecurringType recurringType;
	
	private String checksum;
	
	private String originalChecksum;
		
	private String dateString;
	
	public Transaction() {
		onDate = getCreatedOn();
		amount = BigDecimal.ZERO;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
		if (category != null) {
			setCategoryId(category.getId());
		}
		if (category != null && account == null) {
			setAccount(category.getDefaultAccount());
		}
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		if (account != null) {
			setAccountId(account.getId());
		}
	}
 
	public Date getOnDate() {
		return onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getPrevAmount() {
		return prevAmount;
	}

	public void setPrevAmount(BigDecimal prevAmount) {
		this.prevAmount = prevAmount;
	}

	public String getRecurringWhen() {
		return recurringWhen;
	}

	public void setRecurringWhen(String recurringWhen) {
		this.recurringWhen = recurringWhen;
	}

	public boolean isRecurring() {
		return recurring;
	}

	public void setRecurring(boolean recurring) {
		this.recurring = recurring;
	}

	public RecurringType getRecurringType() {
		return recurringType;
	}
	
	public void setRecurringType(RecurringType recurringType) {
		this.recurringType = recurringType;
		if (this.recurringType != null) {
			setRecurring(true);
			setRecurringWhen(recurringType.getCode());
		}
	}
	
	public Account getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}
	
	public Long getFromAccountId() {
		return fromAccountId;
	}
	
	public void setFromAccountId(Long fromAccountId) {
		this.fromAccountId = fromAccountId;
	}
	
	public String getChecksum() {
		return checksum;
	}
	
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	public String getOriginalChecksum() {
		return originalChecksum;
	}
	
	public void setOriginalChecksum(String originalChecksum) {
		this.originalChecksum = originalChecksum;
	}

	public String getDateString() {
		return dateString;
	}
	
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
}
