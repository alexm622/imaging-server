package com.alexcomeau;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
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
				ArrayList<String> list = cleanupHTML(url, os.getIsIsoDir());
				String download;
				if(!os.getIsIsoDir()) {
					for(Iterator<String> it = list.iterator(); it.hasNext();) {
				        	//get index of string	
				        	String s = it.next();
				        	
				        	int i = list.indexOf(s);
				        	
				        	//only get the versions that have digits
				        	if(s.matches(".*\\d.*")) {
				        		list.set(i, s);
				        	}else {
				        		it.remove();
				        	}
				        	
					 }
					 //versions as integers
					 ArrayList<Integer> versions = new ArrayList<Integer>();
					 
					 for (String s : list) {
						 //remove all "."'s
						 s = s.replace(".", "");
						 //pad with zeroes
						 s = StringUtils.rightPad(s, 8, "0");
						 //add to the arraylist of versions as integers
						 versions.add(Integer.parseInt(s));
						 
						 Handler.debug("string in list to pad: " + s);
					 }
					 //sort into versions
					 Collections.sort(versions);
					 
					 //get the last version
					 String version = os.getLastVersion();
					 
					 //get the last version as an integer, and then set the variable for the next one to download
					 int lastV = Integer.parseInt(StringUtils.rightPad(version.replace(".", ""), 8, "0"));
					 int newV = lastV;
					 
					 //find the highest version
					 for(int i : versions) {
						 if(i > newV) {
							 newV = i;
						 }
					 }
					 
					 
					 
					 //convert integer version to string
					 download = insertEveryNCharacters(((Integer) newV).toString(), ".", 2);
					 
					 //remove padded zeroes
					 download = download.replaceAll(".00", "");
					 
					 if(os.getLastVersion() == download) {
						 download = "none";
						 continue;
					 }
					 
					 
					 
					 url = new URL(os.getUrl() + download + "/");
					 list = cleanupHTML(url, true);
					 
					 String file = os.getFile();
					 String split[] = file.split("#");
					 for(Iterator<String> it = list.iterator(); it.hasNext();) {
						 //get index of string	
						 String s = it.next();
				        	
						 int i = list.indexOf(s);
				        	
						 //only get the versions that have digits
						 if(s.contains(split[0]) && s.contains(split[1])) {
			        		list.set(i, s);
			        		Handler.debug("first string is " + split[0]);
			        		Handler.debug("second string is " + split[1]);
			        		Handler.debug("this matches " + s);
		        		}else {
		        			it.remove();
			        	}
				        				        	
					}
				}else {
										
					String file = os.getFile();
					String split[] = file.split("#");
					for(Iterator<String> it = list.iterator(); it.hasNext();) {
			        	//get index of string	
			        	String s = it.next();
			        	
			        	int i = list.indexOf(s);
			        	
			        	//only get the versions that have digits
			        	if(s.contains(split[0]) && s.contains(split[1])) {
			        		list.set(i, s);
			        		/*Handler.debug("first string is " + split[0]);
			        		Handler.debug("second string is " + split[1]);
			        		Handler.debug("this matches " + s);*/
			        	}else {
			        		it.remove();
			        	}
			        				        	
					}
					
					
					int version = 0;
					int newVersion = Integer.parseInt(os.getLastVersion().replace(".", ""));
					
					download = "none";
					
					for(String s : list) {
						String tmp;
						tmp = s.replace(split[0], "");
						tmp = tmp.replace(split[1], "");
						tmp = tmp.replace(".", "");
						//Handler.debug("the lastversion is " + newVersion);
						//Handler.debug("stripped string is " + tmp);
						version = Integer.parseInt(tmp);
						
						if(version > newVersion) {
							newVersion = version;
							download = s;
						}
					
						
						
					}
					
					if(Integer.parseInt(os.getLastVersion().replace(".", "")) == newVersion) {
						Handler.debug("no update");
						
					}else {
						Handler.debug("download this: " + download);
					}
				}
				 
				 //debug print out the version to download
				 
				 if(download == os.getLastVersion()) {
					 Handler.debug("we don't need to update " + os.getOsName());
				 }else {
					 Handler.debug("we need to get version: " + download + " of " + os.getOsName()); 
				 }
				 
				//TODO detect version being outdated
				//TODO also skip version stuff
			}
		}catch(Exception e) {
			Handler.debug(e, true);
			e.printStackTrace();
		}
		
	}

	
	private static ArrayList<String> cleanupHTML(URL url, boolean isIsoDir) throws IOException{
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


        return list;
        
        
	}
	private static String insertEveryNCharacters(String originalText, String textToInsert, int breakInterval) {
	    String out = "";
	    int textLength = originalText.length(); //initialize this here or in the start of the for in order to evaluate this once, not every loop
	    for (int i = breakInterval , current = 0; i <= textLength || current < textLength; current = i, i += breakInterval ) {
	        if(current != 0) {  //do not insert the text on the first loop
	            out += textToInsert;
	        }
	        if(i <= textLength) { //double check that text is at least long enough to go to index i without out of bounds exception
	            out += originalText.substring(current, i);
	        } else { //text left is not longer than the break interval, so go simply from current to end.
	            out += originalText.substring(current); //current to end (if text is not perfectly divisible by interval, it will still get included)
	        }
	    }
	    return out;
	}
}
