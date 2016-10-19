package com.wearedevelopers.rush.misc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignBreakListener implements Listener{

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(e.getBlock()!=null){
			Material m = e.getBlock().getType();
			if(m==Material.SIGN||m==Material.SIGN_POST||m==Material.WALL_SIGN){
				Sign s = (Sign) e.getBlock().getState();
				if(Main.getWorldForSign(s)!=null){
					FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getSwFileLoc()));
					fc.set("signs."+Main.getWorldForSign(s), null);
					List<String> worlds = (List<String>) fc.getList("worlds");
					worlds.remove(Main.getWorldForSign(s));
					fc.set("worlds", worlds);
					try {
						fc.save(new File(Main.getSwFileLoc()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Main.initHm();
				}
			}
		}
	}
	
}
