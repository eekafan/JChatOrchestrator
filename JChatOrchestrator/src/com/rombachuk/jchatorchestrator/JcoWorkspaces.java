package com.rombachuk.jchatorchestrator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class JcoWorkspaces {
	
	private List<Workspace> list = new ArrayList<Workspace>();
	
	public JcoWorkspaces(String propsfile) {

		try {
			File pfile = new File(propsfile);
			Scanner input = new Scanner(pfile);

			while(input.hasNext()) {
			    String[] nextLine = input.nextLine().split("\\s+");
			    if (nextLine.length == 2) {
			      Workspace thisworkspace = new Workspace(nextLine[0],nextLine[1]);
                  this.list.add(thisworkspace);
			    }
			}
			input.close();
	     }
	     catch (IOException e) {
	    	 System.out.println("File "+propsfile+" not found");
	     }
	}
	
	public List<Workspace> getList() {
		return list;
	}

	public void setList(List<Workspace> list) {
		this.list = list;
	}
}
