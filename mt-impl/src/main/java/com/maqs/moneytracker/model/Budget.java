package com.maqs.moneytracker.model;

import java.util.ArrayList;
import java.util.List;

public class Budget extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	private String name;

	private List<BudgetItem> items;

	public static final String BUDGET = "Budget - ";

	public static final String NAME = "name";

	public Budget() {
		
	}
	
	public Budget(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BudgetItem> getItems() {
		return items;
	}

	public void setItems(List<BudgetItem> items) {
		this.items = items;
	}

	public void addBudgetItem(BudgetItem item) {
		if (items == null) {
			items = new ArrayList<BudgetItem>();
		}
		items.add(item);
	}

	public void removeBudgetItem(BudgetItem item) {
		if (items == null) {
			logger.error("Cannot remove budget from an empty list");
			return;
		}
		items.remove(item);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
