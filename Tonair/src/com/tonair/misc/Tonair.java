package com.tonair.misc;

import com.tonair.reader.Reader;

public class Tonair {
	
	public static String execute(String filename,String[] args){
		Reader r = new Reader(filename);
		return r.readAndExec(args);
	}
	
}
