package com.bluesky.objects;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

public class Healer {
	
	private Location lo;
	int task;
	
	 public Healer(){
		 //
	 }
	 
	 public Healer(Location l){
		 this.create(l);
	 }
	 
	 public void create(Location l){
		 this.lo= l;
		 ArmorStand h = ((ArmorStand)(l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND)));
		 h.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
		 h.setCustomName(ChatColor.GREEN+"HEALER");
		 h.setGravity(false);
		 h.setInvulnerable(true);
		 h.setCustomNameVisible(true);
		 h.setVisible(false);
		 this.healerCooldown(h);
	 }	 
	 
	 @SuppressWarnings("deprecation")
		public void healerCooldown(Entity e) {
	        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            @SuppressWarnings("unused")
				public void run() {
	            	if(e.isDead()){
	            		e.remove();
	            		Bukkit.getServer().getScheduler().cancelTask(task);
	            		Healer h = new Healer(lo);
	            	}
	            	else{
		            	e.getWorld().spawnParticle(Particle.HEART, e.getLocation(), 500);
		            	AreaEffectCloud ae = ((AreaEffectCloud)(e.getWorld().spawnEntity(e.getLocation(), EntityType.AREA_EFFECT_CLOUD)));
		            	ae.setDuration(5*20);
		        		e.setPassenger(ae);
		        		ae.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL , false , false));
		        		List<Entity> l =ae.getNearbyEntities(5, 5, 5);
		        		for (Entity e : l){
		        			if(e instanceof Player){
		        				Player p = (Player) e;
		        				if(!(p.getFoodLevel()>=20))
		        					p.setFoodLevel(p.getFoodLevel()+4);
		        			}
		        		}
	        		}
	            }
	        }, 0L, 15*20L);
	    }
}
