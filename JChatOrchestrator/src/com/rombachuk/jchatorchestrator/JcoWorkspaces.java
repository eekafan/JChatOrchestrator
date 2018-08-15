package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class JcoWorkspaces {
	
	private List<Workspace> list = new ArrayList<Workspace>();
	
    public String findId(String name) {
    	int index = 0;
    	String id = null;
    	Boolean found = false;
    	while (!found && index < this.list.size()) {
    		if (this.list.get(index).getName().equals(name)) {
    			found = true;
    			id = this.list.get(index).getId();
    		}
    		else
    		{
    			index=index+1;
    		}
    	}
    	return id;
    }
	
	public JcoWorkspaces(InputStream input) {

		try {
		   	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    	String line;

			while((line = reader.readLine()) != null) {
			    String[] nextLine = line.split("\\s+");
		    if (nextLine.length == 2) {
			      Workspace thisworkspace = new Workspace(nextLine[0],nextLine[1]);
                  this.list.add(thisworkspace);
			    }
			}
			input.close();
	     }
	     catch (IOException e) {
	    	 System.out.println("File  not found");
	     }
	}
	
	public List<Workspace> getList() {
		return list;
	}

	public void setList(List<Workspace> list) {
		this.list = list;
	}
}
