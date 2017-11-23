package com.maqs.moneytracker.services;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.maqs.moneytracker.common.paging.spec.Operation;
import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.BusinessException;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.common.transferobjects.Action;
import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.util.Util;
import com.maqs.moneytracker.dto.BudgetReportDto;
import com.maqs.moneytracker.dto.BudgetSearchDto;
import com.maqs.moneytracker.dto.DomainSearchDto;
import com.maqs.moneytracker.dto.TransactionDto;
import com.maqs.moneytracker.dto.TransactionSearchDto;
import com.maqs.moneytracker.model.Budget;
import com.maqs.moneytracker.model.BudgetItem;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.security.LoggedInChecker;
import com.maqs.moneytracker.server.core.dao.IDao;
import com.maqs.moneytracker.server.core.exception.DataAccessException;

@Service
@Transactional(value = "transactionManager", readOnly = true, propagation = Propagation.SUPPORTS)
public class BudgetServiceImpl implements BudgetService {

	private static final String GET_TOTAL_BUDGETED = "BudgetItem.getTotalBudgeted";

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDao dao = null;

	@Autowired
	private DomainService domainService;

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private LoggedInChecker loggedInChecker;
	
	public BudgetServiceImpl() {

	}

	public BudgetServiceImpl(IDao dao) {
		setDao(dao);
		// cache purpose
	}

	public void setDao(IDao categoryDao) {
		this.dao = categoryDao;
	}

