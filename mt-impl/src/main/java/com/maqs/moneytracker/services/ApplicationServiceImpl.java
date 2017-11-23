package com.maqs.moneytracker.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maqs.moneytracker.common.Constants;
import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.common.transferobjects.Action;
import com.maqs.moneytracker.common.util.AppUtil;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.dto.SetupActivityDto;
import com.maqs.moneytracker.model.Account;
import com.maqs.moneytracker.model.BankStatement;
import com.maqs.moneytracker.model.Budget;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.model.Setting;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.security.LoggedInChecker;
import com.maqs.moneytracker.server.core.dao.IDao;
import com.maqs.moneytracker.server.core.exception.DataAccessException;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDao dao;

	@Autowired
	private LoggedInChecker loggedInChecker;

	@Autowired
	private DomainService domainService;
	
	@Autowired
	private TransactionService transactionService;
	
	public ApplicationServiceImpl() {

	}

	public ApplicationServiceImpl(IDao dao) {
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
	public List<Setting> listAll(Page page) throws ServiceException {
		List<Setting> settings = null;
		try {
			QuerySpec settingQuery = loggedInChecker
					.getQuerySpec(Setting.class);
			settings = (List<Setting>) dao.listAll(settingQuery, page);
			logger.debug("settings "
					+ (CollectionsUtil.isNonEmpty(settings) ? settings.size()
							: 0) + " records found.");
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return settings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public List<Setting> saveAll(List<Setting> changedList)
			throws ServiceException {
		try {
			Long userId = loggedInChecker.getCurrentUserId();
			for (Setting setting : changedList) {
				int actionIndex = setting.getAction().getActionIndex();
				if (Action.CREATE_NEW == actionIndex) {
					setting.setUserId(userId);
				}
			}
			dao.saveAll(changedList);
			logger.debug("setting "
					+ (CollectionsUtil.isNonEmpty(changedList) ? changedList
							.size() : 0) + " records have been saved.");
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return changedList;
	}

	@Override
	public List<SetupActivityDto> listSetupActivity() throws ServiceException {
		List<SetupActivityDto> list = new ArrayList<SetupActivityDto>();
		
		long accountsCount = domainService.totalCount(Account.class);
		String accountsActivity = Constants.ACTIVITY_ACCT;
		int accountsPoints = Constants.ACTIVITY_ACCT_POINTS;
		String accountsResult = AppUtil.getFormattedText(Constants.ACTIVITY_TOTAL_RES, new Object[] { accountsCount } );
		SetupActivityDto accountsSetup = new SetupActivityDto(accountsActivity, accountsResult, accountsPoints);
		accountsSetup.setDone(accountsCount > 0);
		list.add(accountsSetup);
		
		long catCount = domainService.totalCount(Category.class);
		String catActivity = Constants.ACTIVITY_CAT;
		int catPoints = Constants.ACTIVITY_CAT_POINTS;
		String catResult = AppUtil.getFormattedText(Constants.ACTIVITY_TOTAL_RES, new Object[] { catCount } );
		SetupActivityDto catSetup = new SetupActivityDto(catActivity, catResult, catPoints);
		catSetup.setDone(catCount >= Constants.ACTIVITY_CAT_MIN);
		list.add(catSetup);
		
		long tranCount = domainService.totalCount(Transaction.class);
		String tranActivity = Constants.ACTIVITY_TRAN;
		int tranPoints = Constants.ACTIVITY_TRAN_POINTS;
		String tranResult = AppUtil.getFormattedText(Constants.ACTIVITY_TOTAL_RES, new Object[] { tranCount } );
		SetupActivityDto tranSetup = new SetupActivityDto(tranActivity, tranResult, tranPoints);
		tranSetup.setDone(tranCount > 0);
		list.add(tranSetup);
		
		long budgetCount = domainService.totalCount(Budget.class);
		String budgetActivity = Constants.ACTIVITY_BUDGET;
		int budgetPoints = Constants.ACTIVITY_BUDGET_POINTS;
		String budgetResult = AppUtil.getFormattedText(Constants.ACTIVITY_TOTAL_RES, new Object[] { budgetCount } );
		SetupActivityDto budgetSetup = new SetupActivityDto(budgetActivity, budgetResult, budgetPoints);
		budgetSetup.setDone(budgetCount > 0);
		list.add(budgetSetup);
		
		long statementCount = domainService.totalCount(BankStatement.class);
		String statementActivity = Constants.ACTIVITY_STATEMENTS;
		int statementPoints = Constants.ACTIVITY_STATEMENT_POINTS;
		String statementResult = AppUtil.getFormattedText(Constants.ACTIVITY_TOTAL_RES, new Object[] { statementCount } );
		SetupActivityDto statementSetup = new SetupActivityDto(statementActivity, statementResult, statementPoints);
		statementSetup.setDone(statementCount > 0);
		list.add(statementSetup);
		
		logger.debug("listSetupActivity "
				+ (CollectionsUtil.isNonEmpty(list) ? list
						.size() : 0) + " records have been listed.");
		return list;
	}
}
