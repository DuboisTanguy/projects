package com.wearedevelopers.rush.party;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.wearedevelopers.rush.misc.Main;
import com.wearedevelopers.rush.misc.SignRefresher;

public class Party {

	private PartyType type;
	private World world;
	private Sign sign;
	private boolean inGame;
	private Scoreboard sc;
	private List<Block> placedBlocks = new LinkedList<Block>();
	private HashMap<Location, Inventory> locinv = new HashMap<Location, Inventory>();
	private World lobby;
	
	public Party(PartyType pt ,World w ,Sign s, Scoreboard sc, World lobby){
		FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getWorldsFileLoc()));
		this.type = pt;
		this.world = w;
		this.sign = s;
		this.sc = sc;
		this.lobby=lobby;
		if(fc.get(w.getName())!=null){
			Set<Player> unteamed = new HashSet<Player>();
			for(Player p : lobby.getPlayers()){
				p.getInventory().clear();
				p.updateInventory();
				if(isTeamed(p)) unteamed.add(p);
			}
			for(Player p : unteamed){
				sc.getTeam(this.getTeamAvecLeMoinsDeGensDedans()).addEntry(p.getName());
				p.setScoreboard(sc);
			}
			Color[] cs = {Color.BLUE , Color.RED , Color.PURPLE , Color.YELLOW};
			int i = 0;
			for(org.bukkit.scoreboard.Team t : sc.getTeams()){
				for(String str : t.getEntries()){
					Player p = Bukkit.getPlayer(str);
					giveArmor(p,cs[i],t);
				}
				i++;
			}
			this.dispatchPlayers();
			sc.registerNewObjective("Dead", "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
			Objective o = sc.getObjective("Dead");
			o.setDisplayName("TEAMS");
			PartyRefresher pf = new PartyRefresher(this);
			pf.start();
			
		}
		
	}

	public Scoreboard getSc() {
		return sc;
	}

	public void setSc(Scoreboard sc) {
		this.sc = sc;
	}

	public PartyType getType() {
		return type;
	}

	public void setType(PartyType type) {
		this.type = type;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public boolean isTeamed(Player p){
		int i = 0;
		for(org.bukkit.scoreboard.Team t : sc.getTeams()){
			for(String str : t.getEntries()){
				if(p.getName().equals(str)) i++;
			}
		}
		return (i==0)? true : false;
	}
	
	public String getTeamAvecLeMoinsDeGensDedans(){
		String str = "";
		for(org.bukkit.scoreboard.Team t : sc.getTeams()){
			str = (!str.equals(""))? ((t.getEntries().size()>sc.getTeam(str).getEntries().size())? t.getName() : str ) : t.getName();
		}
		return str;
	}
	
	public void dispatchPlayers(){
		FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getWorldsFileLoc()));
		int i = 1;
		for(org.bukkit.scoreboard.Team t : sc.getTeams()){
			for(String str : t.getEntries()){
				String s = world.getName()+"."+i+".spawn";
				Location l = new Location(
						world,
						fc.getDouble(s+".X"),
						fc.getDouble(s+".Y"),
						fc.getDouble(s+".Z"));
				Bukkit.getPlayer(str).setBedSpawnLocation(l);
				Bukkit.getPlayer(str).teleport(l.clone().add(0.0, 1.0, 0.0));
				i++;
			}
		}
	}
	
	public void end(org.bukkit.scoreboard.Team t){
		FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(Main.getPointsFileLoc()));
		FileConfiguration fc2 = YamlConfiguration.loadConfiguration(new File(Main.getPlayersFileLoc()));
		FileConfiguration fc3 = YamlConfiguration.loadConfiguration(new File("plugins/Rush/config.yml"));
		for(String s : t.getEntries()){
			Player p = Bukkit.getPlayer(s);
			int points = (fc.get(p.getName())==null)?0:fc.getInt(p.getName());
			points +=  fc2.getStringList("VIPplus").contains(s)?15:( (fc2.getStringList("VIP").contains(s))?10:5 );
			points += (p.hasPotionEffect(PotionEffectType.INVISIBILITY))?0:5;
			fc.set(p.getName(), points);
			try {
				fc.save(new File(Main.getPointsFileLoc()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(Player p : world.getPlayers()){
			p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			p.setFlying(false);
			p.setAllowFlight(false);
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			p.setLevel(0);
			Location l = new Location(Bukkit.createWorld(new WorldCreator(fc3.getString("spawn.world"))),
					fc3.getDouble("spawn.X"),
					fc3.getDouble("spawn.Y"),
					fc3.getDouble("spawn.Z"));
			p.teleport(l);
		}
		for(Block b : this.placedBlocks){
			if(b!=null){
				b.setType(Material.AIR);
			}
		}
		for(Entity e : world.getEntities()){
			e.remove();
		}
		SignRefresher sr = new SignRefresher(sign.getLocation(),lobby,type);
		sr.start();
		Main.removeParty(world.getName());
	}

	public List<Block> getPlacedBlocks() {
		return placedBlocks;
	}

	public void addPlacedBlock(Block b) {
		this.placedBlocks.add(b);
	}
	
	public static void giveArmor(Player p, Color c, org.bukkit.scoreboard.Team t){
		ItemStack[] iss = {new ItemStack(Material.LEATHER_BOOTS),new ItemStack(Material.LEATHER_LEGGINGS),
				           new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)};
		for(int i = 0 ; i<iss.length ; i++){
			ItemStack is = iss[i];
			LeatherArmorMeta lam = (LeatherArmorMeta) is.getItemMeta();
			lam.setColor(c);
			lam.setDisplayName(t.getPrefix());
			is.setItemMeta(lam);
			iss[i] = is;
		}
		p.getInventory().setArmorContents(iss);
	}

	public Inventory getInvForLoc(Location l) {
		return locinv.get(l);
	}

	public void addInv (Location l,Inventory i) {
		this.locinv.put(l, i);
	}
	
}
