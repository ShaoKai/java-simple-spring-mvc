package com.sky.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class BaseConfig extends WebSecurityConfigurerAdapter {
	private static final String ADMIN = "admin";

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(ADMIN).password(ADMIN).roles("USER");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		// web.debug(true);
		web.ignoring()//
				.antMatchers("/")
				.antMatchers("/resources/**") //
				.antMatchers(HttpMethod.GET, "/")//
				.antMatchers(HttpMethod.GET, "/get/**");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().headers().contentTypeOptions().xssProtection().cacheControl()
				.httpStrictTransportSecurity().and().exceptionHandling().and().authorizeRequests()
				.antMatchers("/home/**").authenticated().and().formLogin().defaultSuccessUrl("/home", false)
				.permitAll().and().logout().logoutUrl("/logout").deleteCookies("JSESSIONID").logoutSuccessUrl("/")
				.permitAll();

	}

}
