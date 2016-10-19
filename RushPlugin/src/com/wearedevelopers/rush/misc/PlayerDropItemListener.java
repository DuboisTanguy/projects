package com.wearedevelopers.rush.misc;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener{

	private Material[] blacklist = {Material.LEATHER_BOOTS,Material.LEATHER_LEGGINGS,Material.LEATHER_CHESTPLATE,Material.LEATHER_HELMET,Material.WOOL};
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e){
		if(Main.getPartyForWorld(e.getPlayer().getWorld().getName())!=null && Arrays.asList(blacklist).contains(e.getItemDrop().getType())){
			e.setCancelled(true);
		}
	}
	
}
