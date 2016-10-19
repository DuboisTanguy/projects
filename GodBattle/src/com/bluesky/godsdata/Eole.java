package com.bluesky.godsdata;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.bluesky.plugin.MainClass;

public class Eole extends God{
	
	private static String name = new String("Eole");

	@Override
	public void apply(Player p) {
		PotionEffect pe3 = new PotionEffect(PotionEffectType.HEALTH_BOOST ,  Integer.MAX_VALUE , 1 , true);
		p.addPotionEffect(pe3);
		p.setHealth(28);
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
		ItemMeta bootsmeta = boots.getItemMeta();
		bootsmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		boots.setItemMeta(bootsmeta);
		
		ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemMeta leggmeta = leggings.getItemMeta();
		leggmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		leggings.setItemMeta(leggmeta);
		
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemMeta helmetmeta = helmet.getItemMeta();
		helmetmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		helmet.setItemMeta(helmetmeta);
		
		PotionEffect pe = new PotionEffect(PotionEffectType.JUMP, 9999 * 20, 2, true);
		p.addPotionEffect(pe);
		PotionEffect pe1 = new PotionEffect(PotionEffectType.SPEED, 9999 * 20, 0, true);
		p.addPotionEffect(pe1);
		 p.getInventory().setArmorContents(new ItemStack[]{ boots ,
                 leggings ,
                 new ItemStack(Material.ELYTRA) ,
                helmet });
		 ItemStack item = new ItemStack(Material.GOLD_SWORD);
		 ItemMeta meta = item.getItemMeta();
		 meta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
		 item.setItemMeta(meta);
		 p.getInventory().addItem(MainClass.rename(item, ChatColor.RED+"Mélée", ChatColor.RED+"Mélée"));
		
	}

	@Override
	public void primary(Player p) {
		Block b = p.getTargetBlock((Set<Material>)null, 50);
		List<Entity> l = (List<Entity>) b.getLocation().getWorld().getNearbyEntities(b.getLocation(), 3, 10, 3);
		int i = 5;
		while(i!=0){
			b.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, b.getLocation().add(0, 1, 0), 5);
			i--;
		}
		for(Entity e : l){
			if(e instanceof Player){
				Player o = ((Player) e).getPlayer();
				PotionEffect pe = new PotionEffect(PotionEffectType.HARM, 1, 0, true);
				o.addPotionEffect(pe);
			}
			else{
				e.remove();
			}
	}
}
		

	@Override
	public void secondary(Player p) {
		Vector v = new Vector();
		v.setY(3);
		v.multiply(1.25d);
		p.setVelocity(v);
		p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 1000);
	}

	@Override
	public void ultimate(Player p) {
		Arrow a = (Arrow)p.launchProjectile(Arrow.class);
		a.setVelocity(p.getLocation().getDirection().multiply(2.75f));
		p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, p.getLocation(), 1000);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getHat() {
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemMeta helmetmeta = helmet.getItemMeta();
		helmetmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		helmet.setItemMeta(helmetmeta);
		return helmet;
	}

}
