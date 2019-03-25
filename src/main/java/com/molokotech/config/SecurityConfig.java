package com.molokotech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.molokotech.service.CustomUserDetailsService;

//@ComponentScan(basePackages = { "com.molokotech.config" })
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * Axel Berlot 2018 Saltea archivos varios de recursos
	 */
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
		web.ignoring().antMatchers("/css/**");
		web.ignoring().antMatchers("/img/**");
		web.ignoring().antMatchers("/js/**");
	}

	@Bean
	public UserDetailsService mongoUserDetails() {
		return new CustomUserDetailsService();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/* Filtra quien ingresa y dónde, falta aplicar roles */
	    http
        .csrf().disable()      
        .httpBasic()
        .and()
    .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/index").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/sign-up").permitAll()
//        .antMatchers("/user").hasRole("USER")
//        .antMatchers("/dashboard").hasRole("ADMIN").anyRequest().authenticated()
        .antMatchers("/dashboard","/create-qr", "/assign-qr", "/edit-contact", "/all-user-qr", "/id").authenticated()
        .and()
    .formLogin().loginPage("/login")
        .and()
    .exceptionHandling().accessDeniedPage("/error");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
