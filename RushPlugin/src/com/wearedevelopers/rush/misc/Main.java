package com.wearedevelopers.rush.misc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import com.wearedevelopers.rush.party.Party;
import com.wearedevelopers.rush.party.PartyType;
import com.wearedevelopers.rush.party.Team;

public class Main extends JavaPlugin{
	
	private PluginDescriptionFile pdf;
	private static HashMap<String,Sign> hm;
	private static HashMap<Sign,String> hm2;
	private static HashMap<String,Scoreboard> ws = new HashMap<String,Scoreboard>();
	private static HashMap<String,Party> hmParty = new HashMap<String, Party>();
	private static String swFileLoc = "plugins/Rush/signsAndWorlds.yml";
	private static String popFileLoc = "plugins/Rush/poppersAndWorlds.yml";
	private static String worldsFileLoc = "plugins/Rush/worlds.yml";
	private static String playersFileLoc = "plugins/Rush/players.yml";
	private static String pointsFileLoc = "plugins/Rush/points.yml";
	private static boolean running;
	
	@Override
	public void onEnable(){
		running = true;
		pdf = this.getDescription();
		this.getLogger().info(pdf.getName()+" "+pdf.getVersion()+" starting...");
		getServer().getPluginManager().registerEvents(new SignInteractListener(), this);
		getServer().getPluginManager().registerEvents(new SignChangeListener(), this);
		getServer().getPluginManager().registerEvents(new SignBreakListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerUseItemListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerBreakListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerKilledlistener(), this);
		getServer().getPluginManager().registerEvents(new CraftItemListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		getServer().getPluginManager().registerEvents(new PlaceblockListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
		getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
		CommandExecutor rushExec = new Commands();
		getCommand("rush").setExecutor(rushExec);
		this.saveDefaultConfig();
		initHm();
		File f = new File(swFileLoc);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File f2 = new File(worldsFileLoc);
		if(!f2.exists()){
			try {
				f2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File f3 = new File(playersFileLoc);
		if(!f3.exists()){
			try {
				f3.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File f4 = new File(popFileLoc);
		if(!f4.exists()){
			try {
				f4.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File f5 = new File(pointsFileLoc);
		if(!f5.exists()){
			try {
				f5.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onDisable(){
		running = false;
		this.getLogger().info(pdf.getName()+" "+pdf.getVersion()+" stopping...");
		this.saveConfig();
	}
	
	@SuppressWarnings("unchecked")
	public static void initHm(){
		hm = new HashMap<String,Sign>();
		hm2 = new HashMap<Sign,String>();
		FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(swFileLoc));
		if(fc.get("signs")!=null && fc.get("worlds")!=null){
			List<String> worlds = (List<String>) fc.getList("worlds");
			for(int i = 0 ; i<worlds.size() ; i++){
				if(fc.get("signs."+worlds.get(i))!=null){
					String w = worlds.get(i);
					String s = "signs."+w;
					Location l = new Location(
							Bukkit.createWorld(new WorldCreator(fc.getString(s+".world"))),
							fc.getDouble(s+".X"),
							fc.getDouble(s+".Y"),
							fc.getDouble(s+".Z"));
					Sign sign = (Sign) l.getBlock().getState();
					hm.put(w, sign);
					hm2.put(sign, w);
					World world = Bukkit.createWorld(new WorldCreator(w));
					SignRefresher sr = new SignRefresher(sign.getLocation(),world,PartyType.fromString(fc.getString(s+".partyType")));
					sr.start();
				}
			}
		}
	}
	
	public static void createParty(Sign s, String w){
		FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(swFileLoc));
		World w2 = Bukkit.createWorld(new WorldCreator(fc.getString("signs." + w+".to")));
		Party party = new Party(PartyType.fromString(fc.getString("signs." + w+".partyType")),
				w2,
				s,
				getScForWorld(w),
				Bukkit.createWorld(new WorldCreator(w)));
		hmParty.put(w2.getName(), party);
	}
	
	
	
	public static Sign getSignForWorld(String w){
		return hm.get(w);
	}
	
	public static String getWorldForSign(Sign s){
		return hm2.get(s);
	}
	
	public static String getSwFileLoc(){
		return swFileLoc;
	}
	
	public static String getWorldsFileLoc(){
		return worldsFileLoc;
	}

	public static boolean isRunning() {
		return running;
	}
	
	public static String getpopFileLoc(){
		return popFileLoc;
	}
	
	public static void giveItem(ItemStack is, Player p){
		p.getInventory().addItem(is);
		p.updateInventory();
	}
	
	public static Scoreboard addScoreboard(String s, PartyType pt){
		ws.put(s, Bukkit.getScoreboardManager().getNewScoreboard());
		for(int i = 0 ; i<pt.getTeams() ; i++)
			ws.get(s).registerNewTeam(Team.values()[i].toString()).setPrefix(Team.values()[i].toString()+" ");
		return ws.get(s);
	}
	
	public static Scoreboard getScForWorld(String w){
		return ws.get(w);
	}
	
	public static Party getPartyForWorld(String s){
		return hmParty.get(s);
	}
	
	public static void removeParty(String s){
		Party p = hmParty.get(s);
		hmParty.remove(s);
		p = null;
	}

	public static String getPlayersFileLoc() {
		return playersFileLoc;
	}

	public static String getPointsFileLoc() {
		return pointsFileLoc;
	}
	
	public static Location desyntLoc(String s){
		String[] str = s.split(":");
		Location l = new Location(
				Bukkit.createWorld(new WorldCreator(str[0])),
				Double.parseDouble(str[1]),
				Double.parseDouble(str[2]),
				Double.parseDouble(str[3])
				);
		return l;
	}
	 
}
