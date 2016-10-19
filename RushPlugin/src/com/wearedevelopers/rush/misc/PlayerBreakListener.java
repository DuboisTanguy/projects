package com.wearedevelopers.rush.misc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBreakListener implements Listener{
	
	@EventHandler
	public void PlayerBreakBlock(BlockBreakEvent e){
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if(Main.getPartyForWorld(p.getWorld().getName())!=null){
			if((b.getType() == Material.BED) || (b.getType() == Material.TNT) || b.getType()==Material.OBSIDIAN || b.getType()==Material.GLOWSTONE || b.getType()==Material.STAINED_GLASS ||
				b.getType()==Material.WOOL || b.getType()==Material.ENDER_PORTAL || b.getType()==Material.LOG || b.getType()==Material.FENCE || b.getType()==Material.CHEST ||
				b.getType()==Material.TRAPPED_CHEST || b.getType()==Material.BED_BLOCK){
				e.setCancelled(true);
				return;
			}
			for(String s : DeathCountdown.getTeamOf(p).getEntries()){
				Player pl = Bukkit.getPlayer(s);
				if(b.getLocation().getBlockX()==pl.getLocation().getBlockX()&&
						b.getLocation().getBlockZ()==pl.getLocation().getBlockZ()&&
						b.getLocation().getBlockY()==pl.getLocation().getBlockY()-1){
					e.setCancelled(true);
				}
			}
		}
	}
}
