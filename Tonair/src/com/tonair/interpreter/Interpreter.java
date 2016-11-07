package com.tonair.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.tonair.misc.Functions;
import com.tonair.misc.Instruction;
import com.tonair.misc.VarBase;

public class Interpreter {
	
	private String valueSent;

	public void exec(String s){
		Instruction i = new Instruction(s);
		if(!i.isError()){
			String result=calc(i.getLeftHand());
			if(i.getResultAddress()!=null){
				VarBase.setVar(i.getResultAddress(), result);
			}
		}
	}
	
	public String calc(String s){
		String temp = s;
		if(temp.contains("%IF")){
			processIf(temp);
			return null;
		}
		if(temp.contains("%DO")){
			processDo(temp);
			return null;
		}
		if(temp.contains("$")){
			temp = replaceVar(temp);
		}
		if(temp.contains("#")){
			temp = execFunctions(temp);
		}
		return temp;
	}
	
	public static boolean isOperation(String s){
		return s.contains("+")||s.contains("-")||s.contains("/")||s.contains("*");
	}
	
	public static String getVarName(int startIndex, String s){
		String temp =s.substring(startIndex);
		String result = new String();
		for(int i = 0 ; i<temp.length() ; i++){
			if(!isSpecialChar(temp.charAt(i)))
				result=result+temp.charAt(i);
			else
				break;
		}
		return result;
	}
	
	public static boolean isSpecialChar(char c){
		return c==' '||c=='?'||c=='!'||c=='_'||c=='+'||c=='-'||c=='/'||c=='*'||c=='('||c==')'||c==','||c=='.'||c=='='||c=='<'||c=='>'||c=='|';
	}
	
	public static String replaceVar(String s){
		String temp = s;
		do{
			String varName = getVarName(temp.indexOf("$"),temp);
			temp=temp.replace(varName, (VarBase.getVar(varName)==null)?"NULL":VarBase.getVar(varName));
		}while(temp.contains("$"));
		return temp;
	}
	
