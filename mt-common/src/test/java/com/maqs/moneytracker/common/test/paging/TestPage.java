package com.maqs.moneytracker.common.test.paging;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import com.maqs.moneytracker.common.paging.Page;

public class TestPage {
	
	@Test
	public void testPage() throws JAXBException, IOException {
		Page page = new Page(1, 20);
		Assert.assertEquals(page.getPageSize(), 20);
		
		page.setTotalRecords(page.getPageSize() * 5);
		page.nextPage();
		Assert.assertEquals(page.getPageNumber(), 2);
	}
}
