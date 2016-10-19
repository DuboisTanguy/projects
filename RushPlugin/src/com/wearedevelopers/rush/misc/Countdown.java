package com.wearedevelopers.rush.misc;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable{
	
	private int i = 10;
	private World w;
	private boolean finished = false;
	private boolean running = false;
	
	public Countdown(int a,World wo){
		this.i=a;
		this.w=wo;
	}

	@Override
	public void run() {
		for(int b = i ; b>=0 ; b--){
			for(Player p : w.getPlayers()){
				p.setLevel(b);
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 20, 20);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!running){
				break;
			}
		}
		if(running){
			finished = true;
		}
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
