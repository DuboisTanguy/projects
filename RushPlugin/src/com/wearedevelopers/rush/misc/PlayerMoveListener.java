package com.wearedevelopers.rush.misc;

import java.io.File;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements Listener{
	
	@EventHandler
	public void PlayerMove(PlayerMoveEvent e){
		FileConfiguration fc = YamlConfiguration.loadConfiguration(new File("plugins/Rush/config.yml"));
		FileConfiguration fc2 = YamlConfiguration.loadConfiguration(new File(Main.getWorldsFileLoc()));
		if(Main.getPartyForWorld(e.getPlayer().getWorld().getName())!=null){
			if(e.getPlayer().getLocation().getBlockY() < fc.getInt("death-height")){
				Player p = e.getPlayer();
				if(p.getAllowFlight() && p.getGameMode()==GameMode.SURVIVAL){
					e.getPlayer().setVelocity(new Vector(0,2,0));
				}
				else{
					p.damage(20.0);
				}
			}
			if(e.getPlayer().getLocation().getY() > fc.getInt("max-height")){
				e.getPlayer().setVelocity(new Vector(0,-2,0));
			}
			if(e.getPlayer().getLocation().getX() > fc2.getDouble(e.getPlayer().getWorld().getName()+".maxX"))
				e.getPlayer().setVelocity(new Vector(-2,0,0));
			if(e.getPlayer().getLocation().getX() < fc2.getDouble(e.getPlayer().getWorld().getName()+".minX"))
				e.getPlayer().setVelocity(new Vector(2,0,0));
			if(e.getPlayer().getLocation().getZ() > fc2.getDouble(e.getPlayer().getWorld().getName()+".maxZ"))
				e.getPlayer().setVelocity(new Vector(0,0,-2));
			if(e.getPlayer().getLocation().getZ() < fc2.getDouble(e.getPlayer().getWorld().getName()+".minZ"))
				e.getPlayer().setVelocity(new Vector(0,0,2));
		}
	}
	
}
