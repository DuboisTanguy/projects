package com.bluesky.godsdata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.bluesky.plugin.CoolDown2;
import com.bluesky.plugin.MainClass;

public class Hephaistos extends God{

	private static String name = new String("Hephaïstos");
	private static HashMap<String , Integer> hm = new HashMap<String , Integer>();
	private static HashMap<String , Integer> hm4 = new HashMap<String , Integer>();
	private static HashMap<String , Integer> hm3 = new HashMap<String , Integer>();
	private static HashMap<String , Integer> hm5 = new HashMap<String , Integer>();
	private static HashMap<Player , List<Entity>> hm2 = new HashMap<Player , List<Entity>>();
	
	@Override
	public void apply(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.INCREASE_DAMAGE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 3 , true);
		p.addPotionEffect(pe3);
		p.setHealth(36);
		p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.CHAINMAIL_BOOTS) ,
														   new ItemStack(Material.IRON_LEGGINGS) ,
														   new ItemStack(Material.IRON_CHESTPLATE) ,
														   new ItemStack(Material.DIAMOND_HELMET) });
		p.getInventory().addItem(MainClass.rename(new ItemStack(Material.IRON_AXE), ChatColor.RED+"Mélée", ChatColor.RED+"Mélée"));
	}

	@Override
	public void primary(Player p) {
		p.spawnParticle(Particle.SMOKE_NORMAL, p.getLocation(), 100);
		p.launchProjectile(org.bukkit.entity.Fireball.class , p.getEyeLocation().getDirection().multiply(3));
	}

	@Override
	public void secondary(Player p) {	
		Chicken h = ((Chicken)(p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.CHICKEN)));
		h.setAdult();
		h.setVelocity(p.getEyeLocation().getDirection().multiply(2));
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 3 , hm);
		c.startCooldownExplode(p , hm , h , 9);
	}

	@Override
	public void ultimate(Player p) {
		ArmorStand h = (ArmorStand) bombCreate(p);
		AreaEffectCloud h2 = ((AreaEffectCloud)(p.getWorld().spawnEntity(h.getLocation(), EntityType.AREA_EFFECT_CLOUD)));
		h.setPassenger(h2);
		h2.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE , false , true));
		if(hm2.get(p)!=null){
			if(hm2.get(p).size()==3){
				List<Entity> l = hm2.get(p);
				l.get(0).remove();
				l.remove(0);
				l.add(h);
				hm2.put(p, l);
			}
			else{
				List<Entity> l = hm2.get(p);
				l.add(h);
				hm2.put(p, l);
			}
		}
		else{
			List<Entity> l = new LinkedList<Entity>();
			l.add(h);
			hm2.put(p, l);
		}
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 10 , hm3);
		c.startCooldownExplode(p , hm3 , h , 4);
		CoolDown2 c2 = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c2.setCooldownLength(p , 10 , hm4);
		c2.startCooldownExplode(p , hm4 , h2 , 1);
		CoolDown2 c3 = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c3.setCooldownLength(p , 10 , hm5);
		c3.startCooldownExplode(p , hm5 , h2 , 1);
		
	}

	@Override
	public String getName() {
		return name;
	}
	
	private static Entity bombCreate(Player p){
		ArmorStand h = ((ArmorStand)(p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND)));
		h.setHelmet(new ItemStack(Material.TNT));
		h.setVelocity(p.getEyeLocation().getDirection().multiply(2));
		h.setCustomName(ChatColor.RED+"/!\\");
		h.setCustomNameVisible(true);
		h.setVisible(false);
		
		return h;
	}
	
	@Override
	public ItemStack getHat(){
		return new ItemStack(Material.DIAMOND_HELMET);
	}
}
