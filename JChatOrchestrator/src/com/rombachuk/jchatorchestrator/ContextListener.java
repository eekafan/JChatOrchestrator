package com.rombachuk.jchatorchestrator;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */

public class ContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	try{  
    		//storing connection object as an attribute in ServletContext  
    		ServletContext ctx=sce.getServletContext();  
    		
          	InputStream workspacesfile = ctx.getResourceAsStream(
             		 ctx.getInitParameter("jcoWorkspaces"));
            JcoWorkspaces jcoworkspaces = new JcoWorkspaces(workspacesfile); 
            ctx.setAttribute("jcoworkspaces", jcoworkspaces); 
            workspacesfile.close(); 
            
    	    InputStream propsfile = ctx.getResourceAsStream(
	        		 ctx.getInitParameter("jcoProperties"));
	        JcoProps jcoprops = new JcoProps(propsfile);   
            ctx.setAttribute("jcoprops", jcoprops);
	        propsfile.close();
    		          
    		}catch(Exception e){
    			e.printStackTrace();
    		}  
     }  
 }
	
