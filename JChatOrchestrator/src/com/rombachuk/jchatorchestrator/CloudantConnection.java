package com.rombachuk.jchatorchestrator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;


public class CloudantConnection {
	CloudantClient client;
	
public CloudantConnection (JcoProps props)  throws MalformedURLException,IOException {
		   	   
	 
	    this.client =  ClientBuilder.url(new URL(props.getCloudanturl()))
                                            .username(props.getCloudantapikey())
                                            .password(props.getCloudantapipassword())
                                            .build();
	}

public CloudantClient getClient() {
	return client;
}

public void setClient(CloudantClient client) {
	this.client = client;
}
}
