package com.tonair.misc;

public enum VarType {
	
	STRING("STRING",'$'),
	NUMBER("NUMBER",':');
	
	private String text;
	private char symbol;
	
	VarType(String s, char c){
		this.text=s;
		this.symbol=c;
	}
	
	public String toString(){
		return symbol+text;
	}
	
	public static VarType fromString(String s){
		for(VarType it : VarType.values()){
			if(s.equals(it.toString())){
				return it;
			}
		}
		return null;
	}
	
	public static VarType fromSymbol(char c){
		for(VarType vt : VarType.values()){
			if(c==vt.symbol){
				return vt;
			}
		}
		return null;
	}

	public char getSymbol() {
		return symbol;
	}
	
}
