package com.maqs.moneytracker.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.maqs.moneytracker.types.Period;

public class BudgetItem extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	public static final String BUDGET_ID = "budgetId";

	public static final String CAT_ID = "categoryId";

	private Category category;

	private Long categoryId;

	private Long budgetId;

	private Period period;

	private String periodCode;

	private BigDecimal amount = BigDecimal.ZERO;

	private BigDecimal spent = BigDecimal.ZERO;
	
	private List<BudgetItem> children;
	
	public BudgetItem() {
		this(null);
	}

	public BudgetItem(Category category) {
		this.category = category;
		setPeriod(Period.valueOf(Period.MONTHLY));
		if (category != null) {
			this.categoryId = category.getId();
		}
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getSpent() {
		return spent;
	}
	
	public void setSpent(BigDecimal spent) {
		this.spent = spent;
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

	public Long getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(Long budgetId) {
		this.budgetId = budgetId;
	}

	public List<BudgetItem> getChildren() {
		return children;
	}
	
	public void setChildren(List<BudgetItem> children) {
		this.children = children;
	}
	
	public void addChild(BudgetItem item) {
		if (children == null) {
			children = new ArrayList<BudgetItem>();
		}
		children.add(item);
	}
	
	public void removeChild(BudgetItem item) {
		if (children == null) {
			return;
		}
		children.remove(item);
	}
	@Override
	public String toString() {
		return super.toString() + ": " + getCategory();
	}
}
