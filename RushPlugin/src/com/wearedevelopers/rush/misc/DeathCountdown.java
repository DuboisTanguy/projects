package com.wearedevelopers.rush.misc;

import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.wearedevelopers.rush.party.Party;

public class DeathCountdown extends BukkitRunnable {
	
	private Player player;
	private int count = 10;
	private Color color;
	private Team team;
	
	public DeathCountdown(Player p , int i){
		this.player = p;
		this.count = i;
		this.color = ( (LeatherArmorMeta) p.getInventory().getArmorContents()[0].getItemMeta()).getColor();
		this.team = getTeamOf(player);
	}

	@Override
	public void run() {
		player.getInventory().setArmorContents(null);
		player.setAllowFlight(true);
		player.teleport(player.getLocation().clone().add(new Location(player.getWorld(),0,2,0)));
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,1));
		player.setFlying(true);
		for(int i = count ; i>=0 ; i--){
			if(!player.isOnline()) break;
			player.setLevel(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		player.teleport(player.getBedSpawnLocation().clone().add(0.0,1.0,0.0));
		player.setFlying(false);
		player.setAllowFlight(false);
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.setLevel(0);
		Party.giveArmor(player, color, team);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public static Team getTeamOf(Player p){
		if(p.getScoreboard()!=null){
			Scoreboard sc = p.getScoreboard();
			for(Team t : sc.getTeams()){
				for(String s : t.getEntries()){
					if(p.getName().equals(s)) return t;
				}
			}
		}
		return null;
	}
	
}
