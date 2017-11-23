package com.maqs.moneytracker.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.UserDto;
import com.maqs.moneytracker.model.User;
import com.maqs.moneytracker.services.UserService;
import com.wordnik.swagger.annotations.Api;

/**
 * The REST webservices are exposed to the external world.
 * 
 * @author maqbool.ahmed
 */
@Controller
@RequestMapping("/user")
@Api(value = "user", description = "User API")
public class UserController {

	@Autowired
	private UserService userService;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public @ResponseBody UserDto getToken(@RequestBody User user) throws ServiceException {
		logger.debug("login request by: " + user.getUsername());
		UserDto dto = userService.login(user);
		return dto;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody boolean logout(@RequestBody UserDto dto) throws ServiceException {
		logger.debug("logout request by: " + dto.getName());
		return true;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody boolean register(@RequestBody User user) throws ServiceException {
		logger.debug("register request by: " + user.getUsername());
		return userService.register(user);
	}
}
