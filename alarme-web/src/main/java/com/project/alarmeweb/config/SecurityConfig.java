package com.project.alarmeweb.config;

import com.project.alarmeweb.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private UserDetailServiceImpl userDetailServiceImpl;
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring()
		.antMatchers("/static/**");
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.antMatchers("/settings/**").hasRole("ADMIN")
			.antMatchers("/**").permitAll()
			.anyRequest().authenticated();
		
		http.csrf().disable();
		
		http.formLogin()
			.loginPage("/")
			.loginPage("/login")
			.loginProcessingUrl("/j_spring_security_check")
			.failureUrl("/login?error=notLogin")
			.defaultSuccessUrl("/mail/dashboard",true)
			.usernameParameter("username")
			.passwordParameter("password");
			
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/j_spring_security_logout"))
			.logoutSuccessUrl("/login")
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.deleteCookies("JSESSIONID");

		http.sessionManagement()
			.enableSessionUrlRewriting(true)
			.invalidSessionUrl("/login");
	}
	
	@Override 
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailServiceImpl)
			.passwordEncoder(passwordEncoder()); 
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public static void main(String[] args){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String a = "123456";
		//$2a$10$cFsZV/ftX3CywpLMAn6Fge6kZ4y8vrrfuT92EB428bBqPmepfsMTK
		System.out.println(encoder.encode(a));
	}
}
