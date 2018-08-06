package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;


public class CloudantConnection {
	CloudantClient client;
	
public CloudantConnection (String url, String account, String password, String key, String iv)  throws MalformedURLException,IOException {
		   	        
	    this.client =  ClientBuilder.url(new URL(url))
                                            .username(account)
                                            .password(password)
                                            .build();
	}
}
