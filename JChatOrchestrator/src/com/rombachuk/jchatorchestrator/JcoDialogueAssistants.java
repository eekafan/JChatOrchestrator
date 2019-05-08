package com.rombachuk.jchatorchestrator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class JcoDialogueAssistants {
	
	private List<String> list = new ArrayList<String>();
	
	public JcoDialogueAssistants(InputStream input) {

		try {
		   	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    	String line;

			while((line = reader.readLine()) != null) {
			    String[] nextLine = line.split("\\s+");
		    if (nextLine.length == 1) {
			      String thisString = new String(nextLine[0]);
                  this.list.add(thisString);
			    }
			}
			input.close();
	     }
	     catch (IOException e) {
	    	 System.out.println("File  not found");
	     }
	}
	
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
}
