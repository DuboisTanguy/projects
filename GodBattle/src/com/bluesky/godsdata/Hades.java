package com.bluesky.godsdata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.bluesky.plugin.CoolDown2;
import com.bluesky.plugin.MainClass;

public class Hades extends God{
	
	private static HashMap<String , Integer> hm = new HashMap<String , Integer>();
	private static HashMap<String , Integer> hm2 = new HashMap<String , Integer>();
	private static String name = new String("Hades");

	@Override
	public void apply(Player p) {
		PotionEffect pe = new PotionEffect(PotionEffectType.FIRE_RESISTANCE ,  Integer.MAX_VALUE , 2 , true);
		p.addPotionEffect(pe);
		PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
		p.addPotionEffect(pe2);
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 4 , true);
		p.addPotionEffect(pe3);
		p.setHealth(40);
		p.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.IRON_BOOTS) ,
														   new ItemStack(Material.GOLD_LEGGINGS) ,
														   new ItemStack(Material.DIAMOND_CHESTPLATE) ,
														   new ItemStack(Material.DIAMOND_HELMET) });
		p.getInventory().addItem(MainClass.rename(new ItemStack(Material.IRON_SWORD), ChatColor.RED+"Mélée", ChatColor.RED+"Mélée"));
	}

	@Override
	public void primary(Player p) {
		Block b = p.getTargetBlock((Set<Material>) null, 50);
		b = b.getLocation().clone().add(new Location(p.getWorld(),0 , 1 , 0)).getBlock();
		b.setType(Material.FIRE);
		Block[] bs = { b.getLocation().clone().add(new Location(p.getWorld(),1 , 0 , 0)).getBlock() ,
				       b.getLocation().clone().add(new Location(p.getWorld(),0 , 0 , 1)).getBlock() ,
				       b.getLocation().clone().add(new Location(p.getWorld(),-1 , 0 , 0)).getBlock() ,
				       b.getLocation().clone().add(new Location(p.getWorld(),0 , 0 , -1)).getBlock() ,
				       b.getLocation().clone().add(new Location(p.getWorld(),1 , 0 , 1)).getBlock() ,
				       b.getLocation().clone().add(new Location(p.getWorld(),1 , 0 , -1)).getBlock() ,
				       b.getLocation().clone().add(new Location(p.getWorld(),-1 , 0 , 1)).getBlock() ,
				       b.getLocation().clone().add(new Location(p.getWorld(),-1 , 0 , -1)).getBlock() ,};
		List l = Arrays.asList(bs);
		for(Block b2 : bs){
			if(b2.getType()==Material.AIR)
				b2.setType(Material.FIRE);
			else
				l.remove(b2);
		}
		bs =(Block[]) l.toArray();
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 1 , hm);
		c.startCooldown(p , hm , bs , b);
	}

	@Override
	public void secondary(Player p) {
		for(int i = 0 ; i<5 ; i++)
			p.getWorld().spawn(p.getLocation(), org.bukkit.entity.Skeleton.class);
		Vector v = p.getEyeLocation().getDirection();
		v.setX(-v.getX());
		v.setZ(-v.getZ());
		v.multiply(1.6);
		v.setY(0.7);
		p.setVelocity(v);	
	}

	@Override
	public void ultimate(Player p) {
		Block b = p.getTargetBlock((Set<Material>) null, 50);
		p.getWorld().strikeLightning(b.getLocation());
		b = b.getLocation().clone().add(new Location(p.getWorld(),0 , 1 , 0)).getBlock();
		b.setType(Material.LAVA);
		Block[] bs = { b.getLocation().clone().add(new Location(p.getWorld(),1 , 1 , 0)).getBlock() ,
			       b.getLocation().clone().add(new Location(p.getWorld(),0 , 1 , 1)).getBlock() ,
			       b.getLocation().clone().add(new Location(p.getWorld(),-1 , 1 , 0)).getBlock() ,
			       b.getLocation().clone().add(new Location(p.getWorld(),0 , 1 , -1)).getBlock() ,
			       b.getLocation().clone().add(new Location(p.getWorld(),1 , 1 , 1)).getBlock() ,
			       b.getLocation().clone().add(new Location(p.getWorld(),1 , 1 , -1)).getBlock() ,
			       b.getLocation().clone().add(new Location(p.getWorld(),-1 , 1 , 1)).getBlock() ,
			       b.getLocation().clone().add(new Location(p.getWorld(),-1 , 1 , -1)).getBlock() ,
			       b.getLocation().clone().add(new Location(p.getWorld(),0 , 1 , 0)).getBlock() ,};
		List l = Arrays.asList(bs);
		for(Block b2 : bs){
			if(b2.getType()==Material.AIR)
				b2.setType(Material.WEB);
			else
				l.remove(b2);
		}
		bs =(Block[]) l.toArray();
		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
		c.setCooldownLength(p , 10 , hm2);
		c.startCooldown(p , hm2 , bs , b);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		return  new ItemStack(Material.DIAMOND_HELMET);
	}
}
