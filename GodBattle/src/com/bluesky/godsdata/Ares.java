package com.bluesky.godsdata;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.bluesky.plugin.CoolDown2;
import com.bluesky.plugin.MainClass;

public class Ares extends God{

	private static HashMap<String , Integer> hm = new HashMap<String , Integer>();
	private static HashMap<String , Integer> hm2 = new HashMap<String , Integer>();
	private static String name = new String("Ares");
	
	@Override
	public void apply(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.INCREASE_DAMAGE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 4 , true);
		p.addPotionEffect(pe3);
		p.setHealth(40);
		p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.CHAINMAIL_BOOTS) ,
														   new ItemStack(Material.DIAMOND_LEGGINGS) ,
														   new ItemStack(Material.DIAMOND_CHESTPLATE) ,
														   new ItemStack(Material.CHAINMAIL_HELMET) });
		p.getInventory().addItem(MainClass.rename(new ItemStack(Material.DIAMOND_SWORD), ChatColor.RED+"Mélée", ChatColor.RED+"Mélée"));
	}

	@Override
	public void primary(Player p) {
		Location l = p.getLocation();
		p.setVelocity(p.getEyeLocation().getDirection().multiply(1.2));
		p.setInvulnerable(true);
		p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 100);
		p.getWorld().createExplosion(l, 1);
		p.setInvulnerable(false);
		
	}

	@Override
	public void secondary(Player p) {
		p.spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 1);
		p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		PotionEffect pe = new PotionEffect(PotionEffectType.INCREASE_DAMAGE ,  Integer.MAX_VALUE , 3 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE , Integer.MAX_VALUE , 3 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.SLOW ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe3);
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 6 , hm);
		c.startCooldownRage(p, hm);
	}

	@Override
	public void ultimate(Player p) {
		Location l = p.getLocation();
		p.setVelocity(new Vector(0 , 1 , 0));
		p.setInvulnerable(true);
		p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 500);
		p.getWorld().createExplosion(l, 7);
		p.setInvulnerable(false);
		p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		PotionEffect pe = new PotionEffect(PotionEffectType.INCREASE_DAMAGE ,  Integer.MAX_VALUE , 3 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE , Integer.MAX_VALUE , 10 , true);
		p.addPotionEffect(pe2);
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 3 , hm2);
		c.startCooldownRage(p, hm2);
		for(int i = 0 ; i<5 ; i++)
			p.getWorld().spawn(p.getLocation(), org.bukkit.entity.Creeper.class);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		return   new ItemStack(Material.CHAINMAIL_HELMET);
	}

}
