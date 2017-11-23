package com.maqs.moneytracker.services;

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
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.util.Util;
import com.maqs.moneytracker.dto.DomainSearchDto;
import com.maqs.moneytracker.model.Account;
import com.maqs.moneytracker.model.BaseEntity;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.model.User;
import com.maqs.moneytracker.security.LoggedInChecker;
import com.maqs.moneytracker.server.core.dao.IDao;
import com.maqs.moneytracker.server.core.exception.DataAccessException;
import com.maqs.moneytracker.types.AccountType;
import com.maqs.moneytracker.types.MessageType;
import com.maqs.moneytracker.types.TransactionType;

@Service
@Transactional(value="transactionManager", readOnly = true)
public class DomainServiceImpl implements DomainService {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDao dao = null;

	@Autowired
	private LoggedInChecker loggedInChecker;
	
	private Map<Long, Category> categoriesCacheMap;
	
	private Map<Long, Account> accountsCacheMap;
	
	@Autowired
	private UserService userService;
	
	public DomainServiceImpl() {
		this(null);
	}

	public DomainServiceImpl(IDao dao) {
		setDao(dao);
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
	@Override
	public List<Category> listByType(TransactionType transactionType, Page page)
			throws ServiceException {
		List<Category> categories = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Category.class);
		querySpec.addPropertySpec(new PropertySpec(Category.TRAN_TYPE,
				Operation.EQ, transactionType.getCode()));
		try {
			categories = (List<Category>) dao.listAll(querySpec, page);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return categories;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public List<Category> storeCategories(List<Category> categories)
			throws ServiceException {
		try {
			Long userId = loggedInChecker.getCurrentUserId();
			List<Category> storeList = new ArrayList<Category>();
			for (Category category : categories) {
				int actionIndex = category.getAction().getActionIndex();
				if (Action.CREATE_NEW == actionIndex) {
					category.setUserId(userId);
				}
				if (isDuplicate(category)) {
					category.setMessage(Constants.MSG_DUPLICATE);
					category.setMessageType(MessageType.TYPE_ERROR);
					continue;
				} else {
					category.setMessage(Constants.MSG_SUCCESSFUL);
					category.setMessageType(MessageType.TYPE_SUCCESS);
					storeList.add(category);
				}
			}
			if (CollectionsUtil.isNonEmpty(storeList)) {
				dao.saveAll(storeList);
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return categories;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public List<Category> storeCategoryTree(List<Category> categories)
			throws ServiceException {
		if (CollectionsUtil.isNullOrEmpty(categories)) {
			return null;
		}
		try {
			Long userId = loggedInChecker.getCurrentUserId();
			List<Category> storeList = new ArrayList<Category>();
			for (Category category : categories) {
				int actionIndex = category.getAction().getActionIndex();
				if (Action.DO_NOTHING == actionIndex) {
					continue;
				}
				if (Action.CREATE_NEW == actionIndex) {
					category.setUserId(userId);
				}
				if (isDuplicate(category)) {
					category.setMessage(Constants.MSG_DUPLICATE);
					category.setMessageType(MessageType.TYPE_ERROR);
					continue;
				} else {
					category.setMessage(Constants.MSG_SUCCESSFUL);
					category.setMessageType(MessageType.TYPE_SUCCESS);
				}
				logger.debug("category to be stored: " + category.getName());
				storeList.add(category);
			}
			if (CollectionsUtil.isNonEmpty(storeList)) {
				dao.saveAll(storeList);
			}
			List<Category> storeChildList = new ArrayList<Category>();
			for (Category c : categories) {
				List<Category> children = c.getChildren();
				if (CollectionsUtil.isNonEmpty(children)) {
					for (Category child : children) {
						int actionIndex = child.getAction().getActionIndex();
						if (Action.DO_NOTHING != actionIndex) {
							child.setParentCategoryId(c.getId());
							child.setUserId(userId);
							if (isDuplicate(child)) {
								child.setMessage(Constants.MSG_DUPLICATE);
								child.setMessageType(MessageType.TYPE_ERROR);
								continue;
							} else {
								child.setMessage(Constants.MSG_SUCCESSFUL);
								child.setMessageType(MessageType.TYPE_SUCCESS);
							}
							storeChildList.add(child);
						}
					}
				}
			}
			if (CollectionsUtil.isNonEmpty(storeChildList)) {
				dao.saveAll(storeChildList);
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return categories;
	}
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public List<Account> storeAccounts(List<Account> accounts)
			throws ServiceException {
		try {
			Long userId = loggedInChecker.getCurrentUserId();
			for (Account a : accounts) {
				int actionIndex = a.getAction().getActionIndex();
				if (Action.CREATE_NEW == actionIndex) {
					a.setUserId(userId);
				}
			}
			dao.saveAll(accounts);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return accounts;
	}
	
	@Override
	public Category getCategoryByName(String categoryName)
			throws ServiceException {
		try {
			Category c = (Category) dao.getEntity(Category.class, 
					Category.NAME, categoryName);
			return c;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Account> listAccounts(Page page) throws ServiceException {
		List<Account> accounts = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Account.class);
		try {
			accounts = (List<Account>) dao.listAll(querySpec, page);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return accounts;
	}

	@Override
	public Account getAccountByName(String accountName) throws ServiceException {
		try {
			Account a = (Account) dao.getEntity(Account.class, 
					Account.NAME, accountName);
			return a;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Category> listCategories(boolean parentsOnly, Page page) throws ServiceException {
		List<Category> categories = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Category.class);		
		try {
			categories = (List<Category>) dao.listAll(querySpec, page);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return categories;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Account> listAccountsByType(AccountType accountType, Page page)
			throws ServiceException {
		if (accountType == null) {
			throw new IllegalArgumentException("given accountType is null");
		}
		List<Account> accounts = null;
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Account.class);
		querySpec.addPropertySpec(new PropertySpec(Account.ACCT_TYPE,
				Operation.EQ, accountType.getCode()));
		try {
			accounts = (List<Account>) dao.listAll(querySpec, page);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return accounts;
	}

	@Override
	public List<Category> listCategories(DomainSearchDto dto)
			throws ServiceException {
		if (dto == null) {
			throw new IllegalArgumentException("dto is null...");
		}
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Category.class);
		List<Long> categoryIds = dto.getCategoryIds();
		if (CollectionsUtil.isNonEmpty(categoryIds)) {
			querySpec.addPropertySpec(new PropertySpec(Category.ID, Operation.IN, categoryIds));
		}
		
		String tranType = dto.getTransactionType();
		if (tranType != null) {
			querySpec.addPropertySpec(new PropertySpec(Category.TRAN_TYPE, tranType));
		}
		List<OrderBySpec> orderByList = dto.getOrderByList();
		if (CollectionsUtil.isNonEmpty(orderByList)) {
			querySpec.setOrderBySpecs(orderByList);
		}
		Page page = dto.getPage();
		List<? extends Entity> entities = list(querySpec, page);
		updateParentCategory(entities);
		List<Category> categories = (List<Category>) entities;
		return categories;
	}

	private List<? extends Entity> list(QuerySpec querySpec, Page page) throws ServiceException {
		List<? extends Entity> entities = null;
		try {
			entities = dao.listAll(querySpec, page);	
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return entities;
	}

	private void updateParentCategory(List<? extends Entity> categories) {
		if (CollectionsUtil.isNonEmpty(categories)) {
			Map<Long, Entity> map = Util.getMap(categories);
			for (Entity entity : categories) {
				Category c = (Category) entity;
				Long parentId = c.getParentCategoryId();
				if (parentId != null) {
					Category parent = (Category) map.get(parentId);
					c.setParent(parent);
				}
			}
		}
	}

	@Override
	public List<Account> listAccounts(DomainSearchDto dto)
			throws ServiceException {
		if (dto == null) {
			throw new IllegalArgumentException("dto is null...");
		}
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Account.class);
		List<Long> accountIds = dto.getAccountIds();
		if (CollectionsUtil.isNonEmpty(accountIds)) {
			querySpec.addPropertySpec(new PropertySpec(Account.ID, Operation.IN, accountIds));
		}
		
		String acctType = dto.getAccountType();
		if (acctType != null) {
			querySpec.addPropertySpec(new PropertySpec(Account.ACCT_TYPE, acctType));
		}
		List<OrderBySpec> orderByList = dto.getOrderByList();
		if (CollectionsUtil.isNonEmpty(orderByList)) {
			querySpec.setOrderBySpecs(orderByList);
		}
		Page page = dto.getPage();
		return (List<Account>) list(querySpec, page);
	}

	@Override
	public Map<Long, Category> categoriesCacheMap() throws ServiceException {
		if (categoriesCacheMap == null) {
			this.categoriesCacheMap = new HashMap<Long, Category>();
		}
		List<Category> list = listCategories(new DomainSearchDto());
		Map<Long, Entity> map = Util.getMap(list);
		for (Long id : map.keySet()) {
			Category c = (Category) map.get(id);
			categoriesCacheMap.put(id, c);
		}
		return categoriesCacheMap;
	}

	@Override
	public Map<Long, Account> accountsCacheMap() throws ServiceException {
		if (accountsCacheMap == null) {
			this.accountsCacheMap = new HashMap<Long, Account>();
		}
		List<Account> list = listAccounts(new DomainSearchDto());
		Map<Long, Entity> map = Util.getMap(list);
		for (Long id : map.keySet()) {
			Account a = (Account) map.get(id);
			accountsCacheMap.put(id, a);
		}
		return accountsCacheMap;
	}
	
	@Override
	public Account getAccountById(Long id) throws ServiceException {
		try {
			Account a = (Account) dao.getEntity(Account.class, id);
			return a;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Category getCategoryById(Long catId) throws ServiceException {
		try {
			Category c = (Category) dao.getEntity(Category.class, catId);
			return c;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public PageableList<Category> listCategoryTree(DomainSearchDto dto)
			throws ServiceException {
		if (dto == null) {
			throw new IllegalArgumentException("dto is null...");
		}
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Category.class);
		List<Long> categoryIds = dto.getCategoryIds();
		if (CollectionsUtil.isNonEmpty(categoryIds)) {
			querySpec.addPropertySpec(new PropertySpec(Category.ID, Operation.IN, categoryIds));
		}
		
		String tranType = dto.getTransactionType();
		if (tranType != null) {
			querySpec.addPropertySpec(new PropertySpec(Category.TRAN_TYPE, tranType));
		}
		querySpec.addPropertySpec(new PropertySpec(Category.PARENT_ID, Operation.ISNULL, null));
		List<OrderBySpec> orderByList = dto.getOrderByList();
		if (CollectionsUtil.isNonEmpty(orderByList)) {
			querySpec.setOrderBySpecs(orderByList);
		}
		Page page = dto.getPage();
		List<? extends Entity> entities = list(querySpec, page);
		PageableList<Category> parentCategories = (PageableList<Category>) entities;
		if (CollectionsUtil.isNullOrEmpty(parentCategories)) {
			return null;
		}	
		Set<Long> parentCatIds = new HashSet<Long>();
		for (Category parent : parentCategories) {
			Long parentCatId = parent.getId();
			parentCatIds.add(parentCatId);
		}
		dto.setCategoryIds(new ArrayList<Long>(parentCatIds));
		List<Category> childCategories = listChildCategories(dto);
		List<Category> categories = new ArrayList<Category>();
		categories.addAll(parentCategories);
		categories.addAll(childCategories);
		prepareCategoryTree(categories);
		return parentCategories;
	}

	private List<Category> prepareCategoryTree(List<Category> categories) {
		if (CollectionsUtil.isNonEmpty(categories)) {
			Map<Long, Entity> map = Util.getMap(categories);
			List<Category> categoryTree = new ArrayList<Category>(); 
			for (Category c : categories) {
				Long parentId = c.getParentCategoryId();
				if (parentId == null) {
					categoryTree.add(c);
				} else {
					Category parent = (Category) map.get(parentId);
					if (parent != null) {
						parent.addChild(c);
					}
				}
			}
			return categoryTree;
		}
		return categories;
	}
	
	@Override
	public List<Category> listParentCategories(DomainSearchDto dto)
			throws ServiceException {
		if (dto == null) {
			throw new IllegalArgumentException("dto is null...");
		}
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Category.class);
		querySpec.addPropertySpec(new PropertySpec(Category.PARENT_ID, Operation.ISNULL, null));
		Page page = dto.getPage();
		List<? extends Entity> entities = list(querySpec, page);
		List<Category> categories = (List<Category>) entities;
		return categories;
	}
	
	@Override
	public List<Category> listChildCategories(DomainSearchDto dto)
			throws ServiceException {
		if (dto == null) {
			throw new IllegalArgumentException("dto is null...");
		}
		List<Long> parentCategoryIds = dto.getCategoryIds();
		if (CollectionsUtil.isNullOrEmpty(parentCategoryIds)) {
			throw new IllegalArgumentException("given parentIds collection is null or empty...");
		}
		QuerySpec querySpec = loggedInChecker.getQuerySpec(Category.class);
		querySpec.addPropertySpec(new PropertySpec(Category.PARENT_ID, Operation.IN, parentCategoryIds));
		List<OrderBySpec> orderByList = dto.getOrderByList();
		if (CollectionsUtil.isNonEmpty(orderByList)) {
			querySpec.setOrderBySpecs(orderByList);
		}
		Page page = null; // don't want to have paging on child items
		List<? extends Entity> entities = list(querySpec, page);
		List<Category> categories = (List<Category>) entities;
		return categories;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public boolean deleteAccount(Long id) throws ServiceException {
		if (id == null) {
			throw new ValidationException("given id is null");
		}
		try {
			dao.removeEntity(Account.class, id);
			return true;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public boolean deleteCategory(Long id) throws ServiceException {
		if (id == null) {
			throw new ValidationException("given id is null");
		}
		try {
			dao.removeEntity(Category.class, id);
			return true;
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Long, Entity> fetchCategoryMap(Set<Long> ids) throws ServiceException {
		DomainSearchDto dto = new DomainSearchDto();
		dto.setCategoryIds(new ArrayList<Long>(ids));
		List<Category> categories = listCategories(dto);
		Map<Long, Entity> map = com.maqs.moneytracker.common.util.Util
				.getMap(categories);
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PageableList<Category> listSystemCategoryTree(Page page)
			throws ServiceException {
		User systemUser = userService.getSystemUser();
		if (systemUser == null) {
			throw new ServiceException("no system user found");
		}
		QuerySpec querySpec = new QuerySpec(Category.class.getName());
		querySpec.addPropertySpec(new PropertySpec(Constants.USER_ID, systemUser.getId()));
		querySpec.addPropertySpec(new PropertySpec(Category.PARENT_ID, Operation.ISNULL, null));
		PageableList<Category> parentCategories = (PageableList<Category>) list(querySpec, page);
		if (CollectionsUtil.isNullOrEmpty(parentCategories)) {
			return null;
		}	
		Set<Long> parentCatIds = new HashSet<Long>();
		for (Category parent : parentCategories) {
			Long parentCatId = parent.getId();
			parentCatIds.add(parentCatId);
		}
		QuerySpec childrenQuerySpec = new QuerySpec(Category.class.getName());
		childrenQuerySpec.addPropertySpec(new PropertySpec(Constants.USER_ID, systemUser.getId()));
		childrenQuerySpec.addPropertySpec(new PropertySpec(Category.PARENT_ID, Operation.IN, parentCatIds));
		List<Category> childCategories = (List<Category>) list(childrenQuerySpec, null);
		
		List<Category> categories = new ArrayList<Category>();
		categories.addAll(parentCategories);
		categories.addAll(childCategories);
		prepareCategoryTree(categories);
		
		return parentCategories;
	}

	@Override
	public PageableList<Category> listCategories(QuerySpec querySpec, Page page)
			throws ServiceException {
		PageableList<Category> parentCategories = (PageableList<Category>) list(querySpec, page);
		if (CollectionsUtil.isNullOrEmpty(parentCategories)) {
			return null;
		}	
		Set<Long> parentCatIds = new HashSet<Long>();
		for (Category parent : parentCategories) {
			Long parentCatId = parent.getId();
			parentCatIds.add(parentCatId);
		}
		DomainSearchDto dto = new DomainSearchDto();
		dto.setCategoryIds(new ArrayList<Long>(parentCatIds));
		List<Category> childCategories = listChildCategories(dto);
		List<Category> categories = new ArrayList<Category>();
		categories.addAll(parentCategories);
		categories.addAll(childCategories);
		prepareCategoryTree(categories);
		
		return parentCategories;
	}
	
	@Override
	public long totalCount(Class<? extends BaseEntity> clazz) throws ServiceException {
		long count = 0;
		try {
			QuerySpec querySpec = loggedInChecker.getQuerySpec(clazz);
			count = dao.count(querySpec);	
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return count;
	}
	
	public boolean isDuplicate(Category c) throws ServiceException {
		boolean duplicate = false;
		try {
			QuerySpec querySpec = loggedInChecker.getQuerySpec(Category.class);
			querySpec.addPropertySpec(new PropertySpec(Category.NAME, c.getName()));
			Long parentId = c.getParentCategoryId();
			if (parentId != null) {
				querySpec.addPropertySpec(new PropertySpec(Category.PARENT_ID, parentId));
			}
			Long count = dao.count(querySpec);
			int action = c.getAction().getActionIndex();
			duplicate = Action.CREATE_NEW == action ? count > 0 : count > 1;
			logger.debug("isDuplicate: " + c.getName() + "? " + duplicate);
		} catch (DataAccessException e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return duplicate;
	}
}
