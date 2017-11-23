package com.maqs.moneytracker.test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.TransactionDto;
import com.maqs.moneytracker.model.Account;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.services.DomainService;
import com.maqs.moneytracker.services.TransactionService;
import com.maqs.moneytracker.types.AccountType;
import com.maqs.moneytracker.types.TransactionType;

public class TransactionTest extends BaseTestCase {

	@Autowired
	private DomainService domainService;

	@Autowired
	private TransactionService transactionService;

	public TransactionTest() {
	}

//	 @Test
	public void testStoreCategorys() throws ServiceException {
		Transaction t1 = new Transaction();
		Long one = Long.valueOf(1);
		t1.setAccountId(one);
		t1.setCategoryId(one);
		t1.setAmount(BigDecimal.valueOf(150));
		t1.setDescription("Petrol");
		List<Transaction> list = transactionService.store(Arrays.asList(t1));
		Assert.assertTrue(list.size() > 0);
	}

//	 @Test
	public void testCategoryFilter() throws ServiceException {
		List<Category> list = (List<Category>) domainService.listByType(
				TransactionType.valueOf(TransactionType.INCOME), new Page());
		Assert.assertTrue(list.size() > 0);
	}

//	@Test
	public void testAccountFilter() throws ServiceException {
		List<Account> list = (List<Account>) domainService
				.listAccountsByType(AccountType.valueOf(AccountType.BANK), new Page());
		Assert.assertTrue(list.size() > 0);
	}
	
//	@Test
	public void testMonthlyIncExpReport() throws ServiceException {
		List<TransactionDto> list = (List<TransactionDto>) transactionService
				.listMonthlyIncExpReport(null, new Page());
		System.out.println(list);
		Assert.assertTrue(list.size() > 0);
	}
	
//	@Test
	public void testMonthlyExpHistoryReport() throws ServiceException {
		QuerySpec querySpec = new QuerySpec();
		querySpec.addPropertySpec(new PropertySpec(Transaction.CAT_ID, Long.valueOf(2))); //Fuel
		querySpec.addPropertySpec(new PropertySpec(Category.TRAN_TYPE, TransactionType.EXPENSE));
		List<TransactionDto> list = (List<TransactionDto>) transactionService
				.listMonthlyHistoryReportByCat(querySpec, new Page());
		System.out.println(list);
		Assert.assertTrue(list.size() > 0);
	}
	
	@Test
	public void testMaxCat() throws ServiceException {
		QuerySpec querySpec = new QuerySpec();
		querySpec.addPropertySpec(new PropertySpec(Category.TRAN_TYPE, TransactionType.EXPENSE));
		Category c = transactionService.getMaxCategory(querySpec);
		System.out.println(c);
	}
}
