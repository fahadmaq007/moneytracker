package com.maqs.moneytracker.web.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.list.PageContent;
import com.maqs.moneytracker.common.paging.list.PageableList;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.DomainSearchDto;
import com.maqs.moneytracker.model.Account;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.services.DomainService;
import com.maqs.moneytracker.types.AccountType;
import com.maqs.moneytracker.types.Period;
import com.wordnik.swagger.annotations.Api;

/**
 * The REST webservices are exposed to the external world.
 * 
 * @author maqbool.ahmed
 */
@Controller
@RequestMapping("/api/domain")
@Api(value = "domain", description = "Domain API")
@Secured("ROLE_USER")
public class DomainController {

	@Autowired
	private DomainService domainService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Lists the Categories by given search criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Project Dtos
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public @ResponseBody List<Category> listCategories(
			@RequestBody DomainSearchDto dto) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listCategories method is been called");

		List<Category> categories = domainService.listCategories(dto);
		if (logger.isInfoEnabled())
			logger.info("listCategories() has listed "
					+ (categories == null ? 0 : categories.size()) + " records");

		return categories;
	}

	/**
	 * Lists the Accounts by given search criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Project Dtos
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public @ResponseBody List<Account> listAccounts(
			@RequestBody DomainSearchDto dto) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listAccounts method is been called");

		List<Account> accounts = domainService.listAccounts(dto);
		if (logger.isInfoEnabled())
			logger.info("listAccounts() has listed "
					+ (accounts == null ? 0 : accounts.size()) + " records");

		return accounts;
	}

	/**
	 * Lists the Categories by given search criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Project Dtos
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/categorytree", method = RequestMethod.POST)
	public @ResponseBody PageContent<Category> listCategoryTree(
			@RequestBody DomainSearchDto dto) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listCategoryTree method is been called");

		PageableList<Category> categories = domainService.listCategoryTree(dto);
		if (logger.isInfoEnabled())
			logger.info("listCategoryTree() has listed "
					+ (categories == null ? 0 : categories.size()) + " records");

		PageContent<Category> pageContent = new PageContent<Category>(
				categories, categories.getPage());
		return pageContent;
	}

	@RequestMapping(value = "/periods", method = RequestMethod.GET)
	public @ResponseBody List<Period> listPeriod() throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listPeriod method is been called");

		List<Period> periods = new ArrayList<Period>(Period.values());
		return periods;
	}

	@RequestMapping(value = "/accounttypes", method = RequestMethod.GET)
	public @ResponseBody List<AccountType> listAccountTypes()
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listAccountTypes method is been called");
		List<AccountType> types = new ArrayList<AccountType>(
				AccountType.values());
		return types;
	}

	@RequestMapping(value = "/categories", method = RequestMethod.PUT)
	public @ResponseBody List<Category> storeCategories(
			@RequestBody List<Category> categories) throws ServiceException {
		logger.debug("storeCategories method is been called");
		return domainService.storeCategories(categories);
	}

	@RequestMapping(value = "/categorytree", method = RequestMethod.PUT)
	public @ResponseBody List<Category> storeCategoryTree(
			@RequestBody List<Category> categories) throws ServiceException {
		logger.debug("storeCategoryTree method is been called");
		return domainService.storeCategoryTree(categories);
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.PUT)
	public @ResponseBody List<Account> storeAccounts(
			@RequestBody List<Account> accounts) throws ServiceException {
		logger.debug("storeAccounts method is been called");
		return domainService.storeAccounts(accounts);
	}

	/**
	 * Deletes the account.
	 * 
	 * @param id
	 *            account id to be deleted.
	 * @return true if deleted, otherwise false.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
	public @ResponseBody boolean deleteAccount(@PathVariable("id") Long id)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("deleteAccount method is been called");
		return domainService.deleteAccount(id);
	}

	/**
	 * Deletes the Category.
	 * 
	 * @param id
	 *            category id to be deleted.
	 * @return true if deleted, otherwise false.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
	public @ResponseBody boolean deleteCategory(@PathVariable("id") Long id)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("deleteAccount method is been called");
		return domainService.deleteCategory(id);
	}

	/**
	 * Lists the Categories by given search criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Project Dtos
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/systemcategories", method = RequestMethod.POST)
	public @ResponseBody PageContent<Category> importSystemCategoryTree(
			@RequestBody Page page) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listCategoryTree method is been called");

		PageableList<Category> categories = domainService
				.listSystemCategoryTree(page);
		if (logger.isInfoEnabled())
			logger.info("importSystemCategoryTree() has listed "
					+ (categories == null ? 0 : categories.size()) + " records");

		PageContent<Category> pageContent = new PageContent<Category>(
				categories, categories.getPage());
		return pageContent;
	}
}
