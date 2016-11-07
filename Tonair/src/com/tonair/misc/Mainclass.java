package com.tonair.misc;

public class Mainclass {
	
	//@SuppressWarnings("unused")
	public static void main(String[] args){
		String[] toSend = new String[]{"E:/src.txt","E:/dest.txt"};
		System.out.println(Tonair.execute("E:/hellow.tnr",toSend));
		//System.out.println(Tonair.execute("E:/TNR/game.tnr",null));
	}
	
}
