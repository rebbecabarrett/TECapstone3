package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;




public class TransferService {
	
	public String AUTH_TOKEN = "";
	private final String BASE_URL = "http://localhost:8080/api";
	private final RestTemplate restTemplate = new RestTemplate();

	public Transfer[] getListOfTransfers() {
		Transfer[] listOfTransfers = null;
		
		 HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setBearerAuth(AUTH_TOKEN);
		    
		    HttpEntity<Transfer[]> entity = new HttpEntity<>(headers);
		    
		    listOfTransfers = restTemplate.exchange(BASE_URL + "/transfers", HttpMethod.GET, entity, Transfer[].class).getBody();
		    
		return listOfTransfers;
	}
	
	public void setAUTH_TOKEN(String token) {
		AUTH_TOKEN = token;		
	}

	public Transfer getTransferDetails(int transferId) {
		Transfer transferDetails = null;
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(AUTH_TOKEN);
	    
	    HttpEntity<Transfer> entity = new HttpEntity<Transfer>(headers);
	    
	    transferDetails = restTemplate.exchange(BASE_URL + "/transfers/" + transferId, HttpMethod.GET, entity, Transfer.class).getBody();
		return transferDetails;
	}	
	
	
}
