package com.maqs.moneytracker.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.dto.SetupActivityDto;
import com.maqs.moneytracker.model.Setting;
import com.maqs.moneytracker.services.ApplicationService;
import com.wordnik.swagger.annotations.Api;

/**
 * The REST webservices are exposed to the external world.
 * 
 * @author maqbool.ahmed
 */
@Controller
@RequestMapping("/api/application")
@Api(value = "application", description = "Application API")
@Secured("ROLE_USER")
public class ApplicationController {

	public static final Page DEFAULT_PAGE = new Page(1, 100);
	
	@Autowired
	private ApplicationService applicationService;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@RequestMapping(value = "/userprefs", method = RequestMethod.GET)
	public @ResponseBody List<Setting> listUserPreferences() throws ServiceException {
		logger.debug("listUserPreferences is calleed");
		List<Setting> settings = applicationService.listAll(DEFAULT_PAGE);
		return settings;
	}
	
	@RequestMapping(value = "/setupactivities", method = RequestMethod.GET)
	public @ResponseBody List<SetupActivityDto> listSetupActivity() throws ServiceException {
		logger.debug("listSetupActivity is calleed");
		List<SetupActivityDto> list = applicationService.listSetupActivity();
		return list;
	}
}
