package com.maqs.moneytracker.types;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.model.BaseEntity;

/**
 * Roles 
 * 
 * @author Maqbool.Ahmed
 * 
 */
@JsonInclude(Include.NON_NULL)
public class Role extends BaseEntity implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2569568616213048356L;

	public static final String USER = "U";

	public static final String ADMIN = "A";

	public static final String ROLE_USER = "ROLE_USER";
	
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	private String code;

	private String name;

	public Role() {
		this(USER);
	}
	
	public Role(String code) {
		Role t = valueOf(code);
		this.code = t.code;
		this.name = t.name;
	}
	
	public Role(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		Role role = valueOf(code);
		if (role != null) {
			this.code = role.code;
			this.name = role.name;
		}
	}

	public static Role valueOf(String code) {
		return roles.get(code);
	}
	
	public static Role valueByName(String name) {
		return rolesByName.get(name);
	}
	
	private static Map<String, Role> roles = new LinkedHashMap<String, Role>();
	
	private static Map<String, Role> rolesByName = new LinkedHashMap<String, Role>();
	
	static {
		Role userRole = new Role(USER, ROLE_USER);
		roles.put(USER, userRole); 
		rolesByName.put(ROLE_USER, userRole); 
		
		Role adminRole = new Role(ADMIN, ROLE_ADMIN);
		roles.put(ADMIN, adminRole);
		rolesByName.put(ROLE_ADMIN, adminRole);
	}
	
	public static Collection<Role> values() {
		return roles.values();
	}
	
	public static Collection<String> keys() {
		return roles.keySet();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String getAuthority() {
		return getName();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
