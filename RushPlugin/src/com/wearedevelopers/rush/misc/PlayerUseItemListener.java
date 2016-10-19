package com.wearedevelopers.rush.misc;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scoreboard.Scoreboard;

import com.wearedevelopers.rush.party.PartyType;

public class PlayerUseItemListener implements Listener{
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		if(e.getAction()==Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(e.getItem()!=null){
				if(e.getItem().getType()==Material.WOOL && Main.getSignForWorld(e.getPlayer().getWorld().getName())!=null){
					e.setCancelled(true);
					e.getPlayer().updateInventory();
					FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getSwFileLoc()));
					PartyType pt = PartyType.fromString(fc.getString("signs."+e.getPlayer().getWorld().getName()+".partyType"));
					String wn = e.getPlayer().getWorld().getName();
					Scoreboard sc = (Main.getScForWorld(wn)!=null)?Main.getScForWorld(wn):Main.addScoreboard(wn,pt);
					if(e.getItem().getItemMeta().getDisplayName()!=null){
						org.bukkit.scoreboard.Team t = sc.getTeam(e.getItem().getItemMeta().getDisplayName());
						if(!(t.getEntries().size()>=pt.getTeamSize())){
							t.addEntry(e.getPlayer().getName());
							e.getPlayer().sendMessage(ChatColor.GREEN+"Vous avez rejoint l'équipe "+t.getPrefix());
							e.getPlayer().setScoreboard(sc);
						}
						else{
							e.getPlayer().sendMessage(ChatColor.RED+"L'équipe est pleine !");
						}
					}
				}
			}
		}
	}

}
