package com.wearedevelopers.rush.misc;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener{
	
	private Material[] blacklist = {Material.LEATHER_BOOTS,Material.LEATHER_LEGGINGS,Material.LEATHER_CHESTPLATE,Material.LEATHER_HELMET};
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(e.getCurrentItem()!=null){
			if(Main.getPartyForWorld(e.getWhoClicked().getWorld().getName())!=null && Arrays.asList(blacklist).contains(e.getCurrentItem().getType())){
				e.setCancelled(true);
			}
		}
	}
	
}
