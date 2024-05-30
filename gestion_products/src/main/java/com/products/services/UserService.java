package com.products.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.products.entities.UserEntity;
import com.products.repository.UserRespository;
import com.products.shared.Utils;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	UserRespository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	

	
	
	public void addUser(UserEntity user) {
		
		 user.setUserId(utils.generateStringId(32));
		 
		 user.setPassword(passwordEncoder.encode(user.getPassword()));
		
	     userRepository.save(user);
			
	}
	

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
