package com.alexcomeau;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;

import com.alexcomeau.utilities.Handler;
import com.alexcomeau.utilities.json.OperatingSystem;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	public static void main(String[] args) {
		try {
			//test print
			System.out.print("test");
			
			//create objectmapper
			ObjectMapper om = new ObjectMapper();
			
			//create arraylist of updateable os's
			ArrayList<OperatingSystem> updateable = new ArrayList<OperatingSystem>();
			
			//read os's from file
			OperatingSystem[] oses = om.readValue(new File("json/operating-systems.json"), OperatingSystem[].class);
			
			//output the number of os's
			Handler.debug(oses.length);
			
			//loop through os's
			for(OperatingSystem os : oses) {
				//if inactive os skip
				if(!os.getActive()) {
					continue;
				}
				//get url of mirror
				URL url = new URL(os.getUrl());
				
				//cleanup the html into a more usable format
				ArrayList<String> list = cleanupHTML(url);
				
				 for(Iterator<String> it = list.iterator(); it.hasNext();) {
			        	//get index of string	
			        	String s = it.next();
			        	
			        	
			        	
			        	int i = list.indexOf(s);
			        	
			        	if(s.matches(".*\\d.*")) {
			        		list.set(i, s);
			        	}else {
			        		it.remove();
			        	}
			        	
				 }
				 for (String s : list) {
					 Handler.debug(s);
				 }
				
				
				//TODO detect version being outdated
				//TODO also skip version stuff
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
	private static ArrayList<String> cleanupHTML(URL url) throws IOException{
		//read mirror data as html
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        
        //single line of html var
        String line;

        //stringbuilder to create final html file
        StringBuilder sb = new StringBuilder();
        
        //get all the lines from html and put into one string
        while ((line = br.readLine()) != null) {

            sb.append(line);
            sb.append(System.lineSeparator());
        }
        
        //convert from html to plaintext
        Document doc = Jsoup.parse(sb.toString());
        String text = doc.body().text();
        
        //split up at newlines
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(text.split("\n")));
        
        //remove the first one
        list.remove(0);
        
        //loop through list
        for(Iterator<String> it = list.iterator(); it.hasNext();) {
        	//get index of string
        	
        	String s = it.next();
        	
        	int i = list.indexOf(s);
        	
        	
        	
        	
        	if(!s.contains("/")) {
        		//if item is a file remove
        		it.remove();
        	}else {
        		
        		s = s.substring(0, s.indexOf("/"));
        		list.set(i, s);
        	}
        	
        	
        	
        }


        return list;
        
        
	}
}
