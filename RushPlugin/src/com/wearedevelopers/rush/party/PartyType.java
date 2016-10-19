package com.wearedevelopers.rush.party;

public enum PartyType {

	TYPE4X1("4x1",4,1),
	TYPE4X2("4x2",4,2),
	TYPE4X4("4x4",4,4),
	TYPE2X1("2x1",2,1),
	TYPE2X2("2x2",2,2),
	TYPE2X4("2x4",2,4),
	TYPE2X8("2x8",2,8),
	TYPETEST("TEST",1,1);
	
	private String str;
	private int teams;
	private int teamSize;
	
	PartyType(String s,int t, int p){
		this.str = s;
		this.teams=t;
		this.teamSize=p;
	}
	
	public String toString(){
		return this.str;
	}
	
	public int getTeams(){
		return this.teams;
	}
	
	public int getTeamSize(){
		return this.teamSize;
	}
	
	public static PartyType fromString(String str){
		for(PartyType pt:PartyType.values()){
			if(str.equals(pt.toString())){
				return pt;
			}
		}
		return null;
	}

}
