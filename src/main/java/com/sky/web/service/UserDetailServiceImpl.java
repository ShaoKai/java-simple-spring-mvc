package com.sky.web.service;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {

	private static final String ADMIN = "admin";

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		logger.info("loadUserByUsername username=" + username);

		if (!username.equals(ADMIN)) {
			throw new UsernameNotFoundException(username + " not found");
		}

		return new UserDetails() {

			private static final long serialVersionUID = 2059202961588104658L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				return true;
			}

			@Override
			public boolean isAccountNonExpired() {
				return true;
			}

			@Override
			public String getUsername() {
				return username;
			}

			@Override
			public String getPassword() {
				return ADMIN;
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				List<SimpleGrantedAuthority> auths = new java.util.ArrayList<SimpleGrantedAuthority>();
				auths.add(new SimpleGrantedAuthority(ADMIN));
				return auths;
			}
		};
	}
}
