package com.wearedevelopers.rush.misc;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		//if(cmd.getName().equalsIgnoreCase("rush")){
			if(args.length == 2){
				try{
					Player p = (Player) sender;
					FileConfiguration fc = YamlConfiguration.loadConfiguration(new File("plugins/Rush/config.yml"));
					//FileConfiguration fc2 = YamlConfiguration.loadConfiguration(new File());
					if(args[0].equalsIgnoreCase("minheight")){
						fc.set("min-height", args[1]);
						fc.save(new File("plugins/Rush/config.yml"));
						sender.sendMessage("MIN-HEIGHT CHANGED TO " + args[1]);
						return true;
					}else if(args[0].equalsIgnoreCase("maxheight")){
						fc.set("max-height", args[1]);
						fc.save(new File("plugins/Rush/config.yml"));
						sender.sendMessage("MAX-HEIGHT CHANGED TO " + args[1]);
						return true;
					}else if(args[0].equalsIgnoreCase("deathheight")){
						fc.set("death-height", args[1]);
						fc.save(new File("plugins/Rush/config.yml"));
						sender.sendMessage("DEATH-HEIGHT CHANGED TO " + args[1]);
						return true;
					}
					else if(args[0].equalsIgnoreCase("popper")){
						FileConfiguration c = YamlConfiguration.loadConfiguration(new File(Main.getpopFileLoc()));
						//List<String> pops = 
						return true;
					}
					else if(args[0].equalsIgnoreCase("setglobalspawn")){
						fc.set("spawn.world", p.getWorld().getName());
						fc.set("spawn.X", p.getLocation().getX());
						fc.set("spawn.Y", p.getLocation().getY());
						fc.set("spawn.Z", p.getLocation().getZ());
						fc.save(new File("plugins/Rush/config.yml"));
					}
					else if(args[0].equalsIgnoreCase("setmaxX")){
						
					}
				} catch(Exception e){
					//
				}
			}
		//}
		return false;
	}
	
	
	
}
