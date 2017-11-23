package com.maqs.moneytracker.dto;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.model.BudgetItem;

@JsonInclude(Include.NON_NULL)
public class BudgetReportDto {

	private Long budgetId;

	private List<BudgetItem> budgetItems;

	public BudgetReportDto() {

	}

	public Long getBudgetId() {
		return budgetId;
	}

	public void setBudgetId(Long budgetId) {
		this.budgetId = budgetId;
	}

	public List<BudgetItem> getBudgetItems() {
		return budgetItems;
	}

	public void setBudgetItems(List<BudgetItem> budgetItems) {
		this.budgetItems = budgetItems;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
