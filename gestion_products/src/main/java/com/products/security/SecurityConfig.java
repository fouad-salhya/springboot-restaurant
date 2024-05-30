package com.products.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.products.services.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	@Lazy
	UserService userService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
				.csrf(cf -> cf.disable())
			    .authorizeHttpRequests(req -> req.requestMatchers("/auth/**").permitAll())
			    .authorizeHttpRequests( res -> res.requestMatchers("/css/**", "/js/**", "/lib/**", "/img/**").permitAll())
			    .authorizeHttpRequests(req -> req.requestMatchers("/restoran/**").permitAll())
			    .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/restoran/home", true)
                        .permitAll()
                )
			    .logout(lg -> lg.logoutUrl("/auth/logout").logoutSuccessUrl("/auth/login").permitAll())
				.build();
				
	}
	

	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


	   

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
