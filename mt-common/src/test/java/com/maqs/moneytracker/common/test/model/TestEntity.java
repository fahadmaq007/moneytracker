package com.maqs.moneytracker.common.test.model;

import org.junit.Assert;
import org.junit.Test;

import com.maqs.moneytracker.common.transferobjects.Entity;

public class TestEntity {
	
	@Test
	public void testEqualsOperation() {
		Entity e1 = new Entity();
		e1.setId(Long.valueOf(10));
		
		Entity e2 = new Entity();
		e2.setId(Long.valueOf(10));
		
		Assert.assertEquals(e1.getId(), e2.getId());	
	}
	@Test
	public void testEqualsOperation1() {
		Entity e1 = new Entity();
		e1.setId(Long.valueOf(10));
		
		Entity e2 = new Entity();
		e2.setId(Long.valueOf(10));
		
		Assert.assertEquals(e1.getId(), e2.getId());	
	}
}
