package com.maqs.moneytracker.common.paging.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.maqs.moneytracker.common.Constansts;
import com.maqs.moneytracker.common.paging.Page;

/**
 * PageableList has the paging capabilities with and implementation of List.
 * It is a decorator which adds paging functionality to existing implementation of list {actualList}.
 * @author maqbool.ahmed
 *
 * @param <E>
 */
public class PageableList<E> extends ArrayList<E> implements List<E> {

	private static final long serialVersionUID = -1562604675536616297L;
	
	private final List<E> actualList;
	
	private final Page page;
	
	private final Logger logger = Logger.getLogger(getClass());
	
	public PageableList(List<E> actualList, Page page) {
		this.actualList = actualList;
		this.page = page;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return actualList == null ? 0 : actualList.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return actualList == null ? true : actualList.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(Object o) {
		return actualList.contains(o);
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<E> iterator() {
		return actualList == null ? null : actualList.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return actualList.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T[] toArray(T[] a) {
		return actualList.toArray(a);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean add(E e) {
		return actualList.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean remove(Object o) {
		return actualList.remove(o);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsAll(Collection<?> c) {
		return actualList.containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean addAll(Collection<? extends E> c) {
		return actualList.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		return actualList.addAll(index, c);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean removeAll(Collection<?> c) {
		return actualList.removeAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean retainAll(Collection<?> c) {
		return actualList.retainAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		actualList.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public E get(int index) {
		return actualList.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	public E set(int index, E element) {
		return actualList.set(index, element);
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(int index, E element) {
		actualList.add(index, element);
	}

	/**
	 * {@inheritDoc}
	 */
	public E remove(int index) {
		return actualList.remove(index);
	}

	/**
	 * {@inheritDoc}
	 */
	public int indexOf(Object o) {
		return actualList.indexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */
	public int lastIndexOf(Object o) {
		return actualList.lastIndexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */
	public ListIterator<E> listIterator() {
		return actualList.listIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public ListIterator<E> listIterator(int index) {
		return actualList.listIterator(index);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<E> subList(int fromIndex, int toIndex) {
		return actualList.subList(fromIndex, toIndex);
	}
	
	/**
	 * Refreshes the list with the supplied one.
	 * Clears all the existing elements & then adds the supplied list's elements
	 * @param withThisList the list elements to be added
	 */
	public void refreshList(List<E> withThisList) {
		logger.debug("refreshing the pageable list...");
		clear();
		addAll(withThisList);
		logger.debug("refreshed the pageable list with supplied elements.");
	}
	
	/**
	 * Get the page instance.
	 * @return page
	 */
	public Page getPage() {
		return page;
	}
	
	/**
	 * Get current page index.
	 * @return currentPageIndex
	 */
	public int getCurrentPageIndex() {
		int currentPageIndex = -1;
		if (page != null) {
			currentPageIndex = page.getPageNumber();
		}
		return currentPageIndex;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(Constansts.OPEN_BRACKET);
		if (actualList != null) {
			builder.append(actualList);
		} 
		if (page != null) {
			builder.append(",page=").append(page);
		}
		builder.append(Constansts.CLOSE_BRACKET);
		return builder.toString();
	}
}
