package com.bluesky.plugin;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.bluesky.objects.Healer;

public class Party {
	
	private Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
	private Team b = board.registerNewTeam("Blue");
	private Team r = board.registerNewTeam("Red");
	private PartyGameMode pgm;
	
	private List<Player> players ;
	
	public Party(){
		r.setPrefix(ChatColor.RED+"{RED} ");
		b.setPrefix(ChatColor.BLUE+"[BLUE] ");
		r.setAllowFriendlyFire(false);
		b.setAllowFriendlyFire(false);
		r.setCanSeeFriendlyInvisibles(true);
		b.setCanSeeFriendlyInvisibles(true);
	}
	
	public PartyGameMode getGameMode(){
		return this.pgm;
	}
	
	public void setPlayers(List<Player> list){
		this.players = list;
	}
	
	public void createTeams(){
		int i = 0;
		for(Player p : players){
			if(i==0){
				r.addEntry(p.getName());
				i++;
			}
			else{
				b.addEntry(p.getName());
				i--;
			}
			p.setScoreboard(board);
		}
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void start(World w){
		Sign s = MainClass.getSignForWorld(w);
		s.setLine(0, w.getName());
		s.setLine(1, ChatColor.RED+"[IN GAME]");
		s.update();
		FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(MainClass.getMapFileConf()));
		if(fg.get(w.getName()+".gamemode")!=null){
			this.pgm = PartyGameMode.fromString( fg.getString(w.getName()+".gamemode") );
		}
		else{
			Bukkit.getLogger().warning("unable to find gamemode for "+w.getName());
		}
		for(Player p : players){
			if(r.getEntries().contains(p.getName())){
				p.teleport((Location) (fg.get(w.getName()+".redTeamSpawn")) );
			}
			else if(b.getEntries().contains(p.getName())){
				p.teleport((Location) (fg.get(w.getName()+".blueTeamSpawn")) );
			}
			p.openInventory(MainClass.getInventory());
			p.setGameMode(GameMode.SURVIVAL);
		}
		if(fg.get(w.getName()+".healers")!=null){
			List<Location> ll = (List<Location>) (fg.getList(w.getName()+".healers"));
			for(Location lo : ll){
				Healer h = new Healer();
				h.create(lo);
			}
		}
		if(this.pgm == PartyGameMode.TDM){
			for(Player p : players){
				p.sendTitle(ChatColor.RED+this.pgm.toString(), "Kill all the enemies !");
			}
		}
	}
}
