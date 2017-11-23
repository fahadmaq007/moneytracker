package com.maqs.moneytracker.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.services.DomainService;
import com.maqs.moneytracker.types.TransactionType;

public class DomainTest extends BaseTestCase {

	@Autowired
	private DomainService domainService;
	
	public DomainTest() {
	}
	
//	@Test
	public void testStoreCategorys() throws ServiceException {
		Category c1 = new Category("Salary");
		c1.setTransactionType(TransactionType.valueOf(TransactionType.INCOME));
		List<Category> list = domainService.storeCategories(Arrays.asList(c1));
		Assert.assertTrue(list.size() > 0);
	}
	
	@Test
	public void testCategoryFilter() throws ServiceException {
		List<Category> list = (List<Category>) domainService.listByType(
				TransactionType.valueOf(TransactionType.EXPENSE), new Page());
		Assert.assertTrue(list.size() > 0);
	}
	
}
