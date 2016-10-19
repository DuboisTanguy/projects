package com.wearedevelopers.rush.party;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.wearedevelopers.rush.misc.Main;

public class PartyRefresher {

	private Party party;
	private int task;

	public PartyRefresher(Party p) {
		this.party = p;
	}

	public void start() {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Rush"),
				new Runnable() {

					@Override
					public void run() {
						List<org.bukkit.scoreboard.Team> ts = new LinkedList<org.bukkit.scoreboard.Team>();
						if(Main.isRunning()){
							PartyType pt = party.getType();
							Scoreboard sc = party.getSc();
							int i = 0;
							for(org.bukkit.scoreboard.Team t : sc.getTeams()){
								int i2 = 0;
								for(String s : t.getEntries()){
									Player p = Bukkit.getPlayer(s);
									i2 += (p.isOnline()&&!p.hasPotionEffect(PotionEffectType.INVISIBILITY))? 1 : 0 ;
								}
								Objective o = sc.getObjective("Dead");
								o.getScore(t.getName()).setScore(i2);
								if(i2>0){
									i+=1;
									ts.add(t);
								}
							}
							if(i<=1){
								//Bukkit.getScheduler().cancelTask(task);
								//party.end(ts.get(0));
							}
						}
					}
					
				},0L,3L);
	}

}
