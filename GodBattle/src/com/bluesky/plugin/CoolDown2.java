package com.bluesky.plugin;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.bluesky.godsdata.God;

public class CoolDown2 {
	
	public Plugin p;
	 
    public CoolDown2(Plugin plugin) {
        p = plugin;
    }
   
    int task;
   
    public void setCooldownLength(Player player, int time, HashMap<String, Integer> hashmap) {
        hashmap.put(player.getName(), time);
    }
    
    public void setCooldownLength(World w, int time, HashMap<World, Integer> hashmap) {
        hashmap.put(w, time);
    }
   
    public int getTimeLeft(Player player, HashMap<String, Integer> hashmap) {
        int time = hashmap.get(player.getName());
        return time;
    }
	
	@SuppressWarnings("deprecation")
	public void startCooldown(final Player player, final HashMap<String, Integer> hashmap , Block[] bs , Block b) {
        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
            public void run() {
                int time = hashmap.get(player.getName());
                if(time != 0) {
                    hashmap.put(player.getName(), time - 1);
                } else {
                    hashmap.remove(player.getName());
                    b.setType(Material.AIR);
                    for(Block b2 : bs){
            			b2.setType(Material.AIR);
            		}
                    Bukkit.getServer().getScheduler().cancelTask(task);
                }
            }
        }, 0L, 20L);
    }
	@SuppressWarnings("deprecation")
	 public void startCooldownRage(final Player player, final HashMap<String, Integer> hashmap) {
	        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            public void run() {
	                int time = hashmap.get(player.getName());
	                if(time != 0) {
	                    hashmap.put(player.getName(), time - 1);
	                } else {
	                    hashmap.remove(player.getName());
	                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	              player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
	                    player.removePotionEffect(PotionEffectType.SLOW);
	                    PotionEffect pe = new PotionEffect(PotionEffectType.INCREASE_DAMAGE ,  Integer.MAX_VALUE , 0 , true);
	              player.addPotionEffect(pe);
	              PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
	              player.addPotionEffect(pe2);
	              PotionEffect pe3 = new PotionEffect(PotionEffectType.CONFUSION ,  5*20 , 5 , true);
	              player.addPotionEffect(pe3);
	                    Bukkit.getServer().getScheduler().cancelTask(task);
	                }
	            }
	        }, 0L, 20L);
	        
	    }
	@SuppressWarnings("deprecation")
	public void startInvulnerable(final Player player, final HashMap<String, Integer> hashmap){
		  task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            public void run() {
	                int time = hashmap.get(player.getName());
	                if(time != 0) {
	                    hashmap.put(player.getName(), time - 1);
	                } else {
	                    hashmap.remove(player.getName());
	                    player.setInvulnerable(false);
	                    Bukkit.getServer().getScheduler().cancelTask(task);
	                }
	            }
	        }, 0L, 20L);   
	}
	@SuppressWarnings("deprecation")
	 public void startCooldownInv(final Player player, final HashMap<String, Integer> hashmap) {
	        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            public void run() {
	                int time = hashmap.get(player.getName());
	                if(time != 0) {
	                    hashmap.put(player.getName(), time - 1);
	                } else {
	                    hashmap.remove(player.getName());
	                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
	                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
	                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	              PotionEffect pe2 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE ,  Integer.MAX_VALUE , 0 , true);
	              player.addPotionEffect(pe2);
	              player.getInventory().setArmorContents(new ItemStack[]{ new ItemStack(Material.LEATHER_BOOTS) ,
	                      new ItemStack(Material.DIAMOND_LEGGINGS) ,
	                      new ItemStack(Material.LEATHER_CHESTPLATE) ,
	                      new ItemStack(Material.DIAMOND_HELMET) });
	                    Bukkit.getServer().getScheduler().cancelTask(task);
	                }
	            }
	        }, 0L, 20L);
	    }
	@SuppressWarnings("deprecation")
	public void changeBoots(final Player player, final HashMap<String, Integer> hashmap){
		  task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            public void run() {
	                int time = hashmap.get(player.getName());
	                if(time != 0) {
	                    hashmap.put(player.getName(), time - 1);
	                } else {
	                    hashmap.remove(player.getName());
	                    ItemStack item = new ItemStack(Material.IRON_BOOTS);
	            		ItemMeta meta = item.getItemMeta();
	            		meta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
	            		item.setItemMeta(meta);
	            		player.getInventory().setBoots(item);
	                    Bukkit.getServer().getScheduler().cancelTask(task);
	                }
	            }
	        }, 0L, 20L);   
	}
	@SuppressWarnings("deprecation")
	 public void startCooldownGlow(final Player player, final HashMap<String, Integer> hashmap , List<Entity> l) {
	        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            public void run() {
	                int time = hashmap.get(player.getName());
	                if(time != 0) {
	                    hashmap.put(player.getName(), time - 1);
	                } else {
	                    hashmap.remove(player.getName());
	                    player.removePotionEffect(PotionEffectType.SPEED);
	              PotionEffect pe2 = new PotionEffect(PotionEffectType.SPEED ,  Integer.MAX_VALUE , 1 , true);
	              player.addPotionEffect(pe2);
	              for(Entity e : l){
	               e.setGlowing(false);
	              }
	                    Bukkit.getServer().getScheduler().cancelTask(task);
	                }
	            }
	        }, 0L, 20L);
	    }
	 @SuppressWarnings("deprecation")
	public void startgetHat(final Player player, final HashMap<String, Integer> hashmap , List<Entity> l) {
	        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            public void run() {
	                int time = hashmap.get(player.getName());
	                if(time != 0) {
	                    hashmap.put(player.getName(), time - 1);
	                } else {
	                    hashmap.remove(player.getName());
	              for(Entity e : l){
	               if (e instanceof Player){
	            	   MainClass mc= new MainClass();
	            	   God g = (God) mc.getGod((Player)e);
	            	   ItemStack itemhelmet = g.getHat();
	            	   ((Player) e).getInventory().setHelmet(itemhelmet);
	               }
	              }
	                    Bukkit.getServer().getScheduler().cancelTask(task);
	                }
	            }
	        }, 0L, 20L);
	    }
	 @SuppressWarnings("deprecation")
	 public void startCooldownExplode(final Player player, final HashMap<String, Integer> hashmap , Entity e , int i) {
	        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            public void run() {
	                int time = hashmap.get(player.getName());
	                if(time != 0) {
	                    hashmap.put(player.getName(), time - 1);
	                    e.getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getLocation(), 250);
	                    if(e.getType()==EntityType.ARMOR_STAND){
	                     for(Entity e2 : e.getNearbyEntities(5, 5, 5)){
	                   if(e2 instanceof Player){
	                    Player p2 = (Player) e2;
	                    PotionEffect pe = new PotionEffect(PotionEffectType.SLOW ,  40 , 3 , true);
	                    p2.addPotionEffect(pe);
	                   }
	                  }
	                    }
	                } else {
	                    hashmap.remove(player.getName());
	                    e.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, e.getLocation(), 600);
	                    e.remove();
	                    e.getWorld().createExplosion(e.getLocation(), i);
	                    Bukkit.getServer().getScheduler().cancelTask(task);
	                }
	            }
	        }, 0L, 20L);
	    }
	 
	 @SuppressWarnings("deprecation")
	public void tpToSpawn(final Player player, final HashMap<String, Integer> hashmap , Location l) {
	        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
	            public void run() {
	                int time = hashmap.get(player.getName());
	                if(time != 0) {
	                    hashmap.put(player.getName(), time - 1);
	                }
	                else{
	                    hashmap.remove(player.getName());
	                    World w = player.getWorld();
	                    player.teleport(l);
	                    w.getEntities().clear();
	                    Sign s = MainClass.getSignForWorld(w);
	                    s.setLine(1, ChatColor.GREEN+"[JOIN]");
	                    s.update();
	                    player.setGameMode(GameMode.SURVIVAL);
	                    Bukkit.getServer().getScheduler().cancelTask(task);
	                }
	            }
	        }, 0L, 20L);
	    }
	 
	 @SuppressWarnings("deprecation")
		public void wait(World w , HashMap<World, Integer> hm) {
		        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
		            public void run() {
		                int time = hm.get(w);
		                if(time != 0) {
		                	if(w.getPlayers().size()<=1){
		                		for(Player p : w.getPlayers()){
		                			p.sendTitle(ChatColor.RED+"Loading cancelled", "Need more players !");
		                		}
		                		Bukkit.getServer().getScheduler().cancelTask(task);
		                	}
		                	else{
		                		hm.put(w, time - 1);
		                		for(Player p : w.getPlayers()){
		                			p.setLevel(time-1);
		                			p.sendTitle(time-1+" seconds remaining", null);
		                		}
		                    }
		                }
		                else{
		                	MainClass.getSignForWorld(w).setLine(1, ChatColor.RED+"[IN GAME]");
		                    hm.remove(w);
		                    Bukkit.getServer().getScheduler().cancelTask(task);
		                    Party party = new Party();
                        	party.setPlayers(w.getPlayers());
                        	party.createTeams();
                        	party.start(w);
		                }
		            }
		        }, 0L, 20L);
		    }
	 @SuppressWarnings("deprecation")
		public void PlayerJump(final Player player, final HashMap<String, Integer> hashmap, List<Player> l) {
		        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask( Bukkit.getServer().getPluginManager().getPlugin("GodBattle"), new BukkitRunnable() {
		            public void run() {
		                int time = hashmap.get(player.getName());
		                if(time != 0) {
		                    hashmap.put(player.getName(), time - 1);
		                } else {
		                    hashmap.remove(player.getName());
		                    l.remove(player);
		                    Bukkit.getServer().getScheduler().cancelTask(task);
		                }
		            }
		        }, 0L, 20L);
		    }
	 
}
