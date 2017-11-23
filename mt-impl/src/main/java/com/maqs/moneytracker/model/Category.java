package com.maqs.moneytracker.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.common.Constants;
import com.maqs.moneytracker.types.TransactionType;

@JsonInclude(Include.NON_NULL)
public class Category extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	public static final String DEFAULT_CAT = "Default";

	public static final String TRAN_TYPE = "transactionTypeCode";

	public static final String NAME = "name";

	public static final String PARENT_ID = "parentCategoryId";

	public static final String ID = "id";

	private String name;

	private String displayName;

	private Category parent;

	private Long parentCategoryId;

	private Account defaultAccount;

	private Long defaultAccountId;

	private TransactionType transactionType;

	private String transactionTypeCode;

	private boolean displayable = true;

	private List<Category> children;

	public Category() {
		this("Default");
	}

	public Category(String name) {
		this(name, TransactionType.valueOf(TransactionType.EXPENSE));
	}
	
	public Category(Long id, String name) {
		this.name = name;
		setId(id);
	}

	public Category(String name, TransactionType transactionType) {
		setName(name);
		setTransactionType(transactionType);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		updateDisplayName();
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
		if (parent != null) {
			updateDisplayName();
		}
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
		if (transactionType != null) {
			setTransactionTypeCode(transactionType.getCode());
		}
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public Account getDefaultAccount() {
		return defaultAccount;
	}

	public void setDefaultAccount(Account defaultAccount) {
		this.defaultAccount = defaultAccount;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	private void updateDisplayName() {
		String displayText = "";
		if (parent != null) {
			displayText += parent.getDisplayName() + "-";
		}
		if (name != null) {
			displayText += name;
		}

		setDisplayName(displayText);
	}

	public boolean isDisplayable() {
		return displayable;
	}

	public void setDisplayable(boolean displayable) {
		this.displayable = displayable;
	}

	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public Long getDefaultAccountId() {
		return defaultAccountId;
	}

	public void setDefaultAccountId(Long defaultAccountId) {
		this.defaultAccountId = defaultAccountId;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public void addChild(Category c) {
		if (children == null) {
			children = new ArrayList<Category>();
		}
		children.add(c);
	}

	public void removeChild(Category c) {
		children.remove(c);
	}

	public String getTransactionTypeCode() {
		return transactionTypeCode;
	}

	public void setTransactionTypeCode(String transactionTypeCode) {
		this.transactionTypeCode = transactionTypeCode;
		if (transactionTypeCode != null) {
			this.transactionType = TransactionType.valueOf(transactionTypeCode);
		}
	}

	@Override
	public String toString() {
		String text = Constants.EMPTY_STRING;
		if (getParent() != null) {
			text = getParent().toString() + Constants.HYPHEN;
		}
		text += getName();
		return text;
	}
	
}
