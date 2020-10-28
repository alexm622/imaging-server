package com.alexcomeau.utilities;

import java.util.ArrayList;
import java.util.Collections;

public class VersionTools {
	public static String greaterVersion(String versionOneString, String versionTwoString) {
		
		//if version one equals version two return the version
		if(versionOneString == versionTwoString) {
			return versionOneString;
		}
		
		//split up version one string adn version two string
		String[] versionOneArray = versionOneString.split("."), versionTwoArray = versionTwoString.split(".");
		
		//version one arraylist creation
		ArrayList<String> versionOneArrayList = new ArrayList<String>();
		Collections.addAll(versionOneArrayList, versionOneArray);
		
		//version two arraylist creation
		ArrayList<String> versionTwoArrayList = new ArrayList<String>();
		Collections.addAll(versionTwoArrayList, versionTwoArray);
		
		//find which one is longer, if true then it is one, false it is no
		boolean longer = (versionOneArrayList.size() > versionTwoArrayList.size());
		
		//compare the longer arraylist to the shorter arraylist
		ArrayList<String> main = (longer) ? versionOneArrayList : versionTwoArrayList;
		ArrayList<String> second = (!longer) ? versionOneArrayList : versionTwoArrayList;
		
		//this variable will store the status of which version is longer
		//if higher is true then return the longer arraylist's string
		//if false return the shorter arraylist's string
		boolean higher = false;
		
		for(int i = 0; i < main.size(); i++) {
			try {
				//get the integer value of the small piece of the version
				int i1 = Integer.parseInt(main.get(i));
				int i2 = Integer.parseInt(second.get(i));
				
				if(i1 == i2) { //if the same continue
					continue;
				}else { //if not determine where the difference is
					//if the main string is longer then go with longer
					//if not go with shorter
					higher = (i1 > i2) ? true : false;
					break;
				}
			}catch(IndexOutOfBoundsException e) {
				try {
					//we hit the max size of second arraylist
					//try to get the value of the longer one
					main.get(i);
				}catch(IndexOutOfBoundsException e1) {
					//something went wrong, we're going with the shorter one
					Handler.debug("something went wrong in the version comparison!");
					Handler.debug("comparing \"" + versionOneString + "\" and \"" + versionTwoString + "\"");
					higher = false;
					break;
				}
				//turns out that we hit the max of the shorter version, we're gonna pick the longer one now
				higher = true;
				break;
			}

		}
		//if higher == true that means we pick the longer string, if not we pick the shorter string
		if(higher) {
			if(longer) { //if longer == true then the longer version is version one
				return versionOneString;
			}else {
				return versionTwoString;
			}
		}else {
			if(longer) {//if longer == true then the longer version is version one
				return versionTwoString;
			}else {
				return versionOneString;
			}
		}
	}
}

