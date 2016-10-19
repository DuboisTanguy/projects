package com.likorn.multiclip.misc;

import com.likorn.multiclip.gui.ClipPanel;
import com.likorn.multiclip.gui.Window;

public class Main {
	
	private static Window w = new Window();
	
	public static void main(String[] args){
	}
	
	public static void removePan(ClipPanel cp){
		w.removePan(cp);
	}
	
	public static void setOnTop(boolean b){
		w.setAlwaysOnTop(b);
	}
	
	public static int getMaxOf(String[] s){
		int index = 0;
		for(int i = 0 ; i<s.length ; i++)
			index = (s[i].length()>s[index].length())?i:index;
		return index;
	}
	
}
