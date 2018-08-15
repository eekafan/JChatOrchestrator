package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class JcoProps {
	
	private String binddn = null;
	private String bindpassword = null;
	private String hostname = null;
	private int hostport = 389;
	private String userbasedn = null;
	private String useruniqueattribute = null;
	private String userclassfilter = null;
	private String cloudanturl = null;
	private String cloudantapikey = null;
	private String cloudantapipassword = null;
	private String watsonassistantversion = null;
	private String watsonassistanturl = null;
	private String watsonassistantusername = null;
	private String watsonassistantpassword = null;
	
	public JcoProps (InputStream input) {	
		
     try {

    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    	String line;

		while((line = reader.readLine()) != null) {
		    String[] nextLine = line.split("\\s+");
		    if (nextLine[0].equalsIgnoreCase("LDAPbindDN")) {
		    	this.binddn = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("LDAPbindPassword")) {
		    	this.bindpassword = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("LDAPhostname")) {
		    	this.hostname = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("LDAPhostport")) {
		    	this.hostport = Integer.parseInt(nextLine[1]);
		    }
		    if (nextLine[0].equalsIgnoreCase("LDAPuserBaseDN")) {
		    	this.userbasedn = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("LDAPuserUniqueAttribute")) {
		    	this.useruniqueattribute = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("LDAPuserClassFilter")) {
		    	this.userclassfilter = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("CloudantURL")) {
		    	this.cloudanturl = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("CloudantAPIKey")) {
		    	this.cloudantapikey = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("CloudantAPIPassword")) {
		    	this.cloudantapipassword = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("WatsonAssistantVersion")) {
		    	this.watsonassistantversion = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("WatsonAssistantURL")) {
		    	this.watsonassistanturl = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("WatsonAssistantUsername")) {
		    	this.watsonassistantusername = nextLine[1];
		    }
		    if (nextLine[0].equalsIgnoreCase("WatsonAssistantPassword")) {
		    	this.watsonassistantpassword = nextLine[1];
		    }	    
		}
		reader.close();
     }
     catch (IOException e) {
    	 System.out.println("File not found");
     }

	}
	
	public String getCloudanturl() {
		return cloudanturl;
	}

	public void setCloudanturl(String cloudanturl) {
		this.cloudanturl = cloudanturl;
	}

	public String getCloudantapikey() {
		return cloudantapikey;
	}

	public void setCloudantapikey(String cloudantapikey) {
		this.cloudantapikey = cloudantapikey;
	}

	public String getCloudantapipassword() {
		return cloudantapipassword;
	}

	public void setCloudantapipassword(String cloudantapipassword) {
		this.cloudantapipassword = cloudantapipassword;
	}

	public String getWatsonassistanturl() {
		return watsonassistanturl;
	}

	public void setWatsonassistanturl(String watsonassistanturl) {
		this.watsonassistanturl = watsonassistanturl;
	}

	public String getWatsonassistantusername() {
		return watsonassistantusername;
	}

	public void setWatsonassistantusername(String watsonassistantusername) {
		this.watsonassistantusername = watsonassistantusername;
	}

	public String getWatsonassistantpassword() {
		return watsonassistantpassword;
	}

	public void setWatsonassistantpassword(String watsonassistantpassword) {
		this.watsonassistantpassword = watsonassistantpassword;
	}
	
	public String getWatsonassistantversion() {
		return watsonassistantversion;
	}

	public void setWatsonassistantversion(String watsonassistantversion) {
		this.watsonassistantversion = watsonassistantversion;
	}
	
	public String getBinddn() {
		return binddn;
	}

	public void setBinddn(String binddn) {
		this.binddn = binddn;
	}

	public String getBindpassword() {
		return bindpassword;
	}

	public void setBindpassword(String bindpassword) {
		this.bindpassword = bindpassword;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getHostport() {
		return hostport;
	}

	public void setHostport(int hostport) {
		this.hostport = hostport;
	}

	public String getUserbasedn() {
		return userbasedn;
	}

	public void setUserbasedn(String userbasedn) {
		this.userbasedn = userbasedn;
	}

	public String getUseruniqueattribute() {
		return useruniqueattribute;
	}

	public void setUseruniqueattribute(String useruniqueattribute) {
		this.useruniqueattribute = useruniqueattribute;
	}

	public String getUserclassfilter() {
		return userclassfilter;
	}

	public void setUserclassfilter(String userclassfilter) {
		this.userclassfilter = userclassfilter;
	}



}
