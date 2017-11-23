package com.maqs.moneytracker.common;

public interface BeanCallback<T> {
	String getString(T t);
	
	void setString(T t, String s);
}