	public String execFunctions(String s){
		String temp = s;
		try {
			do{
				String name = getFunctionName(temp.indexOf("#"),temp);
				String args = getFunctionArgs(temp.indexOf("#"),temp);
				if(name.equals("sendToJava")){
					this.valueSent = args;
					temp = temp.replace("#"+name+"("+args+")", "");
				}
				else{
					Method m = Functions.class.getDeclaredMethod(name, String.class);
					temp = temp.replace("#"+name+"("+args+")", (String) m.invoke(null, args));
				}
			}while(temp.contains("#"));
		} catch (NoSuchMethodException e) {
			System.err.println("NO SUCH METHOD !");
		} catch (SecurityException e) {
			System.err.println("SECURITY EXCPETION !");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return  temp;
	}
	
	public static String getFunctionName(int index,String s){
		String temp = s;
		String result = new String();
		for(int i = index ; i<temp.length() ; i++){
			if(!isSpecialChar(temp.charAt(i)))
				result=result+temp.charAt(i);
			else
				break;
		}
		return result.replace("#", "");
	}
	
	public static String getFunctionArgs(int index,String s){
		String temp = s;
		index  = temp.indexOf("(")+1;
		int nb = 1;
		int lastIndex = 0;
		temp = temp.substring(index);
		for(int i = 0 ; i<temp.length() ; i++){
			if(temp.charAt(i)=='(')nb++;
			if(temp.charAt(i)==')')nb--;
			if(nb==0){
				lastIndex = i;
				break;
			}
		}
		return temp.substring(0, lastIndex);
	}
	
	public String getReturnedValue(){
		return this.valueSent;
	}
	
	public static String getCmpSign(String s){
		return (
				s.contains("==")?"==":
					s.contains("!=")?"!=":
						s.contains(">=")?">=":
							s.contains("<=")?"<=":
								s.contains("<")?"<":
									s.contains(">")?">":null
				);
	}
	
	public static String getIfArgs(String s){
		String temp = s;
		int index = temp.indexOf("(")+1;
		temp = temp.substring(index);
		int nb = 1;
		int lastIndex = 0;
		for(int i = 0 ; i<temp.length() ; i++){
			if(temp.charAt(i)=='(')nb++;
			if(temp.charAt(i)==')')nb--;
			if(nb==0){
				lastIndex = i;
				break;
			}
		}
		temp = temp.substring(0, lastIndex);
		return temp;
	}
	
	public static String getDoArgs(String s){
		String temp = s.split("%WHILE")[1];
		int index = temp.indexOf("(")+1;
		temp = temp.substring(index);
		int nb = 1;
		int lastIndex = 0;
		for(int i = 0 ; i<temp.length() ; i++){
			if(temp.charAt(i)=='(')nb++;
			if(temp.charAt(i)==')')nb--;
			if(nb==0){
				lastIndex = i;
				break;
			}
		}
		temp = temp.substring(0, lastIndex);
		return temp;
	}
	
	public void execIf(String s){
		String temp = s;
		temp = temp.replace("%IF("+getIfArgs(temp)+")", "");
		String[] instr = splitIf(temp);
		for(String str:instr){
			this.exec(str);
		}
	}
	
	public static boolean isTrue(String s){
		String args = s;
		if(args.contains("$"))
			args = replaceVar(args);
		String[] split = args.split(getCmpSign(args));
		boolean b = false;
		switch(getCmpSign(args)){
		case "==":
			if(split[0].equals(split[1]))b = true;
			break;
		case "!=":
			if(!split[0].equals(split[1]))b = true;
			break;
		case "<=":
			if(getDoubleFor(split[0])<=getDoubleFor(split[1]))b = true;
			break;
		case ">=":
			if(getDoubleFor(split[0])>=getDoubleFor(split[1]))b = true;
			break;
		case "<":
			if(getDoubleFor(split[0])<getDoubleFor(split[1]))b = true;
			break;
		case ">":
			if(getDoubleFor(split[0])>getDoubleFor(split[1]))b = true;
			break;
		}
		return b;
	}
	
	public void processIf(String temp){
		String args = getIfArgs(temp);
		boolean b = isTrue(args);
		if(b){
			if(temp.contains("%ELSE")){
				temp = temp.substring(0, temp.indexOf("%ELSE"));
			}
			execIf(temp);
		}
		else{
			if(temp.contains("%ELSE")){
				temp = temp.substring(temp.indexOf("%ELSE")).replace("%ELSE", "");
				String[] instr = splitIf(temp);
				for(String str:instr){
					this.exec(str);
				}
			}
		}
	}
	
	public String[] splitIf(String s){
		String temp = s;
		if(!temp.contains("%IF")){
			return temp.split("-");
		}
		else{
			List<String> ls = new LinkedList<String>();
			String[] str = temp.substring(1, temp.indexOf("%IF")).split("-");
			if(str.length>=1){
				for(String s2 : str){
					temp = temp.replace(s2, "");
					ls.add(s2);
				}
			}
			String ifInstr = temp.substring(temp.indexOf("%IF"),temp.indexOf(";"));
			ls.add(ifInstr);
			temp = temp.replace(ifInstr, "");
			String[] str2 = temp.split("-");
			if(str2.length>=1){
				for(String s2 : str){
					temp = temp.replace(s2, "");
					ls.add(s2);
				}
			}
		}
		return null;
	}
	
	public static double getDoubleFor(String s){
		double i = 0.0;
		try{
			i = Double.parseDouble(s);
		} catch(NumberFormatException e){
			try{
				i = (double)Integer.parseInt(s);
			} catch(NumberFormatException e2){
				return 0.0;
			}
		}
		return i;
	}
	
	public void processDo(String s){
		String temp = s;
		String rawArgs = getDoArgs(s);
		temp = temp.replace("%DO", "").replace("%WHILE("+rawArgs+")", "");
		String[] instr = temp.split("-");
		do{
			for(String str : instr){
				this.exec(str);
			}
		}while(isTrue(rawArgs));
	}
	
}
