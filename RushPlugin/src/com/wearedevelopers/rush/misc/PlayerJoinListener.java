package com.wearedevelopers.rush.misc;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		FileConfiguration fc = YamlConfiguration.loadConfiguration(new File("plugins/Rush/config.yml"));
		Location l = new Location(Bukkit.createWorld(new WorldCreator(fc.getString("spawn.world"))),
				fc.getDouble("spawn.X"),
				fc.getDouble("spawn.Y"),
				fc.getDouble("spawn.Z"));
		e.getPlayer().teleport(l);
		e.getPlayer().setFlying(false);
		e.getPlayer().setAllowFlight(false);
		e.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
		e.getPlayer().setLevel(0);
	}

}
