package com.wearedevelopers.rush.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftItemListener implements Listener{
	
	@EventHandler
	public void onItemCrafted(CraftItemEvent e){
		if(Main.getPartyForWorld(e.getWhoClicked().getWorld().getName())!=null){
			e.setCancelled(true);
		}
	}
	
}
