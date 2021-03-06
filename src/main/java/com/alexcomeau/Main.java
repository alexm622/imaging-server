package com.alexcomeau;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.alexcomeau.utilities.Downloader;
import com.alexcomeau.utilities.Handler;
import com.alexcomeau.utilities.HashChecking;
import com.alexcomeau.utilities.HtmlCleaner;
import com.alexcomeau.utilities.VersionTools;
import com.alexcomeau.utilities.json.OperatingSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Main {
	public static void main(String[] args) {
		try {
			//test print
			System.out.print("test");
			
			//create objectmapper
			ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
			
			
			
			//read os's from file
			OperatingSystem[] oses = om.readValue(new File("json/operating-systems.json"), OperatingSystem[].class);
			
			//output the number of os's
			Handler.debug(oses.length);
			
			//loop through os's
			for(OperatingSystem os : oses) {
				//filename
				String filename = "none";
				
				
				//if inactive os skip
				if(!os.getActive()) {
					continue;
				}
				
				//get url of mirror
				URL url = new URL(os.getUrl());
				
				//cleanup the html into a more usable format
				
				String download, downloadVer = os.getLastVersion();
				if(!os.getIsIsoDir()) {
					ArrayList<String> verList = HtmlCleaner.cleanupHTML(url, os.getIsIsoDir());
					//TODO this needs comments
					for(Iterator<String> it = verList.iterator(); it.hasNext();) {
				        	//get index of string	
				        	String s = it.next();
				        	
				        	int i = verList.indexOf(s);
				        	
				        	//only get the versions that have digits
				        	if(s.matches(".*\\d.*")) {
				        		verList.set(i, s);
				        	}else {
				        		it.remove();
				        	}
				        	
					 }
										
					String lastVer = os.getLastVersion();
					String newVer = lastVer;
					
					for(String s : verList) {
						String greaterVer = VersionTools.greaterVersion(s, lastVer);
						newVer = (greaterVer == s) ? s : newVer;
					}
					
					download = newVer;
					 
					 
					 //get the new url
					 url = new URL(os.getUrl() + download + "/");
					 ArrayList<String> list = HtmlCleaner.cleanupHTML(url, true);
					 
					 //get the file name
					 String file = os.getFile();
					 
					 //this was some old debug stuff
					 //Handler.debug("ubuntu download: " + file);
					 
					 
					 //split the string at the version number placeholder
					 String split[] = file.split("#");
					 
					 //initialize this first
					 boolean isBeta = false;
					 
					 for(Iterator<String> it = list.iterator(); it.hasNext();) {
						 //get index of string	
						 String s = it.next();
						 //test to make sure it isn't a beta branch
						 if(s.contains("beta")) {
							 isBeta = true;
							 break;
						 }
				        	
						 int i = list.indexOf(s);
				        	
						 //only get the versions that have digits
						 if(s.contains(split[0]) && s.contains(split[1])) {
			        		list.set(i, s);
			        		/*
			        		 * just some debug stuff
			        		Handler.debug("first string is " + split[0]);
			        		Handler.debug("second string is " + split[1]);
			        		*/
			        		//get the version we wanna download
			        		String tmp = s.replace(split[0], "");
			        		downloadVer = tmp.replace(split[1], "");
			        		
			        		//Handler.debug("this matches " + s);
			        		download = url.toString() + s;
			        		//Handler.debug("download is " + download);
			        		filename = s;
		        		}else {
		        			it.remove();
			        	}
					}
					 //oops we picked the beta branch, retry with the branch below it
					 if(isBeta) {
						 
						 verList.remove(verList.indexOf(newVer));
						 
						 newVer = lastVer;
						 
							
						 for(String s : verList) {
							 String greaterVer = VersionTools.greaterVersion(s, lastVer);
							 newVer = (greaterVer == s) ? s : newVer;
						 }
						 
						 download = newVer;
							 
						 
						 url = new URL(os.getUrl() + download + "/");
						 list = HtmlCleaner.cleanupHTML(url, true);
						 
						 for(Iterator<String> it = list.iterator(); it.hasNext();) {
							 //get index of string	
							 String s = it.next();
							 //Handler.debug("String is: " + s);
							 
					        	
							 int i = list.indexOf(s);
					        	
							 //only get the versions that have digits
							 if(s.contains(split[0]) && s.contains(split[1])) {
				        		list.set(i, s);
				        		//Handler.debug("first string is " + split[0]);
				        		//Handler.debug("second string is " + split[1]);
				        		String tmp = s.replace(split[0], "");
				        		downloadVer = tmp.replace(split[1], "");
				        		//Handler.debug("this matches " + s);
				        		filename = s;
				        		
				        		//url = new URL(url);
				        		download = url.toString() + s;
				        		Handler.debug("download is " + download);
			        		}else {
			        			it.remove();
				        	}
						}
						 
					 }
				}else {
					ArrayList<String> list = HtmlCleaner.cleanupHTML(url, os.getIsIsoDir());
					//get the file name and then split it up					
					String file = os.getFile();
					String split[] = file.split("#");
					for(Iterator<String> it = list.iterator(); it.hasNext();) {
			        	//get index of string	
			        	String s = it.next();
			        	
			        	int i = list.indexOf(s);
			        	
			        	//only get the files that match the file format we want
			        	//split the string
			        	if(s.contains(split[0]) && s.contains(split[1])) {
			        		list.set(i, s);
			        		//some debug stuff
			        		/*Handler.debug("first string is " + split[0]);
			        		Handler.debug("second string is " + split[1]);*/
			        		
			        		Handler.debug("the download is " + s);
			        		filename = s;
			        	}else {
			        		it.remove();
			        	}
			        				        	
					}
					
					//initialize variables
					int version = 0;
					int newVersion = Integer.parseInt(os.getLastVersion().replace(".", ""));
					
					//if this stays equal to none in the end there's an issue
					download = "none";
					
					//get last os version
					downloadVer = os.getLastVersion();
					
					for(String s : list) {
						String tmp;
						tmp = s.replace(split[0], "");
						tmp = tmp.replace(split[1], "");
						//the version we wanna download
						downloadVer = tmp;
						
						tmp = tmp.replace(".", "");
						//some debug stuff
						//Handler.debug("the lastversion is " + newVersion);
						//Handler.debug("stripped string is " + tmp);
						version = Integer.parseInt(tmp);
						
						//compare versions
						if(version > newVersion) {
							newVersion = version;
							download = s;
						}
					
						
						
					}
					//debug
					if(Integer.parseInt(os.getLastVersion().replace(".", "")) == newVersion) {
						Handler.debug("no update");
						
					}else {
						os.setLastVersion(downloadVer);
						Handler.debug("download this: " + download);
					}
				}
				 
				 //debug print out the version to download
				 
				 if(download == os.getLastVersion()) {
					 Handler.debug("we don't need to update " + os.getOsName());
				 }else {
					 download = url.toString() + filename;
					 //Handler.debug("we need to get version: " + download + " of " + os.getOsName()); 
				 }
				
				//set the url equal to the download url
				URL toDownload = new URL(download);
				
				
				//debug stuff
				Handler.debug("the url to download is: " + toDownload);
				
				
				
				
				//output file name
				File output = new File(os.getPath() + filename);
							
				
				//make path if not exist
				File path = new File(os.getPath());
				
				//if file path does not exist make it
				if(!path.exists()) {
					path.mkdirs();
				}
				
								
				if(filename == "none") {
					//no update
					continue;
				}
				boolean hashMatches = false;
				boolean downloaded = false;
				try {
					hashMatches = HashChecking.chechHash(output, HashChecking.getOnlineHash(url, filename, os), os.getHashType());
					//output results
					Handler.debug("does hash match? " + ((hashMatches) ? "yes" : "no"));
				}catch(Exception e) {
					
				}
				boolean processed = false;
				while(!hashMatches) {
					if((os.getDownloaded() && os.getLastVersion() == downloadVer )|| hashMatches) {
						break;
					}
					if(Downloader.getSize(toDownload) == output.length()) {
						processed = true;
					}
					
					if(processed) {
						//delete file
						output.delete();
					}
					
					//download file
					downloaded = Downloader.Download(toDownload, output, true);
					
					Handler.debug("this url is" + url.toString());
					
					//check hashes
					hashMatches = HashChecking.chechHash(output, HashChecking.getOnlineHash(url, filename, os), os.getHashType());
					//output results
					Handler.debug("does hash match? " + ((hashMatches) ? "yes" : "no"));
				}
				
				//check hashes again
				
				if(downloaded) {
					//if successfully downloaded then set to true
					os.setDownloaded(true);					
				}else {
					//if not downloaded and it threw an error then set to false
					os.setDownloaded(false);
				}
				
			}
			
			//flush to file
			om.writeValue(new File("json/operating-systems.json"), oses);
			
		}catch(Exception e) {
			Handler.debug(e, true);
			e.printStackTrace();
		}
		
	}

	
	
}
