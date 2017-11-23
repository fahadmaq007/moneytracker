package com.maqs.moneytracker.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.spec.OrderBySpec;
import com.maqs.moneytracker.managers.reports.Report;

@JsonInclude(Include.NON_NULL)
public class TransactionSearchDto {

	public static final String TRAN_TYPE = "tranType";

	public static final String ON_DATE = "onDate";

	public static final String ACCT_ID = "accountId";

	public static final String CAT_ID = "categoryId";

	public static final int DEFAULT_NUM_OF_TRANSACTIONS_REQUIRED = 5;

	public static final String DESC = "description";
	
	private Date fromDate;

	private Date toDate;

	private String tranType;

	private List<Long> categoryIds;

	private List<Long> accountIds;

	private Page page;

	private List<OrderBySpec> orderByList;

	private int noOfTransactions = DEFAULT_NUM_OF_TRANSACTIONS_REQUIRED;
	
	private Report reportBy;
	
	private String description;
	
	public TransactionSearchDto() {

	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
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

	public int getNoOfTransactions() {
		return noOfTransactions;
	}
	
	public void setNoOfTransactions(int noOfTransactions) {
		this.noOfTransactions = noOfTransactions;
	}
	
	public Report getReportBy() {
		return reportBy;
	}
	
	public void setReportBy(Report reportBy) {
		this.reportBy = reportBy;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
