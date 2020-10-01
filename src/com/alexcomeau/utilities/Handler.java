package com.alexcomeau.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Handler {


	
	public static Thread t;
	public static Object o = new Object();


	public static void debug(Object msg, boolean err) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date date = new Date();
			PrintWriter out = new PrintWriter(new FileWriter(
				System.getenv("APPDATA") + "\\AlexRpg\\logs\\" + dateFormat.format(date) + ".txt", true));
			dateFormat = new SimpleDateFormat("[yyyy:MM:dd HH:mm:ss] -- ");
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			String temp = dateFormat.format(date);
			temp += className;
			temp += "." + methodName + "():";
			temp += Integer.toString(lineNumber) + ": ";
			temp += msg;
			temp += " \n";
			String sep = "----------------------------------";
			if(err){
				sep = "#############################";
				
			}
			System.out.println();
			
			System.out.println(temp);


			out.println();
			out.println();
			out.println(sep);

			out.println(temp);

			out.println(sep);
			out.close();
		} catch (IOException e) {
			File f = new File(System.getenv("APPDATA") + "\\AlexRpg\\logs\\");
			f.mkdirs();
			com.alexcomeau.utilities.Handler.debug("attempting to create logs folder to fix issue");
			
		}
		
		
	}

	public static void debug(Object msg){
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date date = new Date();
			PrintWriter out = new PrintWriter(new FileWriter(
				System.getenv("APPDATA") + "\\AlexRpg\\logs\\" + dateFormat.format(date) + ".txt", true));

			dateFormat = new SimpleDateFormat("[yyyy:MM:dd HH:mm:ss] -- ");
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			String temp = dateFormat.format(date);
			temp += className;
			temp += "." + methodName + "():";
			temp += Integer.toString(lineNumber) + ": ";
			temp += msg;
			temp += " \n";
			String sep = "----------------------------------";

			System.out.println();
			
			System.out.println(temp);

	
			out.println();
			out.println();
			out.println(sep);

			out.println(temp);

			out.println(sep);
			out.close();
		} catch (IOException e) {
			File f = new File(System.getenv("APPDATA") + "\\AlexRpg\\logs\\");
			f.mkdirs();
			com.alexcomeau.utilities.Handler.debug("attempting to create logs folder to fix issue");
		}
	}

	

}
