package com.maqs.moneytracker.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:conf/moneytracker-beans.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTestCase extends AbstractJUnit4SpringContextTests {

	protected Logger logger = Logger.getLogger(getClass());
	
	public BaseTestCase() {
		
	}
	
	@Test
	public void baseTest() {
		
	}
}
