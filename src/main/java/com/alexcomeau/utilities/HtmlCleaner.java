package com.alexcomeau.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlCleaner {
	public static ArrayList<String> cleanupHTML(URL url, boolean isIsoDir) throws IOException{
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
        	
        	
        	
        	
        	if((isIsoDir && s.contains("/")) || (!s.contains("/") && !isIsoDir)) {
        		//if item is a file remove
        		it.remove();
        	}else if(isIsoDir && !s.contains(".iso") || s.contains(".sig") || s.contains(".torrent")) {
        		it.remove();
        	}else {
        		if(isIsoDir) {
        			s= s.substring(0, s.indexOf(".iso")) + ".iso";
        			//Handler.debug("substring is " + s);
        			list.set(i, s);
        			continue;
        		}
        		s = s.substring(0, s.indexOf("/"));
        		list.set(i, s);
        	}
        	
        	
        	
        }

        //return the cleansed string
        return list;
        
        
	}
}
