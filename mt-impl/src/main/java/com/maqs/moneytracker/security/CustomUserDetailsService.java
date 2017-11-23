package com.maqs.moneytracker.security;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.maqs.moneytracker.model.User;
import com.maqs.moneytracker.services.UserService;
import com.maqs.moneytracker.types.Role;

public class CustomUserDetailsService implements UserDetailsService {

	private final Logger logger = Logger.getLogger(getClass());

	private final UserService userService;

	@Autowired
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails details = null;
		try {
			User user = userService.getUserByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException(username);
			}
			List<Role> roles = userService
					.getRoles(user.getUsername());
			details = new MyUserDetails(user, roles);
			logger.debug("loadUserByUsername: " + details);
		} catch (UsernameNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
		return details;
	}
}
