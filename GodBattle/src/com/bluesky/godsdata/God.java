package com.bluesky.godsdata;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class God {
	
	public abstract void apply(Player p);
	
	public abstract void primary(Player p);
	
	public abstract void secondary(Player p);
	
	public abstract void ultimate(Player p);
	
	public abstract String getName();
	
	public abstract ItemStack getHat();
}
