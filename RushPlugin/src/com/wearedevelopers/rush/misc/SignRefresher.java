package com.wearedevelopers.rush.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import com.wearedevelopers.rush.party.PartyType;

public class SignRefresher {

	private Sign sign;
	private World world;
	private PartyType pt;
	private boolean running = false;
	private Countdown c;
	private int task;
	private int lastP = 0;

	public SignRefresher(Location l, World w, PartyType pt) {
		this.sign = (Sign) l.getBlock().getState();
		this.world = w;
		this.pt = pt;
		c = new Countdown(10, w);
		sign.setLine(0, ChatColor.AQUA+"Rush "+pt.toString());
		sign.setLine(1, ChatColor.GREEN+"[REJOINDRE]");
		sign.setLine(2, ChatColor.DARK_BLUE+"En attente");
		sign.setLine(3, ChatColor.WHITE+"0/"+(pt.getTeams()*pt.getTeamSize()));
		sign.update();
	}

	@SuppressWarnings("deprecation")
	public void start() {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Rush"),
				new BukkitRunnable(){

					@Override
					public void run() {
						if(Main.isRunning()){
							int maxPlayers = pt.getTeams()*pt.getTeamSize();
							int players = world.getPlayers().size();
							sign.setLine(3, ChatColor.WHITE+""+players+"/"+(pt.getTeams()*pt.getTeamSize()));
							sign.setLine(1, (players==maxPlayers)?ChatColor.RED+"[COMPLET]":ChatColor.GREEN+"[REJOINDRE]");
							sign.update();
							if(players>=pt.getTeams() || players==maxPlayers){
								if(lastP!=players){
									lastP=players;
									c.setRunning(false);
								}
								if(!c.isRunning()){
									c = new Countdown( (players==maxPlayers)?10:250-(15*players)  ,world);
									c.setRunning(true);
									c.runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Rush"));
								}
								if(c.isFinished()){
									c.cancel();
									Bukkit.getScheduler().cancelTask(task);
									Main.createParty(sign, world.getName());
								}
							}
							else{
								if(c.isRunning()){
									c.setRunning(false);
									c.cancel();
								}
							}
						}
					}
					
			},0L,1L);

	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
