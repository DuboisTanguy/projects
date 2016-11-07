package com.tonair.misc;

import java.util.LinkedList;
import java.util.List;

public class Instruction {
	
	private VarType type;
	private List<String> args = new LinkedList<String>();
	private String name;
	private String raw;
	private String resultAddress;
	private String leftHand;
	private boolean error = false;
	
	public Instruction(String s){
		raw = s;
		this.decode();
	}
	
	private void decode(){
		if(!raw.contains("=>")||raw.contains("%IF")||raw.contains("%DO")){
			resultAddress=null;
			leftHand=raw;
		}
		else{
			String[] split = raw.split("=>");
			if(split[1].replace(" ", "").startsWith("$")||split[1].replace(" ", "").startsWith(":")){
				resultAddress = split[1].replace(" ", "");
				this.type = VarType.fromSymbol(resultAddress.charAt(0));
				leftHand=split[0];
			}
			else{
				System.err.println("ERROR ! Var name format invalid ! : "+raw.split("=>")[1].replace(" ", ""));
				error=true;
			}
		}
	}

	public VarType getType() {
		return type;
	}

	public List<String> getArgs() {
		return args;
	}

	public String getRaw() {
		return raw;
	}

	public String getName() {
		return name;
	}

	public String getResultAddress() {
		return resultAddress;
	}

	public String getLeftHand() {
		return leftHand;
	}

	public boolean isError() {
		return error;
	}
	
}
