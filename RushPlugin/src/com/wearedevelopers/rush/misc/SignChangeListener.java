package com.wearedevelopers.rush.misc;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.wearedevelopers.rush.party.PartyType;

public class SignChangeListener implements Listener{
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		if(event.getLine(0).equals("<[rush]>")&&event.getPlayer().isOp()){
			if(event.getLine(1)!=null&&event.getLine(2)!=null&&event.getLine(3)!=null){
				int i = Integer.parseInt(event.getLine(2));
				PartyType pt = PartyType.values()[i];
				FileConfiguration c = YamlConfiguration.loadConfiguration(new File(Main.getSwFileLoc()));
				List<String> worlds = (c.get("worlds")!=null)?(List<String>) c.getList("worlds"):new LinkedList<String>();
				Player p = event.getPlayer();
				Location l = p.getLocation();
				worlds.add(event.getLine(1));
				c.set("signs."+event.getLine(1)+".X", event.getBlock().getLocation().getX());
				c.set("signs."+event.getLine(1)+".Y", event.getBlock().getLocation().getY());
				c.set("signs."+event.getLine(1)+".Z", event.getBlock().getLocation().getZ());
				c.set("signs."+event.getLine(1)+".world", event.getBlock().getLocation().getWorld().getName());
				c.set("worlds", worlds);
				c.set("signs."+event.getLine(1)+".partyType",pt.toString());
				c.set("signs."+event.getLine(1)+".to", event.getLine(3));
				c.set("signs."+event.getLine(1)+".lobbyspawn", event.getLine(1)+":"+l.getX()+":"+l.getY()+":"+l.getZ());
				event.setLine(0, ChatColor.AQUA+"Rush "+pt.toString());
				event.setLine(1, ChatColor.GREEN+"[REJOINDRE]");
				event.setLine(2, ChatColor.DARK_BLUE+"En attente");
				event.setLine(3, ChatColor.WHITE+"0/"+(pt.getTeams()*pt.getTeamSize()));
				try {
					c.save(new File(Main.getSwFileLoc()));
				} catch(NumberFormatException e){
					event.getPlayer().sendMessage(ChatColor.RED+"Error on sign : line 3");
				} catch (IOException e) {
					e.printStackTrace();
				}
				Main.initHm();
			}
		}
	}

}
