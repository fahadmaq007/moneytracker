package com.maqs.moneytracker.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;

import com.maqs.moneytracker.common.Constants;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.util.StringUtil;
import com.maqs.moneytracker.model.User;
import com.maqs.moneytracker.types.Role;

public class TokenManagerImpl implements TokenManager {

	private Logger logger = Logger.getLogger(getClass());

	private static final String SALT = UUID.randomUUID().toString();

	private static final String COMMA = ", ";

	private static final String EQ = "=";

	private final CipherGenerator cipherGenerator;

	private static final String INVALID_TOKEN = "Invalid token";

	private static final String USERNAME = "username";

	private static final String ROLES = "roles";

	private static final String SEPARATOR = ":";

	public TokenManagerImpl() {
		cipherGenerator = new CipherGenerator();
		cipherGenerator.setSalt(SALT);
	}

	@Override
	public String newToken(UserDetails userDetails) throws ServiceException {
		MyUserDetails myUserDetails = (MyUserDetails) userDetails;
		String content = getContent(myUserDetails);
		logger.debug("newToken content: " + content);
		byte[] byteCipherText;
		try {
			byteCipherText = cipherGenerator.encrypt(content);
			String token = Base64.encodeBase64String(byteCipherText);
			return token;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	private String getContent(MyUserDetails myUserDetails)
			throws ServiceException {
		StringBuilder content = new StringBuilder();
		User u = myUserDetails.getUser();
		String username = u.getUsername();
		content.append(USERNAME).append(EQ).append(username);
		content.append(SEPARATOR);
		content.append(Constants.USER_ID).append(EQ).append(u.getId());
		
		Collection<Role> authorities = (Collection<Role>) myUserDetails
				.getAuthorities();
		if (CollectionsUtil.isNonEmpty(authorities)) {
			content.append(SEPARATOR);
			content.append(ROLES).append(EQ);
			int i = 0;
			int size = authorities.size();
			for (Role role : authorities) {
				content.append(role.getName());
				if (i < (size - 1)) {
					content.append(COMMA);
				}
				i++;
			}
		}

		return content.toString();
	}

	private UserDetails getUserDetails(String decryptedContent)
			throws ServiceException {
		Map<String, String> map = getPropertiesMap(decryptedContent);
		logger.debug("dec map: " + map);
		String username = map.get(USERNAME);
		String userId = map.get(Constants.USER_ID);
		User u = new User();
		u.setUsername(username);
		Long id = Long.valueOf(userId);
		u.setId(id);

		String rolesText = map.get(ROLES);
		List<Role> authorities = new ArrayList<Role>();
		if (!StringUtil.nullOrEmpty(rolesText)) {
			if (rolesText.contains(COMMA)) {
				String[] array = rolesText.split(COMMA);
				for (String roleName : array) {
					Role role = Role.valueByName(roleName);
					authorities.add(role);
				}
			} else {
				Role role = Role.valueByName(rolesText);
				authorities.add(role);
			}
		}
		UserDetails userDetails = new MyUserDetails(u, authorities);

		return userDetails;
	}

	@Override
	public boolean valid(String token) {
		return true;
	}

	@Override
	public UserDetails getUserFromToken(String token) throws ServiceException {
		byte[] bytesDecoded = Base64.decodeBase64(token);
		try {
			String decryptedContent = cipherGenerator.decrypt(bytesDecoded);
			logger.debug("decrypted text: " + decryptedContent);
			return getUserDetails(decryptedContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, String> getPropertiesMap(String text)
			throws ServiceException {
		String[] array = text.split(SEPARATOR);
		if (array.length == 0) {
			throw new ServiceException(INVALID_TOKEN);
		}
		Map<String, String> map = new HashMap<String, String>();
		for (String s : array) {
			String[] keyValues = s.split(EQ);
			if (keyValues.length == 0) {
				throw new ServiceException(INVALID_TOKEN);
			}
			String key = keyValues[0];
			String value = keyValues[1];
			if (StringUtil.nullOrEmpty(key) || StringUtil.nullOrEmpty(value)) {
				logger.warn("key: " + key + ", value: " + value);
				throw new ServiceException(INVALID_TOKEN);
			}
			value = value.trim();
			map.put(key, value);
		}
		return map;
	}
}
