package com.maqs.moneytracker.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.BudgetReportDto;
import com.maqs.moneytracker.dto.BudgetSearchDto;
import com.maqs.moneytracker.model.Budget;
import com.maqs.moneytracker.model.BudgetItem;
import com.maqs.moneytracker.services.BudgetService;
import com.wordnik.swagger.annotations.Api;

/**
 * The REST webservices are exposed to the external world. 
 * 
 * @author maqbool.ahmed
 */
@Controller
@RequestMapping("/api/budget")
@Api(value = "budget", description = "Budget API")
@Secured("ROLE_USER")
public class BudgetController {

	@Autowired
	private BudgetService budgetService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Lists the Budgets by given search criteria.
	 * 
	 * @param dto 
	 * @return List of Budget Dtos
	 * @throws ServiceException
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public @ResponseBody
	List<Budget> listBudgets(@RequestBody BudgetSearchDto dto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listBudgets method is been called");
		
		List<Budget> budgets = budgetService.listBudget();
		if (logger.isInfoEnabled())
			logger.info("listBudgets() has listed "
					+ (budgets == null ? 0 : budgets.size()) + " records");

		return budgets;
	}
	
	/**
	 * Lists the BudgetItems by given identifier.
	 * 
	 * @param dto 
	 * @return List of Budget Dtos
	 * @throws ServiceException
	 */
	@RequestMapping(value="/items", method = RequestMethod.POST)
	public @ResponseBody
	List<BudgetItem> listBudgetItems(@RequestBody BudgetSearchDto searchDto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listBudgetItems method is been called");
		
		List<BudgetItem> budgetItems = budgetService.listBudgetItems(searchDto);
		if (logger.isInfoEnabled())
			logger.info("listBudgets() has listed "
					+ (budgetItems == null ? 0 : budgetItems.size()) + " records");

		return budgetItems;
	}
	
	/**
	 * Lists the BudgetItems by given identifier.
	 * 
	 * @param dto 
	 * @return List of Budget Dtos
	 * @throws ServiceException
	 */
	@RequestMapping(value="/budgetedvsspentreport", method = RequestMethod.POST)
	public @ResponseBody
	BudgetReportDto getBudgetVsSpentReport(@RequestBody BudgetSearchDto searchDto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listBudgetItemsTree method is been called");
		
		BudgetReportDto budgetedVsSpentReport = budgetService.getBudgetVsSpentReport(searchDto);
		if (logger.isInfoEnabled())
			logger.info("listBudgetItemsTree() has listed "
					+ (budgetedVsSpentReport.getBudgetItems() == null ? 0 : budgetedVsSpentReport.getBudgetItems().size()) + " records");

		return budgetedVsSpentReport;
	}
	
	@RequestMapping(value="/store", method = RequestMethod.PUT)
	public @ResponseBody
	Budget store(@RequestBody Budget budget)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("store method is been called");
		
		Budget b = budgetService.store(budget);
		return b;
	}
	
	@RequestMapping(value="/storeitems", method = RequestMethod.PUT)
	public @ResponseBody
	List<BudgetItem> storeItems(@RequestBody List<BudgetItem> budgetItems)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("storeItems method is been called");
		
		List<BudgetItem> items = budgetService.store(budgetItems);
		return items;
	}
	
	/**
	 * Total budget for the given search criteria.
	 * 
	 * @param dto 
	 * @return B
	 * @throws ServiceException
	 */
	@RequestMapping(value="/totalbudgeted", method = RequestMethod.POST)
	public @ResponseBody
	BigDecimal getTotalBudgeted(@RequestBody BudgetSearchDto searchDto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("getTotalBudgeted method is been called");
		
		BigDecimal totalBudgeted = budgetService.getTotalBudgeted(searchDto);
		if (logger.isInfoEnabled())
			logger.info("getTotalBudgeted(): " + totalBudgeted);

		return totalBudgeted;
	}
}
