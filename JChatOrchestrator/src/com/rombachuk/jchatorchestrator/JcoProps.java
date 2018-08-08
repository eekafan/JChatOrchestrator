package com.rombachuk.jchatorchestrator;

import java.io.File;
import java.io.IOException;

import java.util.Scanner;

public class JcoProps {
	
	String binddn = null;
	String bindpassword = null;
	String hostname = null;
	int hostport = 389;
	String userbasedn = null;
	String useruniqueattribute = null;
	String userclassfilter = null;
	
	public JcoProps (String propsfile) {	
		
     try {
		File pfile = new File(propsfile);
		Scanner input = new Scanner(pfile);

		while(input.hasNext()) {
		    String[] nextLine = input.nextLine().split("\\s+");
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
		    
		}

		input.close();
     }
     catch (IOException e) {
    	 System.out.println("File "+propsfile+" not found");
     }

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
