package com.maqs.moneytracker.web.controller.test;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mt-servlet.xml")
public class AbstractContextControllerTest {    
  {
    MockitoAnnotations.initMocks(this); 
  }
  
  @Autowired
  protected WebApplicationContext applicationContext;


}
