package com.maqs.moneytracker.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maqs.moneytracker.common.paging.list.PageContent;
import com.maqs.moneytracker.common.paging.list.PageableList;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.TransactionDto;
import com.maqs.moneytracker.dto.TransactionSearchDto;
import com.maqs.moneytracker.managers.reports.Report;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.services.DomainService;
import com.maqs.moneytracker.services.TransactionService;
import com.maqs.moneytracker.types.TransactionColumn;
import com.maqs.moneytracker.types.TransactionType;
import com.wordnik.swagger.annotations.Api;

/**
 * The Transaction REST service. 
 * 
 * @author maqbool.ahmed
 */
@Controller
@RequestMapping("/api/transactions")
@Api(value = "transactions", description = "Transactions API")
@Secured("ROLE_USER")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private DomainService domainService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Lists the transactions by given criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Transactions
	 * @throws ServiceException
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public @ResponseBody
	PageContent<Transaction> listTransactions(@RequestBody TransactionSearchDto dto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listTransactions method is been called");
		PageableList<Transaction> transactions = transactionService.list(dto);
		if (transactions == null) {
			return null;
		}
		if (logger.isInfoEnabled())
			logger.info("listTransactions() has listed "
					+ (transactions == null ? 0 : transactions.size()) + " records");
		
		PageContent<Transaction> pageContent = new PageContent<Transaction>(transactions, transactions.getPage());
		return pageContent;
	}
	
	/**
	 * Lists the top n expense categories by given criteria.
	 * 
	 * @param dto criteria
	 * @return List of Transactions
	 * @throws ServiceException
	 */
	@RequestMapping(value="/listtopexpcategories", method = RequestMethod.POST)
	public @ResponseBody
	List<TransactionDto> listTopExpenseCategories(@RequestBody TransactionSearchDto dto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listTransactions method is been called");
		int noOfTransactions = dto.getNoOfTransactions();
		Report reportBy = dto.getReportBy();
		if (reportBy == null) {
			reportBy = Report.THIS_MONTH;
		}
		List<TransactionDto> transactions = transactionService.listTopExpensesCategories(noOfTransactions, reportBy);
		if (transactions == null) {
			return null;
		}
		if (logger.isInfoEnabled())
			logger.info("listTransactions() has listed "
					+ (transactions == null ? 0 : transactions.size()) + " records");
		
		return transactions;
	}
	
	/**
	 * Lists the top n expense categories by given criteria.
	 * 
	 * @param dto criteria
	 * @return List of Transactions
	 * @throws ServiceException
	 */
	@RequestMapping(value="/listtopinccategories", method = RequestMethod.POST)
	public @ResponseBody
	List<TransactionDto> listTopIncomeCategories(@RequestBody TransactionSearchDto dto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listTransactions method is been called");
		int noOfTransactions = dto.getNoOfTransactions();
		Report reportBy = dto.getReportBy();
		if (reportBy == null) {
			reportBy = Report.THIS_MONTH;
		}
		List<TransactionDto> transactions = transactionService.listTopIncomeCategories(noOfTransactions, reportBy);
		if (transactions == null) {
			return null;
		}
		if (logger.isInfoEnabled())
			logger.info("listTransactions() has listed "
					+ (transactions == null ? 0 : transactions.size()) + " records");
		
		return transactions;
	}
	
	/**
	 * Stores the transaction.
	 * 
	 * @param t
	 *            transaction to be stored.
	 * @return Transaction
	 * @throws ServiceException
	 */
	@RequestMapping(value = "store", method = RequestMethod.PUT)
	public @ResponseBody
	Transaction storeTransaction(@RequestBody Transaction t)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("storeTransaction method is been called");
		Long id = t.getId();
		Transaction original = null;
		if (id != null) {
			original = transactionService.get(id);
		}
		t = transactionService.store(original, t);
		return t;
	}
	
	/**
	 * Stores the transactions.
	 * 
	 * @param transactions
	 *            transactions to be stored.
	 * @return Transaction
	 * @throws ServiceException
	 */
	@RequestMapping(value = "storetransactions", method = RequestMethod.PUT)
	public @ResponseBody
	List<Transaction> storeTransactions(@RequestBody List<Transaction> transactions)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("storeTransactions method is been called");
		for (Transaction transaction : transactions) {
			storeTransaction(transaction);
		}
		return transactions;
	}
	/**
	 * Deletes the transaction.
	 * 
	 * @param id
	 *            transaction id to be deleted.
	 * @return true if deleted, otherwise false.
	 * @throws ServiceException
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	boolean deleteTransaction(@PathVariable("id") Long id)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("deleteTransaction method is been called");
		return transactionService.delete(id);
	}
	
	/**
	 * Deletes the list of transactions.
	 * 
	 * @param transactions
	 *            transactions to be deleted.
	 * @return true if deleted, otherwise false.
	 * @throws ServiceException
	 */
	@RequestMapping(value="/deleteall", method = RequestMethod.DELETE)
	public @ResponseBody
	boolean deleteAll(@RequestBody List<Transaction> transactions)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("deleteAll method is been called");
		return transactionService.deleteAll(transactions);
	}
	
	@RequestMapping(value = "expensetotalbyreport", method = RequestMethod.GET)
	public @ResponseBody
	BigDecimal getExpenseTotalAmount(@RequestParam(value="reportBy", required = true) Report reportBy) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("getTotalAmount method is been called");
		return transactionService.getTotalAmount(TransactionType.EXPENSE, reportBy);
	}
	
	@RequestMapping(value = "incometotalbyreport", method = RequestMethod.GET)
	public @ResponseBody
	BigDecimal getIncomeTotalAmount(@RequestParam(value="reportBy", required = true) Report reportBy) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("getTotalAmount method is been called");
		return transactionService.getTotalAmount(TransactionType.INCOME, reportBy);
	}
	
	/**
	 * Lists the ExpenseReport transactions by given criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Transactions
	 * @throws ServiceException
	 */
	@RequestMapping(value="/expensereport", method = RequestMethod.POST)
	public @ResponseBody
	List<TransactionDto> expenseReport(@RequestBody TransactionSearchDto searchDto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("expenseReport method is been called");
		List<TransactionDto> transactions = transactionService.expenseReport(searchDto);
		
		if (logger.isInfoEnabled())
			logger.info("expenseReport() has listed "
					+ (transactions == null ? 0 : transactions.size()) + " records");
		return transactions;
	}
	
	/**
	 * Lists the incomeReport transactions by given criteria.
	 * 
	 * @param searchDto Criteria
	 * @return List of Transactions
	 * @throws ServiceException
	 */
	@RequestMapping(value="/incomereport", method = RequestMethod.POST)
	public @ResponseBody
	List<TransactionDto> incomeReport(@RequestBody TransactionSearchDto searchDto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("incomeReport method is been called");
		List<TransactionDto> transactions = transactionService.incomeReport(searchDto);
		
		if (logger.isInfoEnabled())
			logger.info("incomeReport() has listed "
					+ (transactions == null ? 0 : transactions.size()) + " records");
		return transactions;
	}
	
	/**
	 * Gets the incomeExpenseTotal by given criteria.
	 * 
	 * @param searchDto
	 * @return List of Transactions
	 * @throws ServiceException
	 */
	@RequestMapping(value="/incomeexpensetotal", method = RequestMethod.POST)
	public @ResponseBody
	TransactionDto incomeExpenseTotal(@RequestBody TransactionSearchDto searchDto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("incomeExpenseTotal method is been called");
		TransactionDto transaction = transactionService.getIncomeExpenseTotal(searchDto);
		return transaction;
	}
	
	@RequestMapping(value = "columns", method = RequestMethod.GET)
	public @ResponseBody
	List<TransactionColumn> listTransactionColumns() throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listTransactionColumns method is been called");
		return transactionService.listTransactionColumns();
	}
	
	@RequestMapping(value = "expensetotal", method = RequestMethod.POST)
	public @ResponseBody
	BigDecimal getExpenseTotalAmount(@RequestBody TransactionSearchDto searchDto) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("getExpenseTotalAmount method is been called");
		searchDto.setTranType(TransactionType.EXPENSE);
		return transactionService.getTotalAmount(searchDto);
	}
	
	@RequestMapping(value = "incometotal", method = RequestMethod.POST)
	public @ResponseBody
	BigDecimal getIncomeTotalAmount(@RequestBody TransactionSearchDto searchDto) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("getIncomeTotalAmount method is been called");
		searchDto.setTranType(TransactionType.INCOME);
		return transactionService.getTotalAmount(searchDto);
	}
	
	/**
	 * Lists the ExpenseReport transactions by given criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Transactions
	 * @throws ServiceException
	 */
	@RequestMapping(value="/expensereportastree", method = RequestMethod.POST)
	public @ResponseBody
	List<TransactionDto> expenseReportAsTree(@RequestBody TransactionSearchDto searchDto)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("expenseReport method is been called");
		List<TransactionDto> transactions = transactionService.expenseReport(searchDto);
		List<TransactionDto> transactionsTree = transactionService.transactionsByCategoryTree(transactions);
		if (logger.isInfoEnabled())
			logger.info("expenseReport() has listed "
					+ (transactionsTree == null ? 0 : transactionsTree.size()) + " records");
		return transactionsTree;
	}
	
	/**
	 * Lists the ExpenseReport transactions by given criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Transactions
	 * @throws ServiceException
	 */
	@RequestMapping(value="/incexphistoryreport", method = RequestMethod.POST)
	public @ResponseBody List<TransactionDto> listIncExpHistoryReport(
			@RequestBody TransactionSearchDto searchDto) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listIncExpHistoryReport method is been called");
		List<TransactionDto> transactions = transactionService.listIncExpHistoryReport(searchDto);
		if (logger.isInfoEnabled())
			logger.info("listIncExpHistoryReport() has listed "
					+ (transactions == null ? 0 : transactions.size()) + " records");
		return transactions;
	}
}
