package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class AccountService {

	public String AUTH_TOKEN = "";
	private final String BASE_URL = "http://localhost:8080/api";
	private final RestTemplate restTemplate = new RestTemplate();
	
	public BigDecimal getAccountBalance() {
		
		BigDecimal accountBalance;
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(AUTH_TOKEN);
		
	    HttpEntity<BigDecimal> entity = new HttpEntity<>(headers);
	    
	    accountBalance = restTemplate.exchange(BASE_URL + "/accounts", HttpMethod.GET, entity, BigDecimal.class).getBody();
	    
		return accountBalance;
	}
	
	public void setAUTH_TOKEN(String token) {
		AUTH_TOKEN = token;		
	}	

	

}
