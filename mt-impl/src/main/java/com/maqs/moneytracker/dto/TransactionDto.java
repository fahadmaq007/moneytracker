package com.maqs.moneytracker.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.common.util.DateUtil;

@JsonInclude(Include.NON_NULL)
public class TransactionDto {

	public static final String TRAN_TYPE = "tranType";

	public static final String ON_DATE = "onDate";

	private Date onDate;

	private BigDecimal amount;

	private String tranType;
	
	private String catType;
	
	private Long catId;
	
	private BigDecimal expenseTotal;

	private BigDecimal incomeTotal;
	
	private BigDecimal percent;
	
	private List<TransactionDto> children;
	
	private String period;
	
	public TransactionDto() {
		
	}

	public TransactionDto(Date onDate, String tranType, BigDecimal amount) {
		this.onDate = onDate;
		this.tranType = tranType;
		this.amount = amount;
	}
	
	public Date getOnDate() {
		return onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
		if (onDate != null) {
			String period = DateUtil.getMonthYear(onDate);
			setPeriod(period);
		}
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}
	
	public String getCatType() {
		return catType;
	}
	
	public Long getCatId() {
		return catId;
	}
	
	public void setCatId(Long catId) {
		this.catId = catId;
	}
	
	public BigDecimal getIncomeTotal() {
		return incomeTotal;
	}
	
	public void setIncomeTotal(BigDecimal incomeTotal) {
		this.incomeTotal = incomeTotal;
	}
	
	public BigDecimal getExpenseTotal() {
		return expenseTotal;
	}
	
	public void setExpenseTotal(BigDecimal expenseTotal) {
		this.expenseTotal = expenseTotal;
	}
	
	public BigDecimal getPercent() {
		return percent;
	}
	
	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}
	
	public List<TransactionDto> getChildren() {
		return children;
	}

	public void setChildren(List<TransactionDto> children) {
		this.children = children;
	}

	public void addChild(TransactionDto c) {
		if (children == null) {
			children = new ArrayList<TransactionDto>();
		}
		children.add(c);
	}

	public void removeChild(TransactionDto c) {
		children.remove(c);
	}
	
	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
