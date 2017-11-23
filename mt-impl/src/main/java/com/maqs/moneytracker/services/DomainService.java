package com.maqs.moneytracker.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.list.PageableList;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.dto.DomainSearchDto;
import com.maqs.moneytracker.model.Account;
import com.maqs.moneytracker.model.BaseEntity;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.types.AccountType;
import com.maqs.moneytracker.types.TransactionType;

public interface DomainService {

	public List<Category> listByType(TransactionType transactionType, Page page)
			throws ServiceException;

	public List<Category> storeCategories(List<Category> categories)
			throws ServiceException;
	
	public List<Category> storeCategoryTree(List<Category> categories)
			throws ServiceException;
	
	public List<Account> storeAccounts(List<Account> accounts)
			throws ServiceException;
	
	public Category getCategoryByName(String categoryName)
			throws ServiceException;

	public List<Account> listAccounts(Page page) throws ServiceException;

	public Account getAccountByName(String accountName) throws ServiceException;

	public List<Category> listCategories(boolean parentsOnly, Page page)
			throws ServiceException;

	public List<Account> listAccountsByType(AccountType accountType, Page page)
			throws ServiceException;

	public List<Category> listCategories(DomainSearchDto dto) throws ServiceException;
	
	public List<Account> listAccounts(DomainSearchDto dto) throws ServiceException;
	
	public Map<Long, Category> categoriesCacheMap() throws ServiceException;
	
	public Map<Long, Account> accountsCacheMap() throws ServiceException;
	
	public Account getAccountById(Long id) throws ServiceException;

	public Category getCategoryById(Long catId) throws ServiceException;

	public PageableList<Category> listCategoryTree(DomainSearchDto dto) throws ServiceException;
	
	public List<Category> listParentCategories(DomainSearchDto dto) throws ServiceException;

	public List<Category> listChildCategories(DomainSearchDto domainSearchDto) throws ServiceException;
	
	public boolean deleteAccount(Long id) throws ServiceException;
	
	public boolean deleteCategory(Long id) throws ServiceException;
	
	public Map<Long, Entity> fetchCategoryMap(Set<Long> ids) throws ServiceException;

	public PageableList<Category> listSystemCategoryTree(Page page) throws ServiceException;
	
	public PageableList<Category> listCategories(QuerySpec querySpec, Page page)
			throws ServiceException;
	
	public long totalCount(Class<? extends BaseEntity> clazz) throws ServiceException;
	
}
