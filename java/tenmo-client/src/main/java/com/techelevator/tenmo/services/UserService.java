package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class UserService {
	
	public String AUTH_TOKEN = "";
	private final String BASE_URL = "http://localhost:8080/api";
	private final RestTemplate restTemplate = new RestTemplate();

	public User[] getListOfUsers() {
		User[] getListOfUsers = null;
		
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 headers.setBearerAuth(AUTH_TOKEN);
		    
		 HttpEntity<Transfer[]> entity = new HttpEntity<>(headers);
		 
		 getListOfUsers = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, entity, User[].class).getBody();
		
		return getListOfUsers;
	}

	public void setAUTH_TOKEN(String token) {
		AUTH_TOKEN = token;		
	}
}
