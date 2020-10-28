package com.alexcomeau.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import com.alexcomeau.utilities.json.OperatingSystem;

public class HashChecking {
	private static String getFileChecksum(MessageDigest digest, File file) throws IOException
	{
	    //Get file input stream for reading the file content
	    FileInputStream fis = new FileInputStream(file);
	     
	    //Create byte array to read data in chunks
	    byte[] byteArray = new byte[1024];
	    int bytesCount = 0; 
	      
	    //Read file data and update in message digest
	    while ((bytesCount = fis.read(byteArray)) != -1) {
	        digest.update(byteArray, 0, bytesCount);
	    };
	     
	    //close the stream; We don't need it now.
	    fis.close();
	     
	    //Get the hash's bytes
	    byte[] bytes = digest.digest();
	     
	    //This bytes[] has bytes in decimal format;
	    //Convert it to hexadecimal format
	    StringBuilder sb = new StringBuilder();
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	     
	    //return complete hash
	   return sb.toString();
	}
	
	
	public static boolean chechHash(File file, String hash, String hashtype) throws IOException, NoSuchAlgorithmException{
		
		
		
		//Use SHA-1 algorithm
		MessageDigest shaDigest = MessageDigest.getInstance(hashtype);
		 
		//SHA-1 checksum 
		String shaChecksum = getFileChecksum(shaDigest, file);
		/*
		Handler.debug("online hash: " + hash);
		Handler.debug("current hash: " + shaChecksum);
		
		Handler.debug("does hash match?");
		*/
		if(hash.contains(shaChecksum)) {
			//Handler.debug("yes");
			return true;
		}
		//Handler.debug("no");
		return false;
	}
	
	public static String getOnlineHash(URL url, String filename, OperatingSystem os) throws IOException {
		URL downloadURL = new URL(url.toString() + os.getHashFile());
		
		File file = new File(os.getPath() + os.getHashFile());
		
		Downloader.Download(downloadURL, file);
		
		String line = "";
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			if(line.contains(filename)) {
				break;
			}
		}
		
		
		String hash = "";
		if(line == "") {
			hash = "none";
		}
		
		String[] strings = line.split(" ");
		hash = strings[0];
		
		return hash;
	}
}
