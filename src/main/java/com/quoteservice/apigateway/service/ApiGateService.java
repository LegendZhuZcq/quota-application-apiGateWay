package com.quoteservice.apigateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import com.quoteservice.apigateway.model.Author;
import com.quoteservice.apigateway.model.Quote;

@Service
public class ApiGateService {
	@Value("${service.quoterandom.url}") private String quoteRandomUrl;
	@Value("${service.getAuthorById.url}") private String getAuthorByIdUrl;
	
	@HystrixCommand(fallbackMethod = "reliableQuote")
	public Quote randomQuote(){
		RestTemplate restTemplate = new RestTemplate();
		System.out.println(quoteRandomUrl);
		return restTemplate.getForObject(quoteRandomUrl, Quote.class);
	}
	
	@HystrixCommand(fallbackMethod = "reliableAuthor")
	public Author getAuthor(Long id) {
		RestTemplate restTemplate = new RestTemplate();
		
		UriComponentsBuilder getQuoteUrl = UriComponentsBuilder
				.fromUriString(getAuthorByIdUrl)
				.queryParam("id", id);	
		System.out.println(getAuthorByIdUrl);
		
		return restTemplate.getForObject(getQuoteUrl.toUriString(),Author.class);
	}
	
	public Quote reliableQuote() {
		Quote q1 = new Quote("The world is a thing of utter inordinate","wikiquote",1);
		return q1;
	}
	
	public Author reliableAuthor(Long id) {
		Author a1 = new Author("Douglas Adams");
		return a1;
	}
}
