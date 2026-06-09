package com.agresta.CareerTrack.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				// 1. Permitting the login page and static assets is CRITICAL
				.requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login") // Points to your LoginController
						.loginProcessingUrl("/perform_login") // Must match the th:action in your HTML
						.defaultSuccessUrl("/", true) // Force redirect to index after login
						.permitAll() // Essential for the login process itself
				).logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

		manager.setUsersByUsernameQuery("SELECT username, password, enabled::int FROM users WHERE username = ?");
		manager.setAuthoritiesByUsernameQuery("SELECT u.username as username, a.authority as authority "
				+ "FROM users as u " + "INNER JOIN authorities as a on u.uid=a.uid " + "WHERE u.username = ?");

		return manager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
