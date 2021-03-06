package com.example.demo.securityconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JwtRequestFilter;
import com.example.demo.service.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityCongiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailService userDetailService;
	
	@Autowired
	JwtRequestFilter requestFilter;
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService);
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("ADMIN","USER")
			.antMatchers("/authenticate","/register").permitAll().and()
			.formLogin()
			.and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		
		return NoOpPasswordEncoder.getInstance() ;
	}

}
