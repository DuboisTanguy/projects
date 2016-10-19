package com.wearedevelopers.rush.misc;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceblockListener implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		if(Main.getPartyForWorld(e.getPlayer().getWorld().getName())!=null){
			if(e.getBlock().getType()==Material.TNT){
				e.setCancelled((isNearBed(e)));
			}
			FileConfiguration fc = YamlConfiguration.loadConfiguration(new File("plugins/Rush/config.yml"));
			Location l = e.getBlock().getLocation();
			if(l.getY() > fc.getInt("max-height")){
				e.setCancelled(true);
				return;
			}else if(l.getY() < fc.getInt("min-height")){
				e.setCancelled(true);
				return;
			}
			Main.getPartyForWorld(e.getPlayer().getWorld().getName()).addPlacedBlock(e.getBlock());
		}
	}
	
	public boolean isNearBed(BlockPlaceEvent e){
		if(e.getPlayer().getBedSpawnLocation()!=null){
			Location l = e.getPlayer().getBedSpawnLocation();
			double x = l.getX();
			double z = l.getZ();
			Location l2 = e.getBlock().getLocation();
			double x2 = l2.getX();
			double z2 = l2.getZ();
			return ( 
					(getMax(x,x2)-getMin(x,x2)<16)&&(getMax(z,z2)-getMin(z,z2)<16)
					);
		}
		return false;
	}
	
	public double getMax(double a, double b){
		return  (a!=b)?( (a>b)?a:b  ):a;
	}
	
	public double getMin(double a, double b){
		return  (a!=b)?( (a<b)?a:b  ):b;
	}
	
}
