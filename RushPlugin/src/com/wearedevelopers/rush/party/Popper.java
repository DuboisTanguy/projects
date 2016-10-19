package com.wearedevelopers.rush.party;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.wearedevelopers.rush.misc.Main;

public class Popper extends BukkitRunnable {
	private int itemToPop = 0;
	private World w;
	private boolean canPop = false;
	private Material mBronze = Material.CLAY_BRICK;
	private Material mIron = Material.IRON_INGOT;
	private Material mGold = Material.GOLD_INGOT;
	private Material mDiamond = Material.DIAMOND;
	private List<String> poppers;
	private FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getpopFileLoc()));

	@SuppressWarnings("deprecation")
	public void Pop(World w) {
		this.canPop = true;
		this.w = w;
		this.getPoppers();
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Rush"),
				this, 0L, 10L);
	}

	public void dePop() {
		this.canPop = false;
	}

	@SuppressWarnings("unchecked")
	private void getPoppers() {
		this.poppers = (List<String>) fc.getList("poppers");
	}

	@Override
	public void run() {
		if (this.canPop) {
			switch (this.itemToPop) {
			case 1:
			case 2:
			case 4:
			case 6:
				this.appear(mBronze);
				this.itemToPop++;
				break;
			case 3:
			case 5:
			case 8:
				this.appear(mIron);
				this.itemToPop++;
				break;
			case 7:
			case 9:
				this.appear(mGold);
				this.itemToPop++;
				break;
			case 10:
				this.appear(mDiamond);
				this.itemToPop = new Integer(0);
				break;
			}
		}

	}
	
	public void appear(Material m){
		for (int i = 0; i < poppers.size(); i++) {
			if(poppers.get(i).contains(this.w.getName())){
				String[] coords = poppers.get(i).toString().split(":");
				Location l = new Location(this.w, Double.valueOf(coords[0]), Double.valueOf(coords[1]),
						Double.valueOf(coords[3]));
				l.getWorld().dropItem(l, new ItemStack(m));
			}
		}
	}
}