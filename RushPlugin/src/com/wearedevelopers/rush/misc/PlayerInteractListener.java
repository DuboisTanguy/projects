package com.wearedevelopers.rush.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wearedevelopers.rush.party.Party;

public class PlayerInteractListener implements Listener{
	
	public void PlayerInteract(PlayerInteractEvent e){
		if(Main.getPartyForWorld(e.getPlayer().getWorld().getName())!=null){
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK){
				if(e.getClickedBlock().getType()==Material.BED_BLOCK || e.getClickedBlock().getType()==Material.BED){
					e.setCancelled(true);
				}
				else if(e.getClickedBlock().getType()==Material.CHEST||e.getClickedBlock().getType()==Material.TRAPPED_CHEST){
					e.setCancelled(true);
					Player p = e.getPlayer();
					Party party = Main.getPartyForWorld(p.getWorld().getName());
					Location l = e.getClickedBlock().getLocation();
					if(party.getInvForLoc(l)==null){
						party.addInv(l, Bukkit.createInventory(null, InventoryType.CHEST));
					}
					p.openInventory(party.getInvForLoc(l));
				}
			}
		}
	}
}
