package com.bluesky.godsdata;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bluesky.plugin.MainClass;

public class Dionysos extends God{
	
	private static String name = new String("Dionysos");

	@Override
	public void apply(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.CONFUSION ,  Integer.MAX_VALUE , 100 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 1 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 4 , true);
		p.addPotionEffect(pe3);
		p.setHealth(40);
		p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.CHAINMAIL_BOOTS) ,
														   new ItemStack(Material.LEATHER_LEGGINGS) ,
														   new ItemStack(Material.CHAINMAIL_CHESTPLATE) ,
														   new ItemStack(Material.CHAINMAIL_HELMET) });
		ItemStack is = MainClass.rename(new ItemStack(Material.WOOD_SWORD), ChatColor.RED+"TATANE !", ChatColor.RED+"Mélée");
		is.addEnchantment(Enchantment.KNOCKBACK, 2);
		is.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		p.getInventory().addItem(is);
		
	}

	@Override
	public void primary(Player p) {
		PotionEffect pe4 = new PotionEffect(PotionEffectType.BLINDNESS ,  20 , 100 , true);
		p.addPotionEffect(pe4);
		Block b = p.getTargetBlock((Set<Material>) null, 5000);
		for(int i = 0 ; i<5 ; i++)
			p.getWorld().strikeLightning(b.getLocation());
	}

	@Override
	public void secondary(Player p) {
		PotionEffect pe4 = new PotionEffect(PotionEffectType.BLINDNESS ,  40 , 100 , true);
		p.addPotionEffect(pe4);
		p.launchProjectile(org.bukkit.entity.Arrow.class, p.getEyeLocation().getDirection().multiply(500));
		p.spawnParticle(Particle.FLAME, p.getEyeLocation(), 1000);
	}

	@Override
	public void ultimate(Player p) {
		PotionEffect pe4 = new PotionEffect(PotionEffectType.BLINDNESS ,  40 , 100 , true);
		p.addPotionEffect(pe4);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE ,  6*20 , 10 , true);
		p.addPotionEffect(pe3);
		List<Entity> l = p.getNearbyEntities(5, 5 , 5);
		List<Entity> l2 = new LinkedList<Entity>();
		p.getWorld().spawnEntity(p.getLocation(), EntityType.DRAGON_FIREBALL);
		p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 1000);
		p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 1000);
		p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 1000);
		p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 1000);
		p.playSound(p.getLocation(), Sound.ENTITY_PIG_AMBIENT, 10f, 10f);
		p.playSound(p.getLocation(), Sound.ENTITY_PIG_HURT, 10f, 10f);
		p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT, 10f, 10f);
		for(int i = 0 ; i<3 ; i++)
			p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		for(Entity e : l){
			if(e instanceof Player){
				Player p2 = (Player) e;
				PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS ,  5*20 , 100 , true);
				p2.addPotionEffect(pe);
				PotionEffect pe2 = new PotionEffect(PotionEffectType.CONFUSION ,  10*20 , 100 , true);
				p2.addPotionEffect(pe2);
				PotionEffect pe8 = new PotionEffect(PotionEffectType.POISON ,  6*20 , 4 , true);
				p2.addPotionEffect(pe8);
			}
			else{
				l2.add(e);
			}
		}
		if(l2.size()>=1){
			Entity[] es = new Entity[l2.size()];
			es = l2.toArray(es);
			for(int i=0 ; i<es.length ; i++){
				if(i<es.length-1)
					es[i].setPassenger(es[i+1]);
				else
					es[i].setPassenger(p.getWorld().spawnEntity(p.getLocation(), EntityType.IRON_GOLEM));
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		return    new ItemStack(Material.CHAINMAIL_HELMET);
	}

}
