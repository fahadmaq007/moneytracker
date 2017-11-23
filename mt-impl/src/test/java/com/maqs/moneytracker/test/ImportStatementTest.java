package com.maqs.moneytracker.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.model.BankStatement;
import com.maqs.moneytracker.model.Category;
import com.maqs.moneytracker.services.DomainService;
import com.maqs.moneytracker.services.ImportService;
import com.maqs.moneytracker.types.TransactionType;

public class ImportStatementTest extends BaseTestCase {

	@Autowired
	private ImportService importService;
	
	public ImportStatementTest() {
	}
		
	@Test
	public void testGetStatementDetails() throws ServiceException {
		BankStatement b = getStatement();
		
		b = importService.getBankStatementDetails(b);
		Assert.assertTrue(b != null);
		Assert.assertTrue(b.getColumnMaps().size() > 0);
		Assert.assertTrue(b.getDataMaps().size() > 0);
	}
	
	private BankStatement getStatement() {
		BankStatement b = new BankStatement();
		b.setId(Long.valueOf(4));
		b.setBankAccountId(b.getId());
		b.setStartRow(2);
		b.setEndRow(10);
		b.setDateFormat("dd/MM/yyyy");
		return b;
	}
	
}
