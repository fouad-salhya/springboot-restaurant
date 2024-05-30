package com.products.services;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
	
	 private static final String NODE_SERVICE_URL = "http://localhost:7000/api/mails/send";


	    public void sendEmail(String email) {
	    	
	        RestTemplate restTemplate = new RestTemplate();

	        Map<String, String> request = new HashMap<>();
	        request.put("email", email);
	        
	        
	        restTemplate.postForObject( NODE_SERVICE_URL, email, String.class);
	       
	    }
}
