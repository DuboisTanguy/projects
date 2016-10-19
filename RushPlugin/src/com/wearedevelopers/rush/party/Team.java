package com.wearedevelopers.rush.party;

import org.bukkit.ChatColor;

public enum Team {
	
	TYPEBLUE(ChatColor.BLUE+"[BLEU]"),
	TYPERED(ChatColor.RED+"[ROUGE]"),
	TYPEPURPLE(ChatColor.LIGHT_PURPLE+"[VIOLET]"),
	TYPEYELLOW(ChatColor.YELLOW+"[JAUNE]");
	
	private String name;
	
	Team(String s){
		this.name = s;
	}
	
	public String toString(){
		return this.name;
	}
	
	public static Team fromString(String str){
		for(Team t : Team.values()){
			if(t.toString().equals(str)) return t;
		}
		return null;
	}
	
}
