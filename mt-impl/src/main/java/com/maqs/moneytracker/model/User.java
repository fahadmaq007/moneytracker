package com.maqs.moneytracker.model;

import java.util.ArrayList;
import java.util.List;

import com.maqs.moneytracker.types.Role;

public class User extends BaseEntity {

	/**
	 */
	private static final long serialVersionUID = 2290998341439828705L;

	public static final String USERNAME = "username";

	public static final String PASSWORD = "password";

	private String username;
	
	private String name;
	
	private String password;

	private List<Role> roles;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if (roles == null) {
			roles = new ArrayList<Role>();
		}
		roles.add(role);
	}

	public void removeRole(Role role) {
		if (roles == null) {
			logger.error("Cannot remove role from an empty list");
			return;
		}
		roles.remove(role);
	}
	
	@Override
	public String toString() {
		return getUsername();
	}
}
