package com.bluesky.plugin;

public enum PartyGameMode {
	
	TDM("Team Death Match");
	
	private String name = "";
	
	PartyGameMode(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public static PartyGameMode fromString(String str){
		if(str.equals("TDM")){
			return PartyGameMode.TDM;
		}
		return null;
	}
}
