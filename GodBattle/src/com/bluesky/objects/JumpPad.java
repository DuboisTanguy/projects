package com.bluesky.objects;

import org.bukkit.Location;
import org.bukkit.Material;

public class JumpPad {
	
	public JumpPad(){
		
	}
	public JumpPad(Location l){
		l.add(0d, -1d, 0d);
		l.getBlock().setType(Material.BEDROCK);
		l.add(0d, 1d, 0d);
		l.getBlock().setType(Material.IRON_PLATE);
	}
	
	
}
