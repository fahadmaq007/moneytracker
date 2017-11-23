package com.maqs.moneytracker.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maqs.moneytracker.common.Constants;
import com.maqs.moneytracker.common.ValidationException;
import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.list.PageableList;
import com.maqs.moneytracker.common.paging.spec.Operation;
import com.maqs.moneytracker.common.paging.spec.OrderBySpec;
import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.common.transferobjects.Action;
import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.common.util.AppUtil;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.util.DateUtil;
import com.maqs.moneytracker.common.util.StringUtil;
import com.maqs.moneytracker.common.util.Util;
import com.maqs.moneytracker.dto.AccountDto;
import com.maqs.moneytracker.dto.DomainSearchDto;
import com.maqs.moneytracker.dto.TransactionDto;
import com.maqs.moneytracker.dto.TransactionSearchDto;
import com.maqs.moneytracker.managers.reports.Report;
import com.maqs.moneytracker.model.Account;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.model.FutureTransaction;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.security.LoggedInChecker;
import com.maqs.moneytracker.server.core.dao.IDao;
import com.maqs.moneytracker.server.core.exception.DataAccessException;
import com.maqs.moneytracker.types.MessageType;
import com.maqs.moneytracker.types.TransactionColumn;
import com.maqs.moneytracker.types.TransactionType;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class TransactionServiceImpl implements TransactionService {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDao dao = null;

	private static final String TRAN_TYPE_REPORT = "Transaction.transactionTypeReport";

	private static final String GET_MONTHLY_INC_EXP_REPORT = "Transaction.getMonthlyIncExpReport";

	private static final String GET_MONTHLY_HISTORY_REPORT_BY_CAT = "Transaction.getMonthlyHistoryReportByCat";

	private static final String GET_MAX_CAT = "Transaction.getMaxCat";

	private static final String GET_LAST_TRAN_BY_CAT = "Transaction.getLastTransactionByCat";

	private static final String LIST_TOP_CATEGORIES = "Transaction.listTopCategories";

	private static final String GET_TOTAL_AMOUNT = "Transaction.getTotalAmount";

	private static final String GET_IN_OUT_OF_INCOME_N_EXPENCES = "Transaction.getInAndOutOfIncAndExp";

	private static final String INCOME_EXPENSE_TOTAL = "Transaction.getIncomeExpenseTotal";

	private static final String SUM_BY_CATEGORIES_REPORT = "Transaction.sumByCategoriesReport";
	
	@Autowired
	private LoggedInChecker loggedInChecker;
	
	@Autowired
	private DomainService domainService;

	public TransactionServiceImpl() {

	}

	public TransactionServiceImpl(IDao transactionDao) {
		setTransactionDao(transactionDao);
	}

	public void setTransactionDao(IDao transactionDao) {
		this.dao = transactionDao;
	}

	public IDao getTransactionDao() {
		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transaction> listByType(TransactionType transactionType,
			Page page) throws ServiceException {
		List<Transaction> transactions = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Transaction.class);
		querySpec.addPropertySpec(new PropertySpec(Category.TRAN_TYPE,
				Operation.EQ, transactionType));
		try {
			transactions = (List<Transaction>) dao.listAll(querySpec, page);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public List<Transaction> store(List<Transaction> transactions)
			throws ServiceException {
		throw new ServiceException("it is deprecated");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transaction> listByCategory(Category category, Page page)
			throws ServiceException {
		List<Transaction> transactions = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Transaction.class);
		querySpec.addPropertySpec(new PropertySpec(Transaction.CAT_ID,
				Operation.EQ, category.getId()));
		try {
			transactions = (List<Transaction>) dao.listAll(querySpec, page);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public boolean delete(Long id) throws ServiceException {
		if (id == null) {
			throw new ValidationException("given id is null");
		}
		try {
			dao.removeEntity(Transaction.class, id);
			return true;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Transaction store(Transaction original, Transaction current)
			throws ServiceException {
		try {
			int actionIndex = current.getAction().getActionIndex();
			doActionPreStore(original, current);
			if (Action.DO_NOTHING == current.getAction().getActionIndex()) {
				return current;
			}
			if (Action.DELETE == actionIndex) {
				dao.removeEntity(current.getClass(), current.getId());
			} else {
				dao.saveOrUpdate(current);
				current.getAction().setActionIndex(actionIndex);
			}
			doActionPostStore(original, current);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return current;
	}

	private void doActionPreStore(Transaction original, Transaction current)
			throws ServiceException {
		int actionIndex = current.getAction().getActionIndex();
		if (Action.DO_NOTHING == actionIndex) {
			return;
		}
		if (Action.CREATE_NEW != actionIndex) {
			com.maqs.moneytracker.common.util.Util.setCreateOrUpdateAction(original,
					current);
			if (Action.UPDATE == actionIndex) {
				current.setLastModifiedOn(new Date(System.currentTimeMillis()));
			}
		}
		if (Action.CREATE_NEW == actionIndex) {
			Long userId = loggedInChecker.getCurrentUserId();
			current.setUserId(userId);
			
/*			Date onDate = current.getCreatedOn();
			onDate = DateUtil.getDateWithoutTime(onDate);
			current.setCreatedOn(onDate);
*/		}
		if (Action.CREATE_NEW == actionIndex || Action.UPDATE == actionIndex) {
			String checksum = getChecksum(current);
			current.setChecksum(checksum);
			
			if (isDuplicate(current)) {
				Action.setDoNothingAction(current);
				return;
			}
		}
		fetchEntities(original, current);
	}

	private void doActionPostStore(Transaction original, Transaction current)
			throws ServiceException {
		if (original != null) {
			boolean sameAccount = original.getAccountId().equals(
					current.getAccountId());
			original.setAction(current.getAction());
			correctOldTransaction(sameAccount, original);
		}

		adjustAccountBalance(current);
		current.setMessage(Constants.MSG_SUCCESSFUL);
		current.setMessageType(MessageType.TYPE_SUCCESS);
		AppUtil.setDoNothingAction(current);
	}

	private void correctOldTransaction(boolean sameAccount, Transaction original)
			throws ServiceException {
		Account a = original.getAccount();
		if (a == null) {
			throw new ValidationException("Account cannot be null");
		}

		Category c = original.getCategory();
		if (c == null) {
			throw new ValidationException("Category cannot be null");
		}
		com.maqs.moneytracker.types.Operation operation = c
				.getTransactionType().getOperation();
		BigDecimal transactionAmt = original.getAmount();
		int actionIndex = original.getAction().getActionIndex();
		switch (actionIndex) {
		case Action.UPDATE:
		case Action.DELETE:
			correctOldAccount(sameAccount, operation, a, transactionAmt);
		}
	}

	private void adjustAccountBalance(Transaction current)
			throws ServiceException {
		int actionIndex = current.getAction().getActionIndex();
		Account a = current.getAccount();
		if (a == null) {
			throw new ValidationException("Account cannot be null");
		}
		BigDecimal transactionAmt = current.getAmount();
		com.maqs.moneytracker.types.Operation operation = current.getCategory()
				.getTransactionType().getOperation();
		switch (actionIndex) {
		case Action.CREATE_NEW:
			adjustAccountForNew(operation, a, transactionAmt);
			break;

		case Action.UPDATE:
			adjustAccountForUpdate(operation, a, transactionAmt);
			break;
		}
		try {
			AppUtil.setUpdateAction(a);
			dao.saveOrUpdate(a);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	private void correctOldAccount(boolean sameAccount,
			com.maqs.moneytracker.types.Operation operation, Account account,
			BigDecimal transactionAmt) throws ServiceException {
		BigDecimal balance = account.getBalance();
		switch (operation) {
		case ADD:
			balance = balance.subtract(transactionAmt);
			account.setBalance(balance);
			break;
		case DEDUCT:
			balance = balance.add(transactionAmt);
			account.setBalance(balance);
			break;
		}
		if (!sameAccount) {
			try {
				AppUtil.setUpdateAction(account);
				dao.saveOrUpdate(account);
			} catch (DataAccessException e) {
				throw new ServiceException(e);
			}
		}
	}

	private void adjustAccountForNew(
			com.maqs.moneytracker.types.Operation operation, Account account,
			BigDecimal transactionAmt) {
		BigDecimal balance = account.getBalance();
		if (com.maqs.moneytracker.types.Operation.ADD == operation) {
			account.setBalance(balance.add(transactionAmt));
		} else if (com.maqs.moneytracker.types.Operation.DEDUCT == operation) {
			account.setBalance(balance.subtract(transactionAmt));
		}
	}

	private void adjustAccountForUpdate(
			com.maqs.moneytracker.types.Operation operation, Account account,
			BigDecimal transactionAmt) {
		BigDecimal balance = account.getBalance();
		if (com.maqs.moneytracker.types.Operation.ADD == operation) {
			account.setBalance(balance.add(transactionAmt));
		} else if (com.maqs.moneytracker.types.Operation.DEDUCT == operation) {
			account.setBalance(balance.subtract(transactionAmt));
		}
	}

	public boolean isDuplicate(Transaction current) throws ServiceException {
		boolean duplicate = false;
		String checksum = current.getChecksum();
		try {
			QuerySpec querySpec = loggedInChecker.getQuerySpec(Transaction.class);
			querySpec.addPropertySpec(new PropertySpec(Transaction.CHECKSUM, checksum));
			Long count = dao.count(querySpec);
			int action = current.getAction().getActionIndex();
			duplicate = Action.CREATE_NEW == action ? count > 0 : count > 1;
			if (duplicate) {
				current.setMessage(Constants.MSG_DUPLICATE);
				current.setMessageType(MessageType.TYPE_ERROR);
			} 
		} catch (DataAccessException e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return duplicate;
	}

	public String getChecksum(Transaction t) throws ServiceException {
		StringBuilder b = new StringBuilder();
		b.append(t.getOnDate()).append(t.getCategoryId())
				.append(t.getAccount()).append(t.getDescription());
		BigDecimal amt = t.getAmount();
		if (amt != null) {
			int amountInt = amt.intValue();
			b.append(amountInt);
		}
		if (t.getFromAccountId() != null) {
			b.append(t.getFromAccountId());
		}
		return AppUtil.createChecksum(b.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transaction get(Long id) throws ServiceException {
		if (id == null) {
			throw new ValidationException("given id is null");
		}
		try {
			return (Transaction) dao.getEntity(Transaction.class, id);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transaction> list(QuerySpec querySpec, Page page)
			throws ServiceException {
		List<Transaction> transactions = null;
		try {
			transactions = (List<Transaction>) dao.listAll(querySpec, page);
			loadDomainEntities(transactions);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	private void loadDomainEntities(List<Transaction> transactions)
			throws ServiceException {
		Map<Long, Category> categoriesMap = domainService.categoriesCacheMap();
		Map<Long, Account> accountsMap = domainService.accountsCacheMap();

		for (Transaction transaction : transactions) {
			Long catId = transaction.getCategoryId();
			Long acctId = transaction.getAccountId();
			if (catId != null) {
				transaction.setCategory(categoriesMap.get(catId));
			}
			if (acctId != null) {
				transaction.setAccount(accountsMap.get(acctId));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransactionDto> listMonthlyIncExpReport(QuerySpec querySpec,
			Page page) throws ServiceException {
		List<TransactionDto> transactions = null;
		try {
			Date[] dates = DateUtil.getHistoricalReportRange();
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { dates[0], dates[1], userId };
			transactions = (List<TransactionDto>) dao.executeNamedSQLQuery(
					GET_MONTHLY_INC_EXP_REPORT, params, TransactionDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransactionDto> listMonthlyHistoryReportByCat(
			QuerySpec querySpec, Page page) throws ServiceException {
		if (querySpec == null) {
			throw new IllegalArgumentException("querySpec is null");
		}
		List<TransactionDto> transactions = null;
		try {
			String tranType = null;
			Long catId = null;
			for (PropertySpec p : querySpec.getPropertySpecs()) {
				String propName = p.getPropertyName();
				if (Transaction.CAT_ID.equals(propName)) {
					catId = (Long) p.getValue();
				} else if (Category.TRAN_TYPE.equals(propName)) {
					tranType = (String) p.getValue();
				}
			}
			if (tranType == null) {
				throw new IllegalArgumentException("tranType is null");
			}
			if (catId == null) {
				throw new ServiceException("Category is not selected.");
			}
			Date[] dates = DateUtil.getHistoricalReportRange();
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { tranType, catId, dates[0], dates[1], userId };
			transactions = (List<TransactionDto>) dao.executeNamedSQLQuery(
					GET_MONTHLY_HISTORY_REPORT_BY_CAT, params,
					TransactionDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category getMaxCategory(QuerySpec querySpec) throws ServiceException {
		if (querySpec == null) {
			throw new IllegalArgumentException("querySpec is null");
		}
		Category category = null;
		try {
			String tranType = null;
			for (PropertySpec p : querySpec.getPropertySpecs()) {
				String propName = p.getPropertyName();
				if (Category.TRAN_TYPE.equals(propName)) {
					tranType = (String) p.getValue();
				}
			}
			if (tranType == null) {
				throw new IllegalArgumentException("tranType is null");
			}
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { tranType, userId };
			List<Category> categories = dao.executeNamedSQLQuery(GET_MAX_CAT,
					params, Category.class);
			if (CollectionsUtil.isNonEmpty(categories)) {
				return category = (Category) categories.get(0);
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return category;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transaction getLastTransaction(Category category)
			throws ServiceException {
		Transaction transaction = null;
		try {
			Long catId = category.getId();
			if (catId == null) {
				throw new IllegalArgumentException("categoryId is required.");
			}
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { category.getId(), userId };
			List<Transaction> transactions = dao.executeNamedSQLQuery(
					GET_LAST_TRAN_BY_CAT, params, Transaction.class);
			if (CollectionsUtil.isNonEmpty(transactions)) {
				return transaction = (Transaction) transactions.get(0);
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transaction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransactionDto> listTopExpensesCategories(int numOfRecords,
			Report reportBy) throws ServiceException {
		List<TransactionDto> transactions = null;
		try {
			String tranType = TransactionType.EXPENSE;
			Date[] range = DateUtil.getRange(reportBy);
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { tranType, range[0], range[1], userId, numOfRecords }; 
			transactions = (List<TransactionDto>) dao.executeNamedSQLQuery(
					LIST_TOP_CATEGORIES, params, TransactionDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransactionDto> listTopIncomeCategories(int numOfRecords,
			Report reportBy) throws ServiceException {
		List<TransactionDto> transactions = null;
		try {
			String tranType = TransactionType.INCOME;
			Date[] range = DateUtil.getRange(reportBy);
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { tranType, range[0], range[1], userId, numOfRecords };
			transactions = (List<TransactionDto>) dao.executeNamedSQLQuery(
					LIST_TOP_CATEGORIES, params, TransactionDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getTotalAmount(String tranType, Report reportBy)
			throws ServiceException {
		Date[] range = DateUtil.getRange(reportBy);
		System.out.println("getTOtalAmount " + range[0] + " " + range[1]);
		TransactionSearchDto dto = new TransactionSearchDto();
		dto.setTranType(tranType);
		dto.setFromDate(range[0]);
		dto.setToDate(range[1]);

		return getTotalAmount(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getTotalAmount(TransactionSearchDto searchDto)
			throws ServiceException {
		if (searchDto == null) {
			throw new IllegalArgumentException("no criteria available.");
		}
		BigDecimal total = null;
		try {
			Date[] range = getRange(searchDto);
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { searchDto.getTranType(),
					range[0], range[1], userId };
			List result = dao.executeNamedSQLQuery(GET_TOTAL_AMOUNT, params);
			if (CollectionsUtil.isNonEmpty(result)) {
				total = (BigDecimal) result.get(0);
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return total;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AccountDto> getInAndOutOfIncAndExp(Report reportBy)
			throws ServiceException {
		List<AccountDto> accountDtos = null;
		try {
			Date[] range = DateUtil.getRange(reportBy);
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { range[0], range[1], userId }; // E - Expense
			accountDtos = (List<AccountDto>) dao.executeNamedSQLQuery(
					GET_IN_OUT_OF_INCOME_N_EXPENCES, params, AccountDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return accountDtos;
	}

	@Override
	public PageableList<Transaction> list(TransactionSearchDto dto)
			throws ServiceException {
		if (dto == null) {
			throw new IllegalArgumentException("searchDto is null...");
		}
		Page page = dto.getPage();
		if (page == null) {
			logger.warn("page is not available, using the default page...");
			page = new Page(0, 50);
		}
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Transaction.class);
		Date[] range = getRange(dto);
		if (range != null) {
			querySpec.addPropertySpec(new PropertySpec(
					TransactionSearchDto.ON_DATE, Operation.BETWEEN,
					new Date[] { range[0], range[1] }));
		}
		
		List<Long> accountIds = dto.getAccountIds();
		if (CollectionsUtil.isNonEmpty(accountIds)) {
			querySpec.addPropertySpec(new PropertySpec(
					TransactionSearchDto.ACCT_ID, Operation.IN, accountIds));
		}

		List<Long> categoryIds = dto.getCategoryIds();
		if (CollectionsUtil.isNonEmpty(categoryIds)) {
			querySpec.addPropertySpec(new PropertySpec(
					TransactionSearchDto.CAT_ID, Operation.IN, categoryIds));
		}

		String tranType = dto.getTranType();
		if (tranType != null) {
			querySpec.addPropertySpec(new PropertySpec(
					TransactionSearchDto.TRAN_TYPE, tranType));
		}

		String desc = dto.getDescription();
		if (! StringUtil.nullOrEmpty(desc)) {
			querySpec.addPropertySpec(new PropertySpec(
					TransactionSearchDto.DESC, Operation.LIKE, desc));
		}
		List<OrderBySpec> orderByList = dto.getOrderByList();
		if (CollectionsUtil.isNonEmpty(orderByList)) {
			querySpec.setOrderBySpecs(orderByList);
		}
		List<Transaction> list = list(querySpec, page);
		if (CollectionsUtil.isNonEmpty(list)
				&& list instanceof PageableList) {
			PageableList<Transaction> transactions = (PageableList<Transaction>) list;
			return transactions;
		}
		return null;
	}

	private Date[] getRange(TransactionSearchDto dto) {
		logger.debug("b4 from : " + dto.getFromDate() + " to: " + dto.getToDate());
		Date from = dto.getFromDate();
		Date to = null;
		if (from != null) {
			to = dto.getToDate();
			if (to == null) {
				to = new Date();
			}
			from = DateUtil.getDateWithoutTime(from);
			to = DateUtil.getToDate(to);
			logger.debug("after from : " + from + " to: " + to);
			return new Date[] {from, to};
		}
		return null;
	}

	private void fetchEntities(Transaction original, Transaction current)
			throws ServiceException {
		if (original != null) {
			fetchAccount(original);
			Long oldAccountId = original.getAccountId();
			Long currectAccountId = current.getAccountId();
			boolean sameAccount = oldAccountId.equals(currectAccountId);
			if (sameAccount) {
				current.setAccount(original.getAccount());
			} else {
				fetchAccount(current);
			}
		} else {
			fetchAccount(current);
		}

		if (original != null) {
			Long origCatId = original.getCategoryId();
			Long currentCatId = current.getCategoryId();
			boolean sameCat = origCatId.equals(currentCatId);
			fetchCategory(original);
			if (sameCat) {
				current.setCategory(original.getCategory());
			} else {
				fetchCategory(current);
			}
		} else {
			fetchCategory(current);
		}
	}

	private void fetchAccount(Transaction t) throws ServiceException {
		if (t != null) {
			Long accountId = t.getAccountId();
			Account a = domainService.getAccountById(accountId);
			t.setAccount(a);
		}
	}

	private void fetchCategory(Transaction t) throws ServiceException {
		if (t != null) {
			Long catId = t.getCategoryId();
			Category c = domainService.getCategoryById(catId);
			t.setCategory(c);
		}
	}

	@Override
	public List<TransactionDto> expenseReport(TransactionSearchDto searchDto)
			throws ServiceException {
		List<TransactionDto> transactions = null;
		if (! validDates(searchDto)) {
			return transactions;
		}
		try {
			Date[] range = getRange(searchDto);
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { range[0], range[1], TransactionType.EXPENSE, userId };
			transactions = (List<TransactionDto>) dao.executeNamedSQLQuery(
					TRAN_TYPE_REPORT, params, TransactionDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	private boolean validDates(TransactionSearchDto searchDto) {
		return true;
	}

	@Override
	public List<TransactionDto> incomeReport(TransactionSearchDto searchDto)
			throws ServiceException {
		List<TransactionDto> transactions = null;
		try {
			Date[] range = getRange(searchDto);
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { range[0], range[1], TransactionType.INCOME, userId };
			transactions = (List<TransactionDto>) dao.executeNamedSQLQuery(
					TRAN_TYPE_REPORT, params, TransactionDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}
	
	@Override
	public List<TransactionDto> sumByExpenseCategories(TransactionSearchDto searchDto)
			throws ServiceException {
		List<TransactionDto> transactions = null;
		Date[] range = getRange(searchDto);
		try {
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { range[0], range[1], TransactionType.EXPENSE, userId };
			transactions = (List<TransactionDto>) dao.executeNamedSQLQuery(
					SUM_BY_CATEGORIES_REPORT, params, TransactionDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	@Override
	public TransactionDto getIncomeExpenseTotal(TransactionSearchDto searchDto)
			throws ServiceException {
		TransactionDto transaction = null;
		try {
			if (searchDto.getFromDate() == null) {
				throw new IllegalArgumentException("fromDate is required.");
			}
 			Date[] range = getRange(searchDto);
 			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { range[0], range[1], userId };
			List<TransactionDto> transactions = (List<TransactionDto>) dao
					.executeNamedSQLQuery(INCOME_EXPENSE_TOTAL, params,
							TransactionDto.class);

			if (CollectionsUtil.isNonEmpty(transactions)) {
				transaction = new TransactionDto();
				for (TransactionDto dto : transactions) {
					String tranType = dto.getTranType();
					if (TransactionType.EXPENSE.equals(tranType)) {
						transaction.setExpenseTotal(dto.getExpenseTotal());
					} else if (TransactionType.INCOME.equals(tranType)) {
						transaction.setIncomeTotal(dto.getIncomeTotal());
					}
				}

				BigDecimal incomeTotal = transaction.getIncomeTotal();
				if (incomeTotal == null) {
					incomeTotal = BigDecimal.ZERO;
				}
				BigDecimal expenseTotal = transaction.getExpenseTotal();
				if (expenseTotal == null) {
					expenseTotal = BigDecimal.ZERO;
				}

				double inc = incomeTotal.doubleValue();
				double exp = expenseTotal.doubleValue();
				if (inc > 0 && exp > 0) {
					double perc = (inc - exp) / inc * 100;
					BigDecimal percent = BigDecimal.valueOf(perc);
					transaction.setPercent(percent);
				} else {
					if (inc <= 0) {
						transaction.setIncomeTotal(BigDecimal.ZERO);
						transaction.setPercent(BigDecimal.valueOf(0));
					}
					if (exp <= 0) {
						transaction.setExpenseTotal(BigDecimal.ZERO);
						transaction.setPercent(BigDecimal.valueOf(100));
					}
				}
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transaction;
	}

	@Override
	public Map<Long, TransactionDto> getTransactionsByCategoryMap(
			List<TransactionDto> transactions) {
		Map<Long, TransactionDto> map = new HashMap<Long, TransactionDto>();
		if (CollectionsUtil.isNonEmpty(transactions)) {
			for (TransactionDto transactionDto : transactions) {
				Long catId = transactionDto.getCatId();
				if (catId != null) {
					map.put(catId, transactionDto);
				}
			}
		}
		return map;
	}

	public List<Category> fetchCategories(List<Transaction> transactions)
			throws ServiceException {
		List<Category> categories = null;
		if (CollectionsUtil.isNonEmpty(transactions)) {
			Set<Long> ids = new HashSet<Long>();
			for (Transaction t : transactions) {
				Long catId = t.getCategoryId();
				if (catId != null) {
					ids.add(catId);
				}
			}

			if (CollectionsUtil.isNonEmpty(ids)) {
				Map<Long, Entity> map = domainService.fetchCategoryMap(ids);
				for (Transaction t : transactions) {
					Long catId = t.getCategoryId();
					if (catId != null) {
						Category c = (Category) map.get(catId);
						t.setCategory(c);
					}
				}
			}
		}
		return categories;
	}

	public List<Account> fetchAccounts(List<Transaction> transactions)
			throws ServiceException {
		List<Account> accounts = null;
		if (CollectionsUtil.isNonEmpty(transactions)) {
			Set<Long> ids = new HashSet<Long>();
			for (Transaction t : transactions) {
				Long acctId = t.getAccountId();
				if (acctId != null) {
					ids.add(acctId);
				}
			}

			if (CollectionsUtil.isNonEmpty(ids)) {
				DomainSearchDto dto = new DomainSearchDto();
				dto.setAccountIds(new ArrayList<Long>(ids));
				accounts = domainService.listAccounts(dto);
				Map<Long, Entity> map = com.maqs.moneytracker.common.util.Util
						.getMap(accounts);
				for (Transaction t : transactions) {
					Long acctId = t.getAccountId();
					if (acctId != null) {
						Account a = (Account) map.get(acctId);
						t.setAccount(a);
					}
				}
			}
		}
		return accounts;
	}

	public List<TransactionColumn> listTransactionColumns()
			throws ServiceException {
		return new ArrayList<TransactionColumn>(TransactionColumn.values());
	}
	
	public List<TransactionDto> transactionsByCategoryTree(
			List<TransactionDto> transactions) throws ServiceException {
		if (!CollectionsUtil.isNonEmpty(transactions)) {
			return transactions;
		}
		Map<Long, TransactionDto> transactionMap = getTransactionsByCategoryMap(transactions);
		List<TransactionDto> tree = new ArrayList<TransactionDto>();
		Set<Long> ids = new HashSet<Long>();
		for (TransactionDto transactionDto : transactions) {
			Long catId = transactionDto.getCatId();
			ids.add(catId);
		}
		Map<Long, Entity> map = domainService.fetchCategoryMap(ids);
		
		Set<Long> parentCatIds = new HashSet<Long>();
		for (Long catId : map.keySet()) {
			Category c = (Category) map.get(catId);
			Long parentId = c.getParentCategoryId();
			if (parentId != null && !map.containsKey(parentId)) {
				parentCatIds.add(parentId);
			}
		}
		if (CollectionsUtil.isNonEmpty(parentCatIds)) {
			Map<Long, Entity> parentCatMap = domainService.fetchCategoryMap(parentCatIds);
			if (parentCatMap != null) {
				map.putAll(parentCatMap);
			}
		}
		for (TransactionDto t : transactions) {
			Long catId = t.getCatId();
			if (catId != null) {
				Category c = (Category) map.get(catId);
				Long parentId = c.getParentCategoryId();
				if (parentId == null) {
					tree.add(t);
				} else {
					Category parent = (Category) map.get(parentId);
					if (parent != null) {
						TransactionDto parentTran = transactionMap.get(parentId);
						if (parentTran == null) {
							parentTran = new TransactionDto();
							parentTran.setCatId(parentId);
							parentTran.setCatType(parent.getName());
							parentTran.setAmount(BigDecimal.ZERO);
							
							tree.add(parentTran);
							transactionMap.put(parentId, parentTran);
						}
						parentTran.addChild(t);
					}
				}
			}
		}
		calculateParentTotal(tree);
		return tree;
	}
	
	private void calculateParentTotal(List<TransactionDto> items) {
		if (CollectionsUtil.isNonEmpty(items)) {
			for (TransactionDto item : items) {
				if (CollectionsUtil.isNonEmpty(item.getChildren())) {
					BigDecimal total = calculateParentTotal(item);
					item.setAmount(total);
				}
			}
		}
	}

	private BigDecimal calculateParentTotal(TransactionDto item) {
		BigDecimal total = item.getAmount();
		if (CollectionsUtil.isNonEmpty(item.getChildren())) {
			for (TransactionDto b : item.getChildren()) {
				total = total.add(b.getAmount());
			}
		}
		return total;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransactionDto> listIncExpHistoryReport(TransactionSearchDto searchDto) throws ServiceException {
		if (searchDto == null) {
			throw new IllegalArgumentException("given criteria is null");
		}
		Integer noOfMonths = searchDto.getNoOfTransactions();
		Date[] dates = null;
		if (noOfMonths == null) {
			dates = DateUtil.getHistoricalReportRange();
		} else {
			dates = DateUtil.getHistoricalRange(noOfMonths);
		}
		searchDto.setFromDate(dates[0]);
		searchDto.setToDate(dates[1]);
		Date[] range = getRange(searchDto);
		List<TransactionDto> transactions = null;
		try {			
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { range[0], range[1], userId };
			transactions = (List<TransactionDto>) dao.executeNamedSQLQuery(
					GET_MONTHLY_INC_EXP_REPORT, params, TransactionDto.class);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return transactions;
	}

	@Override
	public List<FutureTransaction> listFutureTransactions(QuerySpec querySpec,
			Page page) throws ServiceException {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public boolean deleteAll(List<Transaction> transactions) throws ServiceException {
		if (CollectionsUtil.isNullOrEmpty(transactions)) {
			throw new IllegalArgumentException("given transactions list is empty");
		}
		try {
			Set<Long> ids = Util.getIds(transactions);
			dao.removeAll(Transaction.class, Transaction.ID, Operation.IN, ids);
			return true;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}
	
}
