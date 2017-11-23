package com.maqs.moneytracker.services;

import java.util.List;

import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.UserDto;
import com.maqs.moneytracker.model.User;
import com.maqs.moneytracker.types.Role;

public interface UserService {

	User getUserByUsername(String username) throws ServiceException;

	List<Role> getRoles(String username) throws ServiceException;

	UserDto login(User user) throws ServiceException;

	boolean register(User user) throws ServiceException;
    
	public User getSystemUser() throws ServiceException;
}
