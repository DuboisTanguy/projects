package com.bluesky.godsdata;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.bluesky.plugin.CoolDown2;
import com.bluesky.plugin.MainClass;

public class Appolon extends God {
	private static HashMap<String , Integer> hm = new HashMap<String , Integer>();
	private static String name = new String("Appolon");

	@Override
	public void apply(Player p) {
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 1 , true);
		p.addPotionEffect(pe3);
		p.setHealth(28);
		PotionEffect pe = new PotionEffect(PotionEffectType.JUMP, 9999 * 20, 2, true);
		p.addPotionEffect(pe);
		PotionEffect pe1 = new PotionEffect(PotionEffectType.SPEED, 9999 * 20, 0, true);
		p.addPotionEffect(pe1);
		 p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.IRON_BOOTS) ,
                 new ItemStack(Material.IRON_LEGGINGS) ,
                 new ItemStack(Material.IRON_CHESTPLATE) ,
                 new ItemStack(Material.IRON_HELMET) });
		 ItemStack item = new ItemStack(Material.WOOD_SWORD);
		 ItemMeta meta = item.getItemMeta();
		 meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
		 item.setItemMeta(meta);
		 p.getInventory().addItem(MainClass.rename(item, ChatColor.RED+"Mélée", ChatColor.RED+"Mélée"));
	}

	@Override
	public void primary(Player p) {
		try{
			if(p.getHealth() == 28.0d) {
				p.setHealth(28.0d);
				}
			else if(p.getHealth() < 27.0d){
				p.setHealth(p.getHealth() + 3.0d);
				p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 1);
			}
			else{
				
			}
		}catch(Exception e){
			
		}
	}

	@Override
	public void secondary(Player p) {
		Vector v1 = p.getEyeLocation().getDirection().multiply(3);
		p.setInvulnerable(true);
		p.getWorld().createExplosion(p.getLocation(), 3);
		p.setVelocity(v1);
		p.setInvulnerable(false);
		p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);
		p.spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 1000);
	}

	@Override
	public void ultimate(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.HARM, 1 * 20, 0, true);
		PotionEffect pe1 = new PotionEffect(PotionEffectType.POISON, 5 * 20, 1, true);
		List<Entity> l = p.getNearbyEntities(10, 30 , 10);
		for(Entity e : l){
			if(e instanceof Player){
				Player o = ((Player) e).getPlayer();
				o.addPotionEffect(pe);
				o.addPotionEffect(pe);
				o.addPotionEffect(pe);
				o.addPotionEffect(pe1);
			}
		}
		
		p.setInvulnerable(true);
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 5 , hm);
		c.startInvulnerable(p, hm);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		return new ItemStack(Material.IRON_HELMET);
	}

}
