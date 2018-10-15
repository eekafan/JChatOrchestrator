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
                
                 Class.forName("com.ibm.db2.jcc.DB2Driver");
                 Connection historyconn = (Connection) this.context.getAttribute("eventbothistoryconn");
                 if (historyconn.isValid(1000)) {
                   historyconn.close();
                 }
                 historyconn = DriverManager.getConnection (eventbotprops.getHistoryurl(), 
                		eventbotprops.getHistoryuser(), eventbotprops.getHistorypassword());
                 this.context.setAttribute("eventbothistoryconn", historyconn);  
                 this.context.setAttribute("eventbotprops", eventbotprops);
 
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
        
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        Connection historyconn = DriverManager.getConnection (eventbotprops.getHistoryurl(), 
        		eventbotprops.getHistoryuser(), eventbotprops.getHistorypassword());
        Statement statement = historyconn.createStatement();
        JsonArray history_fields = new JsonArray();
        ResultSet rs = statement.executeQuery("select column_name,data_type,character_maximum_length from sysibm.columns where table_name='REPORTER_STATUS'");
        while(rs.next()){
        	JsonObject field_def = new JsonObject();
		      field_def.add("id", new JsonPrimitive(rs.getString("column_name")));
		      field_def.add("name", new JsonPrimitive(rs.getString("column_name")));
		      field_def.add("type", new JsonPrimitive(rs.getString("data_type")));
		      field_def.add("length", new JsonPrimitive(Integer.toString(rs.getInt("character_maximum_length"))));
		      history_fields.add(field_def);
         }
        historyconn.close();
        ctx.setAttribute("eventbothistoryfields",history_fields);
        
        
        FileAlterationObserver fao = new FileAlterationObserver(ctx.getRealPath("/")+"/WEB-INF");
        fao.addListener(new FileAlterationListenerEB(ctx));
        final FileAlterationMonitor ebmonitor = new FileAlterationMonitor(30000);
        ebmonitor.addObserver(fao);
        ebmonitor.start();
        ctx.setAttribute("eventbotcontextmonitor", ebmonitor);  
        
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        Connection impactconn = DriverManager.getConnection (eventbotprops.getImpactserverurl(), 
        		eventbotprops.getImpactserveruser(), eventbotprops.getImpactserverpassword());
        statement = impactconn.createStatement();
        JsonArray re_groups = new JsonArray();
        rs = statement.executeQuery("select groupname,instances,uniqueevents,totalevents,type from RELATEDEVENTS.RE_GROUPS");
        while(rs.next()){
        	JsonObject re_group = new JsonObject();
        	re_group.add("groupname", new JsonPrimitive(rs.getString("groupname")));
        	re_group.add("type", new JsonPrimitive(rs.getString("type")));
        	re_group.add("total_events", new JsonPrimitive(Integer.toString(rs.getInt("totalevents"))));
        	re_group.add("unique_events", new JsonPrimitive(Integer.toString(rs.getInt("uniqueevents"))));
        	re_group.add("instances", new JsonPrimitive(Integer.toString(rs.getInt("instances"))));
        	re_groups.add(re_group);
         }
        impactconn.close();
        ctx.setAttribute("eventbotregroups",re_groups);
    }	     
     catch (Exception e) {
   	    System.out.println("Exception processing file");	
    	e.printStackTrace();
    }	
   }
    
}
