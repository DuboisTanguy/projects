package com.tonair.misc;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.tonair.interpreter.Interpreter;

public class Functions {
	
	public static String puts(String s){
		System.out.println(s);
		return "";
	}
	
	@SuppressWarnings("resource")
	public static String gets(String s){
		System.out.println(s);
		Scanner input = new Scanner(System.in);
		String result = input.nextLine();
		return result;
	}
	
	public static String toUpper(String s){
		return s.toUpperCase();
	}
	
	public static String toLower(String s){
		return s.toLowerCase();
	}
	
	public static String shuffle(String s){
		char[] o = s.toCharArray();
		char[] result = new char[o.length];
		List<Integer> l = new LinkedList<Integer>();
		for(int i = 0 ; i<o.length ; i++)l.add(i);
		int curr = 0;
		do{
			int nextIndex = (int) (Math.random()*((l.size())));
			result[curr] = o[l.get(nextIndex)];
			l.remove((int)nextIndex);
			curr++;
		}while(l.size()!=1);
		result[curr] = o[l.get(0)];
		return String.copyValueOf(result);
	}
	
	@SuppressWarnings("unused")
	public static String readFile(String s){
		String temp = new String();
		File file = new File(s);
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[8];
			int n = 0;
			while((n=bis.read(buf))>=0){
				for(byte bit : buf){
					if(bit!=0)
						temp+=((char)bit);
				}
				buf = new byte[8];
			}
			bis.close();
		} catch (FileNotFoundException e) {
			System.out.println("File can't be found !");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		return temp;
	}
	
	public static String writeFile(String s){
		String temp = s.substring(s.indexOf("|")+1);
		String fileName = s.substring(0, s.indexOf("|"));
		File file = new File(fileName);
		try {
			if(!file.exists()){
				file.createNewFile();
				System.out.println("File "+fileName+" created");
			}
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(temp);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("IOException");
		}
		return "Writed \""+temp+"\" to "+fileName;
	}
	
	public static String sum(String s){
		int index = s.indexOf("|");
		if(index==0){
			return null;
		}
		String[] nb = new String[]{s.substring(0,index),s.substring(index+1)};
		double i = Interpreter.getDoubleFor(nb[0]);
		double i2 = Interpreter.getDoubleFor(nb[1]);
		return (i+i2)+"";
	}
	
	public static String subtract(String s){
		int index = s.indexOf("|");
		if(index==0){
			return null;
		}
		String[] nb = new String[]{s.substring(0,index),s.substring(index+1)};
		double i = Interpreter.getDoubleFor(nb[0]);
		double i2 = Interpreter.getDoubleFor(nb[1]);
		return (i-i2)+"";
	}
	
	public static String mult(String s){
		int index = s.indexOf("|");
		if(index==0){
			return null;
		}
		String[] nb = new String[]{s.substring(0,index),s.substring(index+1)};
		double i = Interpreter.getDoubleFor(nb[0]);
		double i2 = Interpreter.getDoubleFor(nb[1]);
		return (i*i2)+"";
	}
	
	public static String divide(String s){
		int index = s.indexOf("|");
		if(index==0){
			return null;
		}
		String[] nb = new String[]{s.substring(0,index),s.substring(index+1)};
		double i = Interpreter.getDoubleFor(nb[0]);
		double i2 = Interpreter.getDoubleFor(nb[1]);
		return (i/i2)+"";
	}
	
	public static String random(String s){
		int index = s.indexOf("|");
		if(index==0){
			return null;
		}
		String[] nb = new String[]{s.substring(0,index),s.substring(index+1)};
		double i = Interpreter.getDoubleFor(nb[0]);
		double i2 = Interpreter.getDoubleFor(nb[1]);
		int res = (int)((Math.random() * ( i2 - i )));
		return res + "";
	}
	
	public static String exec(String s){
		String[] args = s.split("\\|");
		String[] toSend = new String[args.length-1];
		for(int i = 1 ; i<args.length ; i++){
			toSend[i-1] = args[i];
		}
		String result = Tonair.execute(args[0], toSend);
		return (result==null)?"NULL":result;
	}
	
}