	public IDao getDao() {
		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public List<BudgetItem> store(List<BudgetItem> budgetItems)
			throws ServiceException {
		return store0(budgetItems);
	}

	private List<BudgetItem> store0(List<BudgetItem> budgetItems)
			throws ServiceException {
		try {
			Long userId = loggedInChecker.getCurrentUserId();
			for (BudgetItem budgetItem : budgetItems) {
				int actionIndex = budgetItem.getAction().getActionIndex();
				if (Action.CREATE_NEW == actionIndex) {
					budgetItem.setUserId(userId);
				}
			}
			dao.saveAll(budgetItems);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return budgetItems;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public Budget getByName(String name) throws ServiceException {
		if (name == null) {
			throw new IllegalArgumentException("given name is null");
		}
		Budget budget = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Budget.class);
		querySpec.addPropertySpec(new PropertySpec(Budget.NAME, Operation.EQ,
				name));
		try {
			budget = (Budget) dao.getEntity(querySpec);
			if (budget == null) {
				logger.debug("Budget: " + name + " not found.");
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return budget;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Budget store(Budget budget) throws ServiceException {
		try {
			Long userId = loggedInChecker.getCurrentUserId();
			int actionIndex = budget.getAction().getActionIndex();
			if (Action.CREATE_NEW == actionIndex) {
				budget.setUserId(userId);
			}
			budget = (Budget) dao.saveOrUpdate(budget);

			if (CollectionsUtil.isNonEmpty(budget.getItems())) {
				store0(budget.getItems());
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return budget;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<BudgetItem> listBudgetItems(BudgetSearchDto searchDto)
			throws ServiceException {
		if (searchDto == null) {
			throw new IllegalArgumentException("given search criteria is null");
		}

		Long budgetId = searchDto.getBudgetId();
		if (budgetId == null) {
			throw new IllegalArgumentException("given budgetId is null");
		}

		List<BudgetItem> items = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(BudgetItem.class);
		querySpec.addPropertySpec(new PropertySpec(BudgetItem.BUDGET_ID,
				budgetId));
		try {
			items = (List<BudgetItem>) dao.listAll(querySpec,
					searchDto.getPage());
			List<Category> categories = fetchCategories(items);
			categories = listParentCategories(categories);
			List<BudgetItem> budgetItemsTree = prepareBudgetTree(items, categories);
			calculateParentTotal(budgetItemsTree);
			return budgetItemsTree;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public BudgetReportDto getBudgetVsSpentReport(BudgetSearchDto searchDto)
			throws ServiceException {
		if (searchDto == null) {
			throw new IllegalArgumentException("given search criteria is null");
		}

		Long budgetId = searchDto.getBudgetId();
		if (budgetId == null) {
			throw new IllegalArgumentException("given budgetId is null");
		}

		BudgetReportDto budgetReportDto = new BudgetReportDto();
		budgetReportDto.setBudgetId(budgetId);
		List<BudgetItem> budgetItems = null;
		
		try {
			TransactionSearchDto transactionSearchDto = new TransactionSearchDto();
			transactionSearchDto.setFromDate(searchDto.getFromDate());
			transactionSearchDto.setToDate(searchDto.getToDate());
			List<TransactionDto> tranListByCategories = transactionService.sumByExpenseCategories(transactionSearchDto);
			if (! CollectionsUtil.isNonEmpty(tranListByCategories)) {
				throw new BusinessException("no_transactions_found", "No transactions found during this period");
			}
			Map<Long, TransactionDto> map = transactionService.getTransactionsByCategoryMap(tranListByCategories);
			Set<Long> ids = map.keySet();
			
			QuerySpec querySpec = loggedInChecker.getQuerySpec(BudgetItem.class);
			querySpec.addPropertySpec(new PropertySpec(BudgetItem.BUDGET_ID,
					budgetId));
			querySpec.addPropertySpec(new PropertySpec(BudgetItem.CAT_ID, Operation.IN, ids));
			budgetItems = (List<BudgetItem>) dao.listAll(querySpec,
					searchDto.getPage());
			List<Category> categories = fetchCategories(ids);
			
			putSpentAmount(budgetItems, tranListByCategories);
			categories = listParentCategories(categories);
			List<BudgetItem> budgetItemsTree = prepareBudgetTree(budgetItems, categories);
			calculateParentTotal(budgetItemsTree);
			budgetReportDto.setBudgetItems(budgetItemsTree);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return budgetReportDto;
	}
	
	private void calculateParentTotal(List<BudgetItem> items) {
		if (CollectionsUtil.isNonEmpty(items)) {
			for (BudgetItem item : items) {
				BigDecimal budgetedTotal = calculateParentBudgetedTotal(item);
				item.setAmount(budgetedTotal);
				
				BigDecimal spentTotal = calculateParentSpentTotal(item);
				item.setSpent(spentTotal);
			}
		}
	}

	private BigDecimal calculateParentBudgetedTotal(BudgetItem item) {
		BigDecimal total = item.getAmount();
		if (CollectionsUtil.isNonEmpty(item.getChildren())) {
			for (BudgetItem b : item.getChildren()) {
				total = total.add(b.getAmount());
			}
		}
		return total;
	}
	
	private BigDecimal calculateParentSpentTotal(BudgetItem item) {
		BigDecimal total = item.getSpent();
		if (CollectionsUtil.isNonEmpty(item.getChildren())) {
			for (BudgetItem b : item.getChildren()) {
				total = total.add(b.getSpent());
			}
		}
		return total;
	}
	
	private void putSpentAmount(List<BudgetItem> budgetItems, List<TransactionDto> tranListByCategories) {
		if (CollectionsUtil.isNonEmpty(budgetItems) && CollectionsUtil.isNonEmpty(tranListByCategories)) {
			Map<Long, Entity> availableBudgetItemsMap = getItemsMapByCategory(budgetItems);
			
			for (TransactionDto transactionDto : tranListByCategories) {
				Long catId = transactionDto.getCatId();
				if (catId != null) {
					BudgetItem i = (BudgetItem) availableBudgetItemsMap.get(catId);
					if (i == null) {
						i = new BudgetItem();
						i.setCategoryId(catId);
						budgetItems.add(i);
					} 
					i.setSpent(transactionDto.getAmount());
				}
			}
		}
	}

	@Transactional(readOnly = true)
	public List<Category> listParentCategories(List<Category> categories) throws ServiceException {
		if (CollectionsUtil.isNonEmpty(categories)) {
			Set<Long> parentCategoryIds = new HashSet<Long>();
			for (Category category : categories) {
				Long parentCategoryId = category.getParentCategoryId();
				if (parentCategoryId != null) {
					parentCategoryIds.add(parentCategoryId);
				}
			}
			if (CollectionsUtil.isNonEmpty(parentCategoryIds)) {
				DomainSearchDto dto = new DomainSearchDto();
				dto.setCategoryIds(new ArrayList<Long>(parentCategoryIds));
				List<Category> parentCategories = domainService.listCategories(dto);
				if (CollectionsUtil.isNonEmpty(parentCategories)) {
					categories.addAll(parentCategories);
				}
			}
			
			Map<Long, Entity> map = Util.getMap(categories);
			if (map.size() != categories.size()) {
				categories.clear(); 
				for (Entity e : map.values()) {
					Category c = (Category) e;
					categories.add(c);
				}
			}
		}
		return categories;
	}

	private List<BudgetItem> prepareBudgetTree(List<BudgetItem> items, List<Category> categories) {
		List<BudgetItem> budgetItemsTree = null;
		if (CollectionsUtil.isNonEmpty(items) && CollectionsUtil.isNonEmpty(categories)) {
			Map<Long, Entity> itemsMapByCategory = getItemsMapByCategory(items);
			Map<Long, Entity> categoryMap = Util.getMap(categories);
			budgetItemsTree = new ArrayList<BudgetItem>(); 
			for (BudgetItem item : items) {
				Long catId = item.getCategoryId();
				Category c = (Category) categoryMap.get(catId);
				if (c != null) {
					item.setCategory(c);
					Long parentCategoryId = c.getParentCategoryId();
					if (parentCategoryId == null) {
						budgetItemsTree.add(item);
					} else {
						BudgetItem parent = (BudgetItem) itemsMapByCategory.get(parentCategoryId);
						if (parent == null) {
							Category parentCategory = (Category) categoryMap.get(parentCategoryId);
							parent = new BudgetItem(parentCategory);
							itemsMapByCategory.put(parentCategoryId, parent);
							parent.setBudgetId(item.getBudgetId());
							parent.addChild(item);
							budgetItemsTree.add(parent);
						} else {
							parent.addChild(item);
						}
					}
				}				
			}
		}
		return budgetItemsTree;
	}
	
	private Map<Long, Entity> getItemsMapByCategory(List<BudgetItem> items) {
		if (CollectionsUtil.isNonEmpty(items)) {
			Map<Long, Entity> itemsMapByCategory = new HashMap<Long, Entity>();
			for (BudgetItem budgetItem : items) {
				Long catId = budgetItem.getCategoryId();
				itemsMapByCategory.put(catId, budgetItem);
			}
			return itemsMapByCategory;
		}
		return null;
	}

	private void assignCategories(List<BudgetItem> items,
			List<Category> categories) {
		if (CollectionsUtil.isNonEmpty(items)) {
			Map<Long, Entity> map = Util.getMap(categories);
			for (BudgetItem budgetItem : items) {
				Long catId = budgetItem.getCategoryId();
				Category c = (Category) map.get(catId);
				budgetItem.setCategory(c);
			}
		}
	}

	private List<Category> fetchCategories(Set<Long> ids)
			throws ServiceException {
		List<Category> categories = null;
		if (CollectionsUtil.isNonEmpty(ids)) {
			DomainSearchDto dto = new DomainSearchDto();
			dto.setCategoryIds(new ArrayList<Long>(ids));
			categories = domainService.listCategories(dto);
		}
		return categories;
	}
	
	private List<Category> fetchCategories(List<BudgetItem> items)
			throws ServiceException {
		List<Category> categories = null;
		if (CollectionsUtil.isNonEmpty(items)) {
			Set<Long> ids = new HashSet<Long>();
			for (BudgetItem budgetItem : items) {
				Long catId = budgetItem.getCategoryId();
				ids.add(catId);
			}

			if (CollectionsUtil.isNonEmpty(ids)) {
				categories = fetchCategories(ids);
				Map<Long, Entity> map = Util.getMap(categories);
				for (BudgetItem budgetItem : items) {
					Long catId = budgetItem.getCategoryId();
					Category c = (Category) map.get(catId);
					budgetItem.setCategory(c);
				}
			}
		}
		return categories;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<Budget> listBudget() throws ServiceException {
		List<Budget> items = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Budget.class);
		try {
			items = (List<Budget>) dao.listAll(querySpec, null);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return items;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public BigDecimal getTotalBudgeted(BudgetSearchDto searchDto) 
			throws ServiceException {
		checkSearchCriteria(searchDto);

		BigDecimal totalBudgeted = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(BudgetItem.class);
		Long budgetId = searchDto.getBudgetId();
		querySpec.addPropertySpec(new PropertySpec(BudgetItem.BUDGET_ID,
				budgetId));
		try {
			Long userId = loggedInChecker.getCurrentUserId();
			Object[] params = { budgetId, userId };
			List result = dao.executeNamedSQLQuery(GET_TOTAL_BUDGETED, params);
			if (CollectionsUtil.isNonEmpty(result)) {
				totalBudgeted = (BigDecimal) result.get(0);
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return totalBudgeted;
	}

	private void checkSearchCriteria(BudgetSearchDto searchDto) {
		if (searchDto == null) {
			throw new IllegalArgumentException("given search criteria is null");
		}

		Long budgetId = searchDto.getBudgetId();
		if (budgetId == null) {
			throw new IllegalArgumentException("given budgetId is null");
		}
	}
	
}
