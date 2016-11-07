package com.tonair.misc;

import java.util.HashMap;

public class VarBase {

	private static HashMap<String, String> hm = new HashMap<String, String>();
	
	public static void setVar(String s, String v){
		hm.put(s, v);
	}
	
	public static String getVar(String s){
		return hm.get(s);
	}
	
}