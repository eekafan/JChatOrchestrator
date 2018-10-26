package com.rombachuk.jchatorchestrator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ImpactConnection {
	Connection connection = null;
	String type = "derby";
	Boolean status = false;
	
	public ImpactConnection (EventbotProps eventbotprops)   {
		this.status = false;
		this.type = eventbotprops.getImpactservertype();
	   	   
		try {
        if (this.type.equals("derby")) {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
           }
           this.connection = DriverManager.getConnection (eventbotprops.getImpactserverurl(), 
           		eventbotprops.getImpactserveruser(), eventbotprops.getImpactserverpassword());
           if (this.connection.isValid(200)) {
        	   this.status = true;
           }
		}
		catch (SQLException e ) {
			System.out.println(e.toString());
		}
        catch (ClassNotFoundException e ) {
			
		}
	}

}
