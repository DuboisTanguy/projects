package com.wearedevelopers.rush.misc;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Team;

public class AsyncPlayerChatListener implements Listener{

	@EventHandler
	public void onAsyncplayerChat(AsyncPlayerChatEvent e){
		FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getPlayersFileLoc()));
		FileConfiguration fc2 = YamlConfiguration.loadConfiguration(new File(Main.getPointsFileLoc()));
		Player p = e.getPlayer();
		String points = "["+ ChatColor.GREEN+ ( (fc2.get(p.getName())!=null)?fc2.getInt(p.getName()):0) +ChatColor.RESET+"]";
		String vip = fc.getStringList("VIPplus").contains(p.getName())?ChatColor.translateAlternateColorCodes('&', "&e")+"[VIP+]"+ChatColor.RESET:( (fc.getStringList("VIP").contains(p.getName()))?ChatColor.GOLD+"[VIP]"+ChatColor.RESET:"" ) ;
		String name = p.getDisplayName();
		Team t = DeathCountdown.getTeamOf(p);
		String tm = (t!=null)?t.getName()+ChatColor.RESET:"";
		if(t!=null){
			if(!(e.getMessage().startsWith("@"))){
				e.setCancelled(true);
				String msg = points+" <Privé "+tm+">"+" "+vip+" "+name+": "+e.getMessage();
				for(String s : t.getEntries()){
					Player to = Bukkit.getPlayer(s);
					to.sendMessage(msg);
				}
			}
			else{
				e.setMessage(e.getMessage().replaceFirst("@", ""));
			}
		}
		e.setFormat(points+" "+tm+" "+vip+" %1$s: %2$s");
	}
	
}
