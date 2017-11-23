package com.maqs.moneytracker.security;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.maqs.moneytracker.common.Constants;
import com.maqs.moneytracker.common.util.CollectionsUtil;

public class CustomAuthenticationToken extends AbstractAuthenticationToken implements Authentication {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8482729479409817604L;

	private final Logger logger = Logger.getLogger(getClass());
    
    public CustomAuthenticationToken(UserDetails userDetails) {
		super(userDetails.getAuthorities());
		setDetails(userDetails);
		logger.debug("the authentication is created for : " + userDetails);
		if (CollectionsUtil.isNonEmpty(userDetails.getAuthorities())) {
			super.setAuthenticated(true); // must use super, as we override
		}
	}
	
	@Override
    public Object getCredentials() {
        return Constants.EMPTY_STRING;
    }
 	
    @Override
    public Object getPrincipal() {
    	UserDetails userDetails = (UserDetails) getDetails();
		if (userDetails != null) {
			return userDetails.getUsername();
		}
        return userDetails;
    }
    
}
