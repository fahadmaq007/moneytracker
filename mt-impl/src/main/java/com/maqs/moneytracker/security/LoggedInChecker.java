package com.maqs.moneytracker.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.maqs.moneytracker.common.Constants;
import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.model.BaseEntity;
import com.maqs.moneytracker.model.User;

@Component
public class LoggedInChecker {
	private final Logger logger = Logger.getLogger(getClass());

	public User getLoggedInUser() {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		logger.debug("getLoggedInUser(): " + authentication);
		if (authentication != null) {
			Object details = authentication.getDetails();
			if (details instanceof MyUserDetails) {
				MyUserDetails userDetails = (MyUserDetails) details;
				user = userDetails.getUser();
			}
		}
		return user;
	}

	public Long getCurrentUserId() throws ServiceException {
		User u = getLoggedInUser();
		if (u == null) {
			throw new ServiceException("you are not not logged in");
		}
		if (u.getId() == null) {
			throw new IllegalArgumentException("there is no userId assigned.");
		}
		return u.getId();
	}

	public QuerySpec getQuerySpec(Class<? extends BaseEntity> clazz)
			throws ServiceException {
		QuerySpec querySpec = new QuerySpec(clazz.getName());
		Long userId = getCurrentUserId();
		querySpec.addPropertySpec(new PropertySpec(Constants.USER_ID, userId));
		return querySpec;
	}
}
