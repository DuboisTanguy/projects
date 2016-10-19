package com.wearedevelopers.rush.misc;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerKilledlistener implements Listener{

	@SuppressWarnings("rawtypes")
	@EventHandler
	public void onEntityDamaged(EntityDamageEvent e){
		if(e.getEntity() instanceof Player && Main.getPartyForWorld(e.getEntity().getWorld().getName())!=null){
			Player p = (Player) e.getEntity();
			if(p.getAllowFlight() && p.getGameMode()==GameMode.SURVIVAL) e.setCancelled(true);
			Class player = Player.class;
			Method m = null;
			for(Method me : player.getMethods()){
				if(me.getName().equals("getHealth")&&me.getReturnType().equals(double.class)){
					m = me;
				}
			}
			try {
				double d = (double) m.invoke(p, null);
				if(d-e.getFinalDamage()<=0){
					e.setDamage(0.0);
					p.setHealth((double)20.0);
					p.getInventory().clear();
					p.updateInventory();
					if(p.getBedSpawnLocation()!=null){
						FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getPlayersFileLoc()));
						DeathCountdown dc = new DeathCountdown(p, (fc.getStringList("VIP")!=null&&fc.getStringList("VIP").contains(
								p.getName()))?2:5);
						dc.runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Rush"));
					}
					else{
						p.getInventory().setArmorContents(null);
						p.setAllowFlight(true);
						p.teleport(p.getLocation().clone().add(new Location(p.getWorld(),0,2,0)));
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,1));
						p.setFlying(true);
					}
				}
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
}
