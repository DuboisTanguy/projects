package com.tonair.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FileReader {

	@SuppressWarnings("unused")
	public static List<String> getInstructions(File file, String[] args){
		try {
			List<String> result = new LinkedList<String>();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[8];
			int n = 0;
			String content = "";
			while((n=bis.read(buf))>=0){
				for(byte bit : buf){
					if(bit!=0)
						content+=((char)bit);
				}
				buf = new byte[8];
			}
			if(args!=null){
				int i = 1;
				for(String s : args){
					if(content.contains("%JAVA"+i))content = content.replace("%JAVA"+i, s);
					i++;
				}
			}
			result = splitInstructions(content);
			bis.close();
			return result;
		} catch (FileNotFoundException e) {
			System.out.println("File can't be found !");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		return null;
	}
	
	public static List<String> splitInstructions(String str){
		String temp = str.replace(System.getProperty("line.separator"), "");
		List<String> result = new LinkedList<String>();
		int end = 0;
		String toAdd = "";
		do{
			if(temp.startsWith("%IF")){
				int count = countString(temp,"%IF");
				for(int i = 0 ; i<(count-1) ; i++){
					
				}
			}
		}while(temp.length()>=1);
		return Arrays.asList(temp.split(";"));
	}
	
	public static int countString(String src, String regex){
		String temp = src;
		int i = 0;
		while(temp.contains(regex)){
			i++;
			temp = temp.replaceFirst(regex, "");
		}
		return i;
	}
	
}
