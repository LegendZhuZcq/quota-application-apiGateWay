package com.quoteservice.apigateway.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.quoteservice.apigateway.model.*;

@ImportResource("classpath:application-context.xml")

@RestController
public class apiGateController {
	
	//import external URL for Author and Quote APIs
	@Value("${service.quoterandom.url}") private String quoteRandomUrl;
	@Value("${service.getQuoteById.url}") private String getQuoteByIdUrl;
	@Value("${service.getQuoteByAuthorId.url}") private String getQuoteByAuthorIdUrl;
	@Value("${service.saveQuote.url}") private String saveQuoteUrl;
	@Value("${service.getAuthorById.url}") private String getAuthorByIdUrl;
	@Value("${service.getAuthorByName.url}") private String getAuthorByNameUrl;
	@Value("${service.saveAuthor.url}") private String saveAuthorUrl;
	
	
	@RequestMapping("/api/quote/random")
	public QuoteCombined random() {
		RestTemplate restTemplate = new RestTemplate();
		
		Quote quote = restTemplate.getForObject(quoteRandomUrl, Quote.class);
		
		UriComponentsBuilder getQuoteUrl = UriComponentsBuilder
				.fromUriString(getAuthorByIdUrl)
				.queryParam("id", quote.getAuthorId());		
		
		Author author = restTemplate.getForObject(getQuoteUrl.toUriString(),Author.class);
		
		QuoteCombined returnQuote =  new QuoteCombined(quote.getId(),quote.getText(),quote.getSource(),author.getName());
		return returnQuote;
	}
	
	@RequestMapping(value = "/api/quote", method = RequestMethod.POST)
    public void saveQuote(@RequestBody QuoteCombined quote) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Author> postAuthor = new HttpEntity<>(new Author(quote.getAuthor()));
		Long authorId = restTemplate.postForObject(saveAuthorUrl, postAuthor, Long.class);
		HttpEntity<Quote> postQuote = new HttpEntity<>(new Quote(quote.getText(),quote.getSource(),authorId));
		Quote quotePost = restTemplate.postForObject(saveQuoteUrl, postQuote, Quote.class);
	}
	

	@RequestMapping(value = "/api/quoteByAuthor", method = RequestMethod.GET)
	public List<QuoteCombined> getQuoteByAuthor(@RequestParam(value="author") String authorName){
    	RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder getAuthorUrl = UriComponentsBuilder
        	    .fromUriString(getAuthorByNameUrl)
        	    .queryParam("name", authorName);       	    
        System.out.println(getAuthorUrl.toUriString());
        
        Author authorObj = restTemplate.getForObject(getAuthorUrl.toUriString(), Author.class);

        UriComponentsBuilder getQuotesByAuthorId = UriComponentsBuilder
        	    .fromUriString(getQuoteByAuthorIdUrl)
        	    .queryParam("authorId", authorObj.getId());          
        
    	@SuppressWarnings("unchecked")
        List<Quote> quotesFromApi= restTemplate.getForObject(getQuotesByAuthorId.toUriString(), List.class);
        
        List<QuoteCombined> returnedQuotes = new ArrayList<QuoteCombined>();
        
        for(int i=0;i<quotesFromApi.size();i++) {
        	returnedQuotes.get(i).setAuthor(authorObj.getName());
        	returnedQuotes.get(i).setText(quotesFromApi.get(i).getText());
        	returnedQuotes.get(i).setSource(quotesFromApi.get(i).getSource());
        }
        		
		return returnedQuotes;		
	}
}
