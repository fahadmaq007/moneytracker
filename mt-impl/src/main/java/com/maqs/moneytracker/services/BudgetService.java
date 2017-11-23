package com.maqs.moneytracker.services;

import java.math.BigDecimal;
import java.util.List;

import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.BudgetReportDto;
import com.maqs.moneytracker.dto.BudgetSearchDto;
import com.maqs.moneytracker.model.Budget;
import com.maqs.moneytracker.model.BudgetItem;

public interface BudgetService {

	public Budget getByName(String name) throws ServiceException;

	public Budget store(Budget budget) throws ServiceException;

	public List<BudgetItem> listBudgetItems(BudgetSearchDto searchDto)
			throws ServiceException;

	public List<BudgetItem> store(List<BudgetItem> budgetItems)
			throws ServiceException;

	public List<Budget> listBudget() throws ServiceException;
	
	public BudgetReportDto getBudgetVsSpentReport(BudgetSearchDto searchDto)
			throws ServiceException;

	public BigDecimal getTotalBudgeted(BudgetSearchDto searchDto) throws ServiceException;
	
}
