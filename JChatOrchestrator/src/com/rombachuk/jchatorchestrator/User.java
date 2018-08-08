package com.rombachuk.jchatorchestrator;

import java.util.List;

import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;

public class User {
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public List<String> getRoles() {
		return Roles;
	}

	public void setRoles(List<String> roles) {
		Roles = roles;
	}

	String name;
	String dn;
	List<String> Roles;	
	
	public User(JcoProps ldapconfig, String username) throws LDAPException, LDAPSearchException {
		this.name = username;
		this.dn = "notfound";
	    
		LDAPConnection ldapconnection = new LDAPConnection(ldapconfig.getHostname(),
                ldapconfig.getHostport(),ldapconfig.getBinddn(),ldapconfig.getBindpassword());
	   	if (ldapconnection.isConnected()) {
     	 String userFilter = "(&"+ldapconfig.getUserclassfilter()+
     			            "("+ldapconfig.getUseruniqueattribute()+"="+username+"))";
     	 SearchResult searchResult = ldapconnection.search(ldapconfig.getUserbasedn(), 
     			                    SearchScope.SUB, userFilter);
     	 List<SearchResultEntry> results = searchResult.getSearchEntries();
       	 if (!results.isEmpty()) {
    		this.dn = results.get(0).getDN();
    	  } 
       	 ldapconnection.close();
	   	}
	 }
	   	
	 public static Boolean authenticate(JcoProps ldapconfig, String userdn, String credentials) throws LDAPException {
		 
		    Boolean authenticated = false;
			LDAPConnection ldapconnection = new LDAPConnection(ldapconfig.getHostname(),
                          ldapconfig.getHostport(),ldapconfig.getBinddn(),ldapconfig.getBindpassword());
		 	if (ldapconnection.isConnected()) {
				BindRequest bindRequest = new SimpleBindRequest(userdn,credentials);
		    	BindResult bindResult = ldapconnection.bind(bindRequest);
		    	if(bindResult.getResultCode().equals(ResultCode.SUCCESS)) {
		    		authenticated = true;
		    	}
		        ldapconnection.close();
		 	}
			return authenticated;			
	}
	


}
