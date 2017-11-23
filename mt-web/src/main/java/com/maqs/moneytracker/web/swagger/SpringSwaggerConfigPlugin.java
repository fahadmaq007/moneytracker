package com.maqs.moneytracker.web.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

@Configuration
@EnableSwagger
@ComponentScan(basePackages = {"com.maqs.moneytracker.web.controller"})
public class SpringSwaggerConfigPlugin {

	  private SpringSwaggerConfig springSwaggerConfig;

	  /**
	   * Required to autowire SpringSwaggerConfig
	   */
	  @Autowired
	  public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
	    this.springSwaggerConfig = springSwaggerConfig;
	  }

	  /**
	   * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc framework - allowing for multiple swagger groups
	   * i.e. same code base multiple swagger resource listings. /projects.*?
	   */
	  @Bean
	  public SwaggerSpringMvcPlugin groupOnePlugin() {
	    return new SwaggerSpringMvcPlugin(springSwaggerConfig).includePatterns("/*.*?")
	        .apiInfo(apiInfo()).swaggerGroup("mt-docs");
	  }

	  /**
	   * API Info as it appears on the swagger-ui page
	   */
	  private ApiInfo apiInfo() {
	    ApiInfo apiInfo =
	        new ApiInfo("Money Tracker", "Money Tracker APIs", "http://www.moneytracker.com/",
	            "maqbool.ahmed@moneytracker.com", "", "");
	    return apiInfo;
	  }
}
