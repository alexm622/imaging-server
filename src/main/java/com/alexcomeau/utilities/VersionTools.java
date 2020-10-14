package com.alexcomeau.utilities;

import java.util.ArrayList;
import java.util.Collections;

public class VersionTools {
	public static String greaterVersion(String s1, String s2) {
		if(s1 == s2) {
			return s1;
		}
		String[] s1a = s1.split("."), s2a = s2.split(".");
		
		ArrayList<String> s1al = new ArrayList<String>();
		Collections.addAll(s1al, s1a);
		
		ArrayList<String> s2al = new ArrayList<String>();
		Collections.addAll(s2al, s2a);
		
		boolean larger = (s1al.size() > s2al.size());
		
		ArrayList<String> main = (larger) ? s1al : s2al;
		ArrayList<String> second = (!larger) ? s1al : s2al;
		
		boolean higher = false;
		
		for(int i = 0; i < main.size(); i++) {
			try {
				int i1 = Integer.parseInt(main.get(i));
				int i2 = Integer.parseInt(second.get(i));
				if(i1 == i2) {
					continue;
				}else {
					higher = (i1 > i2) ? true : false;
					break;
				}
			}catch(IndexOutOfBoundsException e) {
				try {
					main.get(i);
				}catch(IndexOutOfBoundsException e1) {
					higher = false;
					break;
				}
				higher = true;
				break;
			}

		}
		if(higher) {
			if(larger) {
				return s1;
			}else {
				return s2;
			}
		}else {
			if(larger) {
				return s2;
			}else {
				return s1;
			}
		}
	}
}

