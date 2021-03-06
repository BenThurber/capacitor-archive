package com.capacitorarchive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	// Fix the CORS errors
	@Bean
	public FilterRegistrationBean simpleCorsFilter() {
		List<String> exposedHeaders = Arrays.asList("Total-Rows", "Has-Next");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		// *** URL below needs to match the Angular client URL and port ***
		config.setAllowedOrigins(Arrays.asList(
				"http://localhost:35504",
				"http://localhost:35506",
				"http://www.capacitor-archive.com",
				"http://www.capacitor-archive.com:35506",
				"https://localhost:35504",
				"https://localhost:35506",
				"https://www.capacitor-archive.com",
				"https://www.capacitor-archive.com:35506"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		config.setExposedHeaders(exposedHeaders);
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}
