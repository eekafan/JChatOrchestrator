package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Application Lifecycle Listener implementation class EventsContextListener
 *
 */
@WebListener
public class EventbotContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public EventbotContextListener() {
        // TODO Auto-generated constructor stub
    }


    private  String convertObjectServerDataType(String input) {
    	switch (input)
    	{
       	case "0" :
    		return "integer";
       	case "1" :
    		return "epochdatetime";
       	case "2" :
    		return "varchar";
       	case "5" :
    		return "incr";
      	case "10" :
    		return "char";
    	default	 :
    		return "unknown";
    	}
    	
    }
    
	private  JsonArray EventFieldDefs(InputStream input, String sourceType) {
		
		JsonArray field_defs = new JsonArray();

		try {
		   	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    	String line;

			while((line = reader.readLine()) != null) {
			    String[] nextLine = line.split("\\s+");
		    if (nextLine.length == 4) {
			      JsonObject field_def = new JsonObject();
			      field_def.add("id", new JsonPrimitive(nextLine[1]));
			      field_def.add("name", new JsonPrimitive(nextLine[1]));
			      if (sourceType.equals("ObjectServer")) {
			       field_def.add("type", new JsonPrimitive(convertObjectServerDataType(nextLine[2])));
			      }
			      field_def.add("length", new JsonPrimitive(nextLine[3]));
                  field_defs.add(field_def);
			    }
			}
			input.close();
			return field_defs;
	     }
	     catch (IOException e) {
	    	 System.out.println("File  not found");
	 		 JsonArray error_field_defs = new JsonArray();
	 		 return error_field_defs;
	     }
	     catch (Exception e) {
	    	 System.out.println("Exception processing file");
	 		 JsonArray error_field_defs = new JsonArray();
	 		 return error_field_defs;
	     }
	}
	
	private class FileAlterationListenerEB implements FileAlterationListener {
		
		  private ServletContext context;

		public FileAlterationListenerEB(ServletContext context) {
		        // TODO Auto-generated constructor stub
			  this.context = context;
		    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onStart(final FileAlterationObserver observer) {
	        // System.out.println("The EB FileListener has started on " + observer.getDirectory().getAbsolutePath());
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
	        if (file.getName().equals("eventbot_objectserver_fields.def")) {
	        	InputStream events_objectserver_fields_file = this.context.getResourceAsStream(
	            		 this.context.getInitParameter("eventbotObjectserverFields"));
	         	JsonArray objectserver_fields = EventFieldDefs(events_objectserver_fields_file,"ObjectServer");
	            this.context.setAttribute("eventbotobjectserverfields", objectserver_fields); 
	        }
            if (file.getName().equals("eventbot.props")) {
            	try {
        	     InputStream propsfile = this.context.getResourceAsStream(this.context.getInitParameter("eventbotProperties"));
                 EventbotProps eventbotprops = new EventbotProps(propsfile);   
                 propsfile.close();
                 this.context.setAttribute("eventbotprops", eventbotprops);
 
                 HistoricEventsConnection historyconn = (HistoricEventsConnection) this.context.getAttribute("eventbothistoryconnection");
                 if ((historyconn.status) && (historyconn.isValidSupported) && (historyconn.connection.isValid(1000))) {
                   historyconn.connection.close();
                 }
                 historyconn = new HistoricEventsConnection (eventbotprops);
                 this.context.setAttribute("eventbothistoryconnection", historyconn);  
 
                 ImpactConnection impactconn = (ImpactConnection) this.context.getAttribute("eventbotimpactconnection");
                 if ((impactconn.status) && (impactconn.connection.isValid(1000))) {
                     impactconn.connection.close();
                   }
                 impactconn = new ImpactConnection (eventbotprops);
                 this.context.setAttribute("eventbotimpactconnection",impactconn);
     
 
                 }
          	     catch (Exception e) {
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
	    	final FileAlterationMonitor ebmonitor = (FileAlterationMonitor) ctx.getAttribute("eventbotcontextmonitor"); 
	    	try {
				ebmonitor.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
     try {
  		ServletContext ctx=sce.getServletContext(); 
     	InputStream events_objectserver_fields_file = ctx.getResourceAsStream(
         		 ctx.getInitParameter("eventbotObjectserverFields"));
      	JsonArray objectserver_fields = EventFieldDefs(events_objectserver_fields_file,"ObjectServer");
        ctx.setAttribute("eventbotobjectserverfields", objectserver_fields); 
        
	    InputStream propsfile = ctx.getResourceAsStream(ctx.getInitParameter("eventbotProperties"));
        EventbotProps eventbotprops = new EventbotProps(propsfile);   
        ctx.setAttribute("eventbotprops", eventbotprops);
        propsfile.close();

        HistoricEventsConnection historyconn = new HistoricEventsConnection (eventbotprops);
        ctx.setAttribute("eventbothistoryconnection",historyconn);
        
        ImpactConnection impactconn = new ImpactConnection (eventbotprops);
        ctx.setAttribute("eventbotimpactconnection",impactconn);
        
        FileAlterationObserver fao = new FileAlterationObserver(ctx.getRealPath("/")+"/WEB-INF");
        fao.addListener(new FileAlterationListenerEB(ctx));
        final FileAlterationMonitor ebmonitor = new FileAlterationMonitor(30000);
        ebmonitor.addObserver(fao);
        ebmonitor.start();
        ctx.setAttribute("eventbotcontextmonitor", ebmonitor);  
        
 
    }	     
     catch (Exception e) {
   	    System.out.println("Exception processing file");	
    	System.out.println(e.getStackTrace());
    }	
   }
    
}
