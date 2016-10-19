package com.bluesky.godsdata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bluesky.plugin.CoolDown2;
import com.bluesky.plugin.MainClass;

public class Artemis extends God{
	
	private static HashMap<String , Integer> hm = new HashMap<String , Integer>();
	private static HashMap<Player, Entity[]> hm2 = new HashMap<Player , Entity[]>();
	private static String name = new String("Artemis");

	@Override
	public void apply(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.SPEED ,  Integer.MAX_VALUE , 1 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 3 , true);
		p.addPotionEffect(pe3);
		p.setHealth(36);
		p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.CHAINMAIL_BOOTS) ,
														   new ItemStack(Material.IRON_LEGGINGS) ,
														   new ItemStack(Material.CHAINMAIL_CHESTPLATE) ,
														   new ItemStack(Material.DIAMOND_HELMET) });
		ItemStack is = MainClass.rename(new ItemStack(Material.BOW), ChatColor.RED+"Arc", ChatColor.RED+"Arme à distance");
		is.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		is.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
		is.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
		p.getInventory().addItem(is);
		p.getInventory().setItem(8,new ItemStack(Material.ARROW));
	}

	@Override
	public void primary(Player p) {
		if (hm2.get(p)!=null){
			Entity[] es = hm2.get(p);
			for(Entity e : es){
				e.remove();
			}
		}
		p.spawnParticle(Particle.HEART, p.getEyeLocation(), 100);
		List<Entity> l = new LinkedList<Entity>();
		for(int i = 0 ; i<3 ; i++){
			Wolf h = (Wolf)p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
			h.setAdult();
			h.setTamed(true);
			h.setOwner(p);
			l.add(h);
		}
		hm2.put(p, l.toArray(new Entity[l.size()]));
	}

	@Override
	public void secondary(Player p) {
		p.removePotionEffect(PotionEffectType.SPEED);
		PotionEffect pe = new PotionEffect(PotionEffectType.SPEED ,  Integer.MAX_VALUE , 3 , true);
		p.addPotionEffect(pe);
		List<Entity> l = p.getNearbyEntities(75, 75 , 75);
		for(Entity e : l){
			e.setGlowing(true);
		}
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 6 , hm);
		c.startCooldownGlow(p, hm , l);
		
	}

	@Override
	public void ultimate(Player p) {
		PotionEffect pe4 = new PotionEffect(PotionEffectType.BLINDNESS ,  40 , 100 , true);
		p.addPotionEffect(pe4);
		PotionEffect pe = new PotionEffect(PotionEffectType.SLOW ,  5*20 , 5 , true);
		p.addPotionEffect(pe);
		p.launchProjectile(org.bukkit.entity.Arrow.class, p.getEyeLocation().getDirection().multiply(500));
		p.spawnParticle(Particle.PORTAL, p.getEyeLocation(), 1000);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		return    new ItemStack(Material.DIAMOND_HELMET);
	}

}
