package com.sky.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.sky.web.service.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class BaseConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		
		auth.authenticationProvider(authenticationProvider())
		    .authenticationProvider(rememberMeAuthenticationProvider());
		
		// @formatter:on
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// @formatter:off
		
		web.ignoring()//
	        .antMatchers("/")//
	        .antMatchers("/resources/**") //
	        .antMatchers(HttpMethod.GET, "/")//
	        .antMatchers(HttpMethod.GET, "/get/**");
		
		// @formatter:on
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		
		http.csrf().disable()
			.headers().contentTypeOptions().xssProtection().cacheControl().httpStrictTransportSecurity().and()
			.exceptionHandling().and()
			.authorizeRequests().antMatchers("/home/**").authenticated().and()
			.formLogin().defaultSuccessUrl("/home", false).permitAll().and()
			.logout().logoutUrl("/logout").deleteCookies("JSESSIONID").logoutSuccessUrl("/").permitAll().and()
			.rememberMe().rememberMeServices(rememberMeService());
		
		// @formatter:on
	}

	// ***************************************************
	// Bean
	// ***************************************************
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailServiceImpl();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new AuthenticationProvider() {

			private final static String PASSWORD_INCORRECT_EXCEPTION = "Password is incorrect.";

			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String account = null;
				String password = null;
				Collection<? extends GrantedAuthority> roles = null;

				UserDetails userDetails = null;
				UsernamePasswordAuthenticationToken token = null;

				account = authentication.getName();
				password = (String) authentication.getCredentials();

				// authorization
				userDetails = userDetailsService().loadUserByUsername(account);
				if (!userDetails.getPassword().equals(password)) { // FIXME
					throw new BadCredentialsException(PASSWORD_INCORRECT_EXCEPTION);
				}
				roles = userDetails.getAuthorities();

				// initialize token
				token = new UsernamePasswordAuthenticationToken(account, password, roles);
				token.setDetails(userDetails);

				return token;
			}

			public boolean supports(Class<?> arg0) {
				return true;
			}

		};
	}

	//
	// ***************************************************
	// Remember Me
	// ***************************************************

	@Bean
	public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
		return new RememberMeAuthenticationProvider("rememberMeCookie");
	}

	@Bean
	public RememberMeServices rememberMeService() {
		String key = "rememberMeCookie";
		TokenBasedRememberMeServices rememberMeService = null;
		rememberMeService = new TokenBasedRememberMeServices(key, userDetailsService());
		rememberMeService.setTokenValiditySeconds(60 * 60 * 24 * 14);
		rememberMeService.setAlwaysRemember(true);
		return rememberMeService;
	}

}
