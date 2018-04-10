package com.CSP_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

	// private @Autowired AutowireCapableBeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// @Bean
	// public FilterRegistrationBean filterRegistrationBean() {
	//
	// FilterRegistrationBean bean = new FilterRegistrationBean();
	// Filter filter = new MyFilter();
	// beanFactory.autowireBean(filter);
	//
	// bean.setFilter(filter);
	// bean.addUrlPatterns("/admin/index.html");
	// bean.addUrlPatterns("/companies/index.html");
	// bean.addUrlPatterns("/customers/index.html");
	//
	// return bean;
	// }
}
