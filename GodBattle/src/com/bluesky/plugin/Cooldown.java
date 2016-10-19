package com.bluesky.plugin;
 
import java.util.HashMap;
 
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
 
public class Cooldown {
 
    // change Main to your plugin's main class
    public MainClass p;
 
    public Cooldown(MainClass i) {
        p = i;
    }
   
    int task;
   
    public void setCooldownLength(Player player, int time, HashMap<String, Integer> hashmap) {
        hashmap.put(player.getName(), time);
    }
   
    public int getTimeLeft(Player player, HashMap<String, Integer> hashmap) {
        int time = hashmap.get(player.getName());
        return time;
    }
   
    @SuppressWarnings("deprecation")
	public void startCooldown(final Player player, final HashMap<String, Integer> hashmap , ItemStack is , int i) {
        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, new BukkitRunnable() {
            public void run() {
                int time = hashmap.get(player.getName());
                if(time != 0) {
                    hashmap.put(player.getName(), time - 1);
                } else {
                    hashmap.remove(player.getName());
                    player.getInventory().setItem(i, is);
                    Bukkit.getServer().getScheduler().cancelTask(task);
                }
            }
        }, 0L, 20L);
    }
    
}