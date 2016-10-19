package com.bluesky.godsdata;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bluesky.plugin.CoolDown2;
import com.bluesky.plugin.MainClass;

public class Poseidon extends God {
	
	private static String name = new String("Poseidon");
	private static HashMap<String , Integer> hm1 = new HashMap<String , Integer>();
	@Override
	public void apply(Player p) {
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 1 , true);
		p.addPotionEffect(pe3);
		p.setHealth(28);
		PotionEffect pe = new PotionEffect(PotionEffectType.SPEED, 9999*20, 1, true);
		p.addPotionEffect(pe);
		PotionEffect pe1 = new PotionEffect(PotionEffectType.WATER_BREATHING, 9999*20, 4, true);
		p.addPotionEffect(pe1);
		ItemStack item = new ItemStack(Material.IRON_BOOTS);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
		item.setItemMeta(meta);
		p.getInventory().setArmorContents(new ItemStack[]{ item ,
				   new ItemStack(Material.DIAMOND_LEGGINGS) ,
				   new ItemStack(Material.IRON_CHESTPLATE) ,
				   new ItemStack(Material.DIAMOND_HELMET) });
p.getInventory().addItem(MainClass.rename(new ItemStack(Material.IRON_SWORD), ChatColor.RED+"Mélée", ChatColor.RED+"Mélée"));
	}

	@Override
	public void primary(Player p) {
		Block b = p.getTargetBlock((Set<Material>)null, 50);
		List<Entity> l = (List<Entity>) b.getLocation().getWorld().getNearbyEntities(b.getLocation(), 3, 10, 3);
		for(Entity e : l){
			if(e instanceof Player){
				Player o = ((Player) e).getPlayer();
				PotionEffect pe = new PotionEffect(PotionEffectType.POISON, 4*20, 2, true);
				PotionEffect pe1 = new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 2, true);
				o.addPotionEffect(pe);
				o.addPotionEffect(pe1);
			}
			else{
				e.remove();
			}
	}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void secondary(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.REGENERATION, 5*20, 2, true);
		p.addPotionEffect(pe);
		int i = 5;
		while(i!=0){
			p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 1);
			i--;
		}
		List<Entity> l = (List)p.getNearbyEntities(5, 10, 5);
		List<Entity> l2 = (List)p.getNearbyEntities(100, 100, 100);
		for(Entity e : l){
			if(e instanceof Player){
				((Player) e).getInventory().setHelmet(new ItemStack(Material.GLASS));
				PotionEffect pe1 = new PotionEffect(PotionEffectType.POISON, 5*20, 3, true);
				((Player) e).addPotionEffect(pe1);
			}
		}
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 5 , hm1);
		c.startgetHat(p , hm1 , l2);
	}

	@Override
	public void ultimate(Player p) {
		Block b = p.getTargetBlock((Set<Material>)null, 100);
		List<Entity> l = (List<Entity>) b.getLocation().getWorld().getNearbyEntities(b.getLocation(), 5, 5, 5);
		for(Entity e : l){
			if(e instanceof Player){
				PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS , 10*20 , 1 , true);
				((Player) e).addPotionEffect(pe);
				Location loc = e.getLocation();
				int i = 8;
						while(i!=0){
							e.getWorld().createExplosion(loc, 2);
							e.getWorld().strikeLightning(loc);
							i--;
						}
					
				
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		return new ItemStack(Material.DIAMOND_HELMET);
	}

}
