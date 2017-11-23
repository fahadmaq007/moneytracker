package com.maqs.moneytracker.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.list.PageableList;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.AccountDto;
import com.maqs.moneytracker.dto.TransactionDto;
import com.maqs.moneytracker.dto.TransactionSearchDto;
import com.maqs.moneytracker.managers.reports.Report;
import com.maqs.moneytracker.model.Account;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.model.FutureTransaction;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.types.TransactionColumn;
import com.maqs.moneytracker.types.TransactionType;

public interface TransactionService {

	public List<Transaction> listByType(TransactionType transactionType, Page page) throws ServiceException;
	
	public List<Transaction> listByCategory(Category category, Page page) throws ServiceException;
	
	public List<Transaction> store(List<Transaction> transactions) throws ServiceException;

	public Transaction store(Transaction original, Transaction current) throws ServiceException;
	
	public Transaction get(Long id) throws ServiceException;
	
	public boolean delete(Long id) throws ServiceException;
	
	public List<Transaction> list(QuerySpec querySpec, Page page) throws ServiceException;
	
	public List<TransactionDto> listMonthlyIncExpReport(QuerySpec querySpec, Page page) throws ServiceException;
	
	public List<TransactionDto> listMonthlyHistoryReportByCat(QuerySpec querySpec, Page page) throws ServiceException;

	public Category getMaxCategory(QuerySpec querySpec) throws ServiceException;

	public Transaction getLastTransaction(Category category) throws ServiceException;

	public List<TransactionDto> listTopExpensesCategories(int numExp, Report reportBy) throws ServiceException;
	
	public List<TransactionDto> listTopIncomeCategories(int numExp, Report reportBy) throws ServiceException;

	public BigDecimal getTotalAmount(String tranType, Report thisMonth) throws ServiceException;
	
	public BigDecimal getTotalAmount(TransactionSearchDto searchDto)
			throws ServiceException;
	
	public List<AccountDto> getInAndOutOfIncAndExp(Report reportBy) throws ServiceException;
	
	public PageableList<Transaction> list(TransactionSearchDto dto)
			throws ServiceException;

	public List<TransactionDto> expenseReport(
			TransactionSearchDto searchDto) throws ServiceException;
	
	public TransactionDto getIncomeExpenseTotal(
			TransactionSearchDto searchDto) throws ServiceException;

	public List<TransactionDto> sumByExpenseCategories(
			TransactionSearchDto transactionSearchDto) throws ServiceException;

	public Map<Long, TransactionDto> getTransactionsByCategoryMap(
			List<TransactionDto> tranListByCategories);
	
	public List<Account> fetchAccounts(List<Transaction> transactions)
			throws ServiceException;
	
	public List<Category> fetchCategories(List<Transaction> transactions)
			throws ServiceException;
	
	public List<TransactionColumn> listTransactionColumns() throws ServiceException;
	
	public String getChecksum(Transaction t) throws ServiceException;
	
	public boolean isDuplicate(Transaction t) throws ServiceException;

	public List<TransactionDto> incomeReport(TransactionSearchDto searchDto) throws ServiceException;

	public List<TransactionDto> transactionsByCategoryTree(
			List<TransactionDto> transactions) throws ServiceException;
	
	public List<TransactionDto> listIncExpHistoryReport(TransactionSearchDto searchDto) throws ServiceException;
	
	public List<FutureTransaction> listFutureTransactions(QuerySpec querySpec, Page page) throws ServiceException;

	public boolean deleteAll(List<Transaction> transactions) throws ServiceException;
	
}
