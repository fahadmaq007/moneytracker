package com.maqs.moneytracker.common.paging;

import static com.maqs.moneytracker.common.Constansts.CLOSE_BRACKET;
import static com.maqs.moneytracker.common.Constansts.OPEN_BRACKET;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Page is an instance to help paging.
 * It defines the current pageNumber that we are dealing with & the pageSize.
 * The totalRecords will be updated at the DAO layer by querying the count().
 * 
 * @author maqbool.ahmed
 */
@XmlRootElement(name = "page")
@XmlAccessorType(XmlAccessType.FIELD)
public class Page implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1534779859274552558L;

	private int pageNumber;
	
	private int pageSize;
	
	private long totalRecords;
	
	private static final int DEFAULT_PAGE = 1;
	
	private static final int DEFAULT_PAGESIZE = 25;
	
	/**
	 * Constructs with the default page & default page size.
	 */
	public Page() {
		this(DEFAULT_PAGESIZE);
	}
	
	/**
	 * Constructs with the specified page size & default page.
	 * @param pageSize page size
	 */
	public Page(int pageSize) {
		this(DEFAULT_PAGE, pageSize);
	}
	
	/**
	 * Constructs with the specified page size & page.
	 * @param pageNumber page number
	 * @param pageSize page size
	 */
	public Page(int pageNumber, int pageSize) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	/**
	 * Returns the current page number.
	 * @return current pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * Set the pageNumber.
	 * @param pageNumber
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * Returns the pageSize.
	 * @return pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * PageSize setter
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Returns total records the paging has.
	 * @return totalRecords
	 */
	public long getTotalRecords() {
		return totalRecords;
	}

	/**
	 * Set the totalRecords
	 * @param totalRecords
	 */
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	/**
	 * Traverse to next page if available.
	 */
	public void nextPage() {
		final Page page = this;
		int pageNum = page.getPageNumber();
		int pageSize = page.getPageSize();
		long totalRecords = page.getTotalRecords();
		if (totalRecords == 0)
			return;
		int totalPages = (int) totalRecords / pageSize;
		if (pageNum < totalPages)
			page.setPageNumber(++pageNum);
	}
	
	/**
	 * Traverse to previous page if available.
	 * 
	 */
	public void prevPage() {
		Page page = this;
		int pageNum = page.getPageNumber();
		if (pageNum <= 1)
			return;
		
		page.setPageNumber(--pageNum);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(OPEN_BRACKET + "pageNumber=" + pageNumber + ", pageSize=" + pageSize);
		if (totalRecords > 0) 
			builder.append(", totalRecords=").append(totalRecords);
		builder.append(CLOSE_BRACKET);
		return builder.toString();
	}
}
