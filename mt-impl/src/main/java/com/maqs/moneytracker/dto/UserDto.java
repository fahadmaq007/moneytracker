package com.maqs.moneytracker.dto;


public class UserDto {

	private String token;
	
	private String name;
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
