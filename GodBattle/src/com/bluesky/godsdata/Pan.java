package com.bluesky.godsdata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bluesky.plugin.CoolDown2;
import com.bluesky.plugin.MainClass;

public class Pan extends God{
	
	private static String name = new String("Pan");
	private static HashMap<Player , Entity> hm2 = new HashMap<Player , Entity>();
	private static HashMap<String , Integer> hm = new HashMap<String , Integer>();

	@Override
	public void apply(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.JUMP ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 9 , true);
		p.addPotionEffect(pe3);
		p.setHealth(60);
		p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.IRON_BOOTS) ,
														   new ItemStack(Material.LEATHER_LEGGINGS) ,
														   new ItemStack(Material.IRON_CHESTPLATE) ,
														   new ItemStack(Material.LEATHER_HELMET) });
		ItemStack is = MainClass.rename(new ItemStack(Material.IRON_SWORD), ChatColor.RED+"Mélée", ChatColor.RED+"Mélée");
		is.addEnchantment(Enchantment.KNOCKBACK, 1);
		p.getInventory().addItem(is);
		
	}

	@Override
	public void primary(Player p) {
		if (hm2.get(p)!=null){
			hm2.get(p).remove();
		}
		p.spawnParticle(Particle.SMOKE_NORMAL, p.getLocation(), 100);
		Horse h = (Horse)p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
		h.setAdult();
		h.setStyle(Style.WHITE_DOTS);
		h.setRemoveWhenFarAway(true);
		h.setMaxHealth(40);
		h.setHealth(40);
		h.setMaxDomestication(h.getMaxDomestication());
		h.setJumpStrength(1.4);
		h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		h.getInventory().setArmor(new ItemStack(Material.DIAMOND_BARDING));
		h.setPassenger(p);
		hm2.put(p, h);
	}

	@Override
	public void secondary(Player p) {
		Block b = p.getTargetBlock((Set<Material>) null, 50);
		Location lo = b.getLocation();
		Chicken h = (Chicken)p.getWorld().spawnEntity(lo.clone().add(new Location(p.getWorld(),0,2,0)), EntityType.CHICKEN);
		List<Entity> l =h.getNearbyEntities(4, 4, 4);
		for(int i = 0 ; i<2 ; i++){
			h = (Chicken)p.getWorld().spawnEntity(lo.clone().add(new Location(p.getWorld(),0,2,0)), EntityType.CHICKEN);
		}
		p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, lo, 250);
		for(Entity e : l){
			if(e instanceof Player){
				PotionEffect pe = new PotionEffect(PotionEffectType.POISON ,  5*20 , 2 , true);
				( (Player) e).addPotionEffect(pe);
			}
		}
	}

	@Override
	public void ultimate(Player p) {
		Block b = p.getTargetBlock((Set<Material>) null, 50);
		Location lo = b.getLocation();
		Creeper h = (Creeper)p.getWorld().spawnEntity(lo.clone().add(new Location(p.getWorld(),0,2,0)), EntityType.CREEPER);
		List<Entity> l =h.getNearbyEntities(4, 4, 4);
		for(int i = 0 ; i<4 ; i++){
			h = (Creeper)p.getWorld().spawnEntity(lo.clone().add(new Location(p.getWorld(),0,2,0)), EntityType.CREEPER);
		}
		p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, lo, 250);
		List<Block> bl = new LinkedList<Block>();
		for(Entity e : l){
			bl.add(e.getLocation().getBlock());
			e.getLocation().getBlock().setType(Material.WEB);
			bl.add(e.getLocation().clone().add(0,1,0).getBlock());
			e.getLocation().clone().add(0,1,0).getBlock().setType(Material.WEB);
		}
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 10 , hm);
		Block[] bs = new Block[bl.size()];
		bs = bl.toArray(bs);
		c.startCooldown(p , hm , bs , b.getLocation().add(0,1,0).getBlock());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		return  new ItemStack(Material.LEATHER_HELMET);
	}
	
}
