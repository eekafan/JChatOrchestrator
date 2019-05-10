package com.rombachuk.jchatorchestrator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import com.google.gson.JsonArray;


/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */

public class JcoContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public JcoContextListener() {
        // TODO Auto-generated constructor stub
    }
    
	private class FileAlterationListenerJco implements FileAlterationListener {
		
		  private ServletContext context;

		public FileAlterationListenerJco(ServletContext context) {
		        // TODO Auto-generated constructor stub
			  this.context = context;
		    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onStart(final FileAlterationObserver observer) {
	       // System.out.println("The Jco FileListener has started on " + observer.getDirectory().getAbsolutePath());
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onDirectoryCreate(final File directory) {
	       // System.out.println(directory.getAbsolutePath() + " was created.");
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onDirectoryChange(final File directory) {
	        // System.out.println(directory.getAbsolutePath() + " was modified");
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onDirectoryDelete(final File directory) {
	        // System.out.println(directory.getAbsolutePath() + " was deleted.");
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onFileCreate(final File file) {
	        //
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onFileChange(final File file) {
	        if (file.getName().equals("jco_workspaces.props")) {
	        	InputStream workspacesfile = this.context.getResourceAsStream(
	             		 this.context.getInitParameter("jcoWorkspaces"));
	            JcoWorkspaces jcoworkspaces = new JcoWorkspaces(workspacesfile); 
	            this.context.setAttribute("jcoworkspaces", jcoworkspaces); 
	            try {
	            	workspacesfile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	        }
	            
		     if (file.getName().equals("jco.props")) {
		    	 InputStream propsfile = this.context.getResourceAsStream(
		        		 this.context.getInitParameter("jcoProperties"));
		        JcoProps jcoprops = new JcoProps(propsfile);   
	            this.context.setAttribute("jcoprops", jcoprops);
		        
		            try {
						propsfile.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	         }
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onFileDelete(final File file) {
	       // System.out.println(file.getAbsoluteFile() + " was deleted.");
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onStop(final FileAlterationObserver observer) {
	       // System.out.println("The FileListener has stopped on " + observer.getDirectory().getAbsolutePath());
	    }
	}

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	ServletContext ctx=sce.getServletContext(); 
    	final FileAlterationMonitor jcomonitor = (FileAlterationMonitor) ctx.getAttribute("jcocontextmonitor"); 
    	try {
			jcomonitor.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	try{  

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
	        
	        FileAlterationObserver fao = new FileAlterationObserver(ctx.getRealPath("/")+"/WEB-INF");
	        fao.addListener(new FileAlterationListenerJco(ctx));
	        final FileAlterationMonitor jcomonitor = new FileAlterationMonitor(30000);
	        jcomonitor.addObserver(fao);
	        jcomonitor.start();
	        ctx.setAttribute("jcocontextmonitor", jcomonitor);  
    		          
    		}catch(Exception e){
    			e.printStackTrace();
    		}  
     }  
 }
	
