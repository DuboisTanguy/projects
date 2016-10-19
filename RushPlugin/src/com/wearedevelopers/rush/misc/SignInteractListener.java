package com.wearedevelopers.rush.misc;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;

import com.wearedevelopers.rush.party.PartyType;
import com.wearedevelopers.rush.party.Team;

public class SignInteractListener implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getClickedBlock()!=null){
			Material m = event.getClickedBlock().getType();
			Player p = event.getPlayer();
			if(event.getAction()==Action.RIGHT_CLICK_BLOCK){
				if(m==Material.SIGN||m==Material.SIGN_POST||m==Material.WALL_SIGN){
					Sign sign = (Sign)event.getClickedBlock().getState();
					if(Main.getWorldForSign(sign)!=null){
						World w = Bukkit.createWorld(new WorldCreator(Main.getWorldForSign(sign)));
						FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getSwFileLoc()));
						PartyType pt = PartyType.fromString(fc.getString("signs."+w.getName()+".partyType"));
						int maxPlayers = pt.getTeams()*pt.getTeamSize();
						if(!(w.getPlayers().size()>=maxPlayers)&&sign.getLine(1).equals(ChatColor.GREEN+"[REJOINDRE]")){
							ItemStack[] wools = {
								new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getData()),
								new ItemStack(Material.WOOL, 1, DyeColor.RED.getData()),
								new ItemStack(Material.WOOL, 1, DyeColor.PURPLE.getData()),
								new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData())
							};
							p.getInventory().clear();
							for(int i = 0 ; i<pt.getTeams() ; i++){
								ItemMeta im = wools[i].getItemMeta();
								Bukkit.getLogger().info(Team.values()[i].toString());
								im.setDisplayName(Team.values()[i].toString());
								wools[i].setItemMeta(im);
								Main.giveItem(wools[i], p);
							}
							Location l = Main.desyntLoc(fc.getString("signs."+w.getName()+".lobbyspawn"));
							p.teleport(l);
							Scoreboard sc = (Main.getScForWorld(l.getWorld().getName())!=null)?Main.getScForWorld(l.getWorld().getName()):Main.addScoreboard(l.getWorld().getName(),pt);
							p.setScoreboard(sc);
						}
					}
				}
			}
		}
	}
	
}
