package com.maqs.moneytracker.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.BusinessException;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.common.util.StringUtil;
import com.maqs.moneytracker.dto.UserDto;
import com.maqs.moneytracker.model.User;
import com.maqs.moneytracker.security.CipherGenerator;
import com.maqs.moneytracker.security.MyUserDetails;
import com.maqs.moneytracker.security.TokenManager;
import com.maqs.moneytracker.server.core.dao.IDao;
import com.maqs.moneytracker.types.Role;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class UserServiceImpl implements UserService {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDao dao;
	
	private final static String QUERY_GET_ROLES = "User.getRoles";
	
	private final static String QUERY_CREATE_ROLE = "User.createRole";
	
	@Autowired
	private TokenManager tokenManager;
	
	private final CipherGenerator cipherGenerator;
	
	private User systemUser;
	
	@Value("${system.user}")
	private String systemUserName;
	
	public UserServiceImpl() {
		cipherGenerator = new CipherGenerator();
	}

	@Override
	public User getUserByUsername(String username) throws ServiceException {
		logger.debug("getUserByUsername: " + username);
		QuerySpec querySpec = new QuerySpec(User.class.getName());
		querySpec.addPropertySpec(new PropertySpec(User.USERNAME, username));
		User u = null;
		try {
			u = (User) dao.getEntity(querySpec);
			if (u == null) {
				throw new UsernameNotFoundException(username);
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return u;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRoles(String username) throws ServiceException {
		logger.debug("getRoles: " + username);
		List<Role> roles = null;
		try {
			roles = (List<Role>) dao.executeNamedSQLQuery(QUERY_GET_ROLES, new Object[] { username } , Role.class);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		logger.debug(username + " permissions: " + roles);
		return roles;
	}

	@Override
	public UserDto login(User user) throws ServiceException {
		if (user == null) {
			throw new IllegalArgumentException("given user is null or undefined.");
		}
		String username = user.getUsername();
		if (StringUtil.nullOrEmpty(username)) {
			throw new ServiceException("Username is empty, it is a required field.");
		}
		QuerySpec querySpec = new QuerySpec(User.class.getName());
		querySpec.addPropertySpec(new PropertySpec(User.USERNAME, username));
		String password = user.getPassword();
		if (StringUtil.nullOrEmpty(password)) {
			throw new BusinessException("EMPTY", "Password is empty, it is a required field.");
		}		
		UserDto userDto = null;
		try {
			String encPassword = cipherGenerator.encryptToBase64(password);
			querySpec.addPropertySpec(new PropertySpec(User.PASSWORD, encPassword));
			
			User existingUser = (User) dao.getEntity(querySpec);
			if (existingUser != null) {
				List<Role> roles = getRoles(user.getUsername());
				UserDetails userDetails = new MyUserDetails(existingUser, roles);
				userDto = new UserDto();
				String name = existingUser.getName();
				userDto.setName(name);						
				String token = tokenManager.newToken(userDetails);
				userDto.setToken(token);
			} else {
				throw new BadCredentialsException("either username or password is incorrect, please try again");
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return userDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public boolean register(User user) throws ServiceException {
		if (user == null) {
			throw new IllegalArgumentException("given user is null or undefined.");
		}
		String username = user.getUsername();
		if (StringUtil.nullOrEmpty(username)) {
			throw new BusinessException("EMPTY", "Username is empty, it is a required field.");
		}
		QuerySpec querySpec = new QuerySpec(User.class.getName());
		querySpec.addPropertySpec(new PropertySpec(User.USERNAME, username));
		
		try {
			Long count = (Long) dao.count(querySpec);
			if (count > 0) {
				throw new BusinessException("DUPLICATE_USER", "Username is already in use");
			} else {
				// create new user
				String password = user.getPassword();
				if (StringUtil.nullOrEmpty(password)) {
					throw new BusinessException("EMPTY", "Password is empty, it is a required field.");
				}
				String encPassword = cipherGenerator.encryptToBase64(password);
				user.setPassword(encPassword);
				user = (User) dao.save(user);
				
				Long userId = user.getId();
				String roleId = Role.USER;
				dao.updateNamedSQLQuery(QUERY_CREATE_ROLE, new Object[] { userId, roleId } );
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		
		return false;
	}

	@Override
	public User getSystemUser() throws ServiceException {
		if (systemUser == null) {
			logger.debug("getting details of system user: " + systemUserName);
			systemUser = getUserByUsername(systemUserName);
		}
		return systemUser;
	}
}
