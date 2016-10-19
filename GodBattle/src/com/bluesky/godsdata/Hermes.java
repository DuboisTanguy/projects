package com.bluesky.godsdata;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bluesky.plugin.CoolDown2;
import com.bluesky.plugin.MainClass;

public class Hermes extends God{
	
	private static HashMap<String , Integer> hm = new HashMap<String , Integer>();
	private static String name = new String("Hermes");
	
	@Override
	public void apply(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.SPEED ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 1 , true);
		p.addPotionEffect(pe3);
		p.setHealth(28);
		p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.LEATHER_BOOTS) ,
														   new ItemStack(Material.DIAMOND_LEGGINGS) ,
														   new ItemStack(Material.LEATHER_CHESTPLATE) ,
														   new ItemStack(Material.DIAMOND_HELMET) });
		p.getInventory().addItem(MainClass.rename(new ItemStack(Material.DIAMOND_SWORD), ChatColor.RED+"Mélée", ChatColor.RED+"Mélée"));
		
	}

	@Override
	public void primary(Player p) {
		Block b = p.getTargetBlock((Set<Material>) null, 20);
		Location lo = b.getLocation().clone().add(new Location(p.getWorld(), 0 , 1 , 0));
		lo.setDirection(p.getEyeLocation().getDirection());
		p.teleport(lo);
		p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getLocation(), 250);
		List<Entity> l = p.getNearbyEntities(2, 2 , 2);
		for(Entity e : l){
			if(e instanceof Player){
				PotionEffect pe = new PotionEffect(PotionEffectType.HARM ,  1 , 0 , true);
				((Player) e).addPotionEffect(pe);
			}
		}
	}

	@Override
	public void secondary(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.INVISIBILITY , Integer.MAX_VALUE, 0 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0 , true);
		p.addPotionEffect(pe3);
		p.getInventory().setArmorContents(null);
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 7 , hm);
		c.startCooldownInv(p, hm);
	}

	@Override
	public void ultimate(Player p) {
		Block b = p.getTargetBlock((Set<Material>) null, 100);
		p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 500);
		p.teleport(b.getLocation().clone().add(new Location(p.getWorld(), 0 , 1 , 0)));
		List<Entity> l = p.getNearbyEntities(2, 2 , 2);
		p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 500);
		p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getLocation(), 250);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5f, 5f);
		for(Entity e : l){
			if(e instanceof Player){
				((Player) e).setHealth(0.0D);
			}
			else{
				e.remove();
			}
		}
		if(p.getHealth()<=14)
			p.setHealth(0.0D);
		else{
			p.setHealth(p.getHealth()-14);
			PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS, 2*20, 1 , true);
			p.addPotionEffect(pe);
			PotionEffect pe2 = new PotionEffect(PotionEffectType.SLOW, 4*20, 1 , true);
			p.addPotionEffect(pe2);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		return   new ItemStack(Material.DIAMOND_HELMET);
	}

}
