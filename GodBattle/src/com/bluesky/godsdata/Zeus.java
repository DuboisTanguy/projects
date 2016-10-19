package com.bluesky.godsdata;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.bluesky.plugin.MainClass;

public class Zeus extends God{
	
	private static String name = new String("Zeus");
	
	public Zeus(){
		//
	}
	
	public Zeus(Player p){
		Bukkit.broadcastMessage(p.getName()+" est devenu "+name+" !");
	}
	
	@Override
	public void apply(Player p){
		PotionEffect pe = new PotionEffect(PotionEffectType.JUMP ,  Integer.MAX_VALUE , 2 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 4 , true);
		p.addPotionEffect(pe3);
		p.setHealth(40);
		p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.DIAMOND_BOOTS) ,
														   new ItemStack(Material.IRON_LEGGINGS) ,
														   new ItemStack(Material.IRON_CHESTPLATE) ,
														   new ItemStack(Material.GOLD_HELMET) });
		p.getInventory().addItem(MainClass.rename(new ItemStack(Material.IRON_SWORD), ChatColor.RED+"Mélée", ChatColor.RED+"Mélée"));
	}

	@Override
	public void primary(Player p) {
		Block b = p.getTargetBlock((Set<Material>) null, 50);
		p.getWorld().strikeLightning(b.getLocation());
	}

	@Override
	public void secondary(Player p) {
		List<Entity> l = p.getNearbyEntities(10, 30 , 10);
		p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 1000);
		for(Entity e : l){
			Vector v = new Vector();
			v.setY(1);
			v.setX(0);
			v.setZ(0);
			v.multiply(2);
			e.setVelocity(v);
		}
	}

	@Override
	public void ultimate(Player p) {		
		Location l = p.getLocation();
		World w = p.getWorld();
		w.createExplosion(l.clone().add(10, 0, 0), 6);
		w.createExplosion(l.clone().add(0, 0, 10), 6);
		w.createExplosion(l.clone().add(0, 0, -10), 6);
		w.createExplosion(l.clone().add(-10, 0, 0), 6);
		w.createExplosion(l.clone().add(8, 0, 8), 6);
		w.createExplosion(l.clone().add(-8, 0, 8), 6);
		w.createExplosion(l.clone().add(8, 0, -8), 6);
		w.createExplosion(l.clone().add(-8, 0, -8), 6);
		
		w.strikeLightning(l.clone().add(10, 0, 0));
		w.strikeLightning(l.clone().add(0, 0, 10));
		w.strikeLightning(l.clone().add(0, 0, -10));
		w.strikeLightning(l.clone().add(-10, 0, 0));
		w.strikeLightning(l.clone().add(8, 0, 8));
		w.strikeLightning(l.clone().add(-8, 0, 8));
		w.strikeLightning(l.clone().add(8, 0, -8));
		w.strikeLightning(l.clone().add(-8, 0, -8));
		
		PotionEffect pe = new PotionEffect(PotionEffectType.SLOW , 5 , 2 , true);
		p.addPotionEffect(pe);
	}
	
	@Override
	public String getName(){
		return name;
	}

	@Override
	public ItemStack getHat() {
		return    new ItemStack(Material.GOLD_HELMET);
	}

}
