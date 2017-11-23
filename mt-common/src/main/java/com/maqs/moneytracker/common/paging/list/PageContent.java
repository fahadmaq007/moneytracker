package com.maqs.moneytracker.common.paging.list;

import java.util.List;

import com.maqs.moneytracker.common.paging.Page;

/**
 * PageableList has the paging capabilities with and implementation of List.
 * It is a decorator which adds paging functionality to existing implementation of list {actualList}.
 * @author maqbool.ahmed
 *
 * @param <E>
 */
public class PageContent<E> {

	private List<E> contentList;
	
	private Page page;
	
	public PageContent(List<E> contentList, Page page) {
		this.contentList = contentList;
		this.page = page;
	}

	public List<E> getContentList() {
		return contentList;
	}

	public void setContentList(List<E> contentList) {
		this.contentList = contentList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	
}
