package com.alexcomeau.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {
	public static boolean Download(URL url, File outputFile) throws IOException {
		
		
		Handler.debug("the url is: " + url.toString());
		
		
		
		long removeFileSize = getSize(url);
		Handler.debug("existing size" + outputFile.length());
		Handler.debug("download size" + removeFileSize);
		if(outputFile.length() == removeFileSize) {
			return true;
		}
		
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		
		
		//Add this right after you initialize httpUrlConnection but before beginning download
		if(outputFile.exists()) {
		    httpConnection.setRequestProperty("Range", "bytes="+outputFile.length()+"-");
		}
		FileOutputStream fileOutputStream;
		//And then you'd initialize the file output stream like so:
		if(outputFile.exists()) {
		    fileOutputStream = new FileOutputStream(outputFile, true); //resume download, append to existing file
		}else {
		    fileOutputStream = new FileOutputStream(outputFile);
		}
		
		boolean out = true;
		long existingFileSize = outputFile.length();
		BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
		
		BufferedOutputStream bout = new BufferedOutputStream(fileOutputStream, 1024);
		try
		{
		    byte[] data = new byte[1024];
		    int x = 0;
		    long written = outputFile.length();
		    System.out.print("\n");
		    while ((x = in.read(data, 0, 1024)) >= 0) 
		    {
		    	written += 1024;
		    	System.out.print("\r" + written + "\\" + removeFileSize + "                ");
		        bout.write(data, 0, x);
		    }
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		    out = false;
		}
		finally
		{
		    if(bout!=null)
		    {
		        bout.flush();
		        bout.close();
		    }
		    if(fileOutputStream!=null)
		    {
		        fileOutputStream.flush();
		        fileOutputStream.close();
		    }
		    
		}
		
		return out;
	}
	
	private static long getSize(URL url) {
		
	    URLConnection conn = null;
	    try {
	        conn = url.openConnection();
	        if(conn instanceof HttpURLConnection) {
	            ((HttpURLConnection)conn).setRequestMethod("HEAD");
	        }
	        conn.getInputStream();
	        return conn.getContentLengthLong();
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    } finally {
	        if(conn instanceof HttpURLConnection) {
	            ((HttpURLConnection)conn).disconnect();
	        }
	    }
	}
}
