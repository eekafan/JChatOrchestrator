package com.rombachuk.jchatorchestrator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

public class LDAPConfig {
	
	String binddn;
	String bindpassword;
	String hostname;
	int hostport;
	String userbasedn;
	String useruniqueattribute;
	String userclassfilter;
	
	public LDAPConfig (ServletContext ctx) {		
		this.binddn				=ctx.getInitParameter("LDAPbindDN");
		this.bindpassword 		=ctx.getInitParameter("LDAPbindPassword");
		this.hostname			=ctx.getInitParameter("LDAPhostname");
		this.hostport			=Integer.parseInt(ctx.getInitParameter("LDAPhostport"));
		this.userbasedn         =ctx.getInitParameter("LDAPuserBaseDN");
	    this.useruniqueattribute=ctx.getInitParameter("LDAPuserUniqueAttribute");
		this.userclassfilter   	=ctx.getInitParameter("LDAPuserClassFilter");	
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
