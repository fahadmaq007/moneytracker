package com.maqs.moneytracker.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.spec.OrderBySpec;

@JsonInclude(Include.NON_NULL)
public class DomainSearchDto {

	public static final String TRAN_TYPE = "tranType";

	public static final String ON_DATE = "onDate";

	public static final String ACCT_ID = "accountId";

	public static final String CAT_ID = "categoryId";

	private String transactionType;

	private String accountType;
	
	private List<Long> categoryIds;

	private List<Long> accountIds;
	
	private Page page;

	private List<OrderBySpec> orderByList;
	
	public DomainSearchDto() {

	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String type) {
		this.transactionType = type;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String type) {
		this.accountType = type;
	}
	
	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<Long> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds) {
		this.accountIds = accountIds;
	}

	public void addCategoryId(Long id) {
		if (categoryIds == null) {
			categoryIds = new ArrayList<Long>();
		}
		categoryIds.add(id);
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<OrderBySpec> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(List<OrderBySpec> orderByList) {
		this.orderByList = orderByList;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
