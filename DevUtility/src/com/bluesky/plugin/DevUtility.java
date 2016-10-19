package com.bluesky.plugin;


import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DevUtility extends JavaPlugin implements Listener{
	
	private static Inventory mainInv = mainCreate();
	private static ItemStack devItem = itemCreate();
	private static String invFileLocation = new String("plugins/DevUtility/Invs.yml");
	
	@Override
	public void onEnable(){
		saveDefaultConfig();
		PluginDescriptionFile pdf = getDescription();
		getLogger().info(pdf.getName()+" "+pdf.getVersion()+" by "+pdf.getAuthors()+" enabled");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		
		List<String> list =new LinkedList<String>();
		list = getConfig().getStringList("disabledPlugins");
		for(String str: list){
			pm.disablePlugin(pm.getPlugin(str));
		}
		
		File invFile = new File(invFileLocation);
		if(!invFile.exists()){
			try {
				invFile.createNewFile();
			} catch (IOException e) {
				getLogger().info("Unable to create "+invFile.getName());
			}
		}
	}
	
	@Override
	public void onDisable(){
		PluginDescriptionFile pdf = getDescription();
		getLogger().info(pdf.getName()+" "+pdf.getVersion()+" by "+pdf.getAuthors()+" disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender , Command cmd , String label , String[] args){
		if(label.equalsIgnoreCase("devutil")){
			if(sender!=getServer().getConsoleSender()){
				Player p = (Player) sender;
				p.getInventory().addItem(devItem);
				p.sendMessage(ChatColor.GREEN+"Dev item gived !");
			}
			else{
				getLogger().info(ChatColor.RED+"You can't execute this command from the console !");
			}
			return true;
		}
		else if(label.equalsIgnoreCase("multiinv")){
			if(sender!=getServer().getConsoleSender()){
				Player p = (Player) sender;
				p.openInventory(multiInvCreate());
			}
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if(e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(e.getPlayer().getInventory().getItemInMainHand().equals(devItem)){
				e.getPlayer().openInventory(mainInv);
				e.setCancelled(true);
			}
		}
		else if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
			if(e.getPlayer().getInventory().getItemInMainHand().getType()==Material.BOOK_AND_QUILL || e.getPlayer().getInventory().getItemInMainHand().getType()==Material.WRITTEN_BOOK){
				if(e.getItem().getItemMeta().getLore() != null){
					if(e.getItem().getItemMeta().getLore().size()==4  ){
						if(e.getItem().getItemMeta().getLore().get(0).contains(".yml")){
							FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(e.getItem().getItemMeta().getLore().get(0)));
							List<String> l = ( (BookMeta) (e.getItem().getItemMeta()) ).getPages();
							String st = new String();
							for(String s : l){
								st = st +s;
							}
							try{
								fg.set(e.getItem().getItemMeta().getLore().get(1), Integer.parseInt(st));
							}catch(NumberFormatException n){
								try{
									fg.set(e.getItem().getItemMeta().getLore().get(1), Float.parseFloat(st));
								}catch(NumberFormatException n2){
									if(st.equals("true") || st.equals("false"))
										fg.set(e.getItem().getItemMeta().getLore().get(1), Boolean.parseBoolean(st));
									else
										fg.set(e.getItem().getItemMeta().getLore().get(1), st);
								}
							}
							try {
								fg.save(new File(e.getItem().getItemMeta().getLore().get(0)));
							} catch (IOException e1) {
								getLogger().info("unable to save "+(new File(e.getItem().getItemMeta().getLore().get(0))).getName());
							}
							e.getPlayer().getInventory().remove(e.getItem());
							e.getPlayer().sendMessage(ChatColor.GREEN+(new File(e.getItem().getItemMeta().getLore().get(0))).getName()+" succefully changed !");
							File fi = new File(e.getItem().getItemMeta().getLore().get(0));
							do{
								fi = fi.getParentFile();
							}while(!fi.getParent().equals("plugins"));
							Plugin pl = getServer().getPluginManager().getPlugin(fi.getName());
							pl.reloadConfig();
							e.getPlayer().sendMessage(ChatColor.GREEN+fi.getName()+" reloaded");
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e){
		if(e.getCurrentItem()!=null){
			if(e.getInventory().equals(mainInv)){
				if(e.getCurrentItem().isSimilar(mainInv.getItem(2))){
					e.getWhoClicked().openInventory(reloadCreate("Reload"));					
				}
				else if(e.getCurrentItem().isSimilar(mainInv.getItem(0))){
					e.getWhoClicked().openInventory(reloadCreate("Enable/Disable"));
				}
				else if(e.getCurrentItem().isSimilar(mainInv.getItem(1))){
					e.getWhoClicked().openInventory(cheatsCreate());
				}
				else if(e.getCurrentItem().isSimilar(mainInv.getItem(3))){
					e.getWhoClicked().openInventory(invsCreate());
				}
				else if(e.getCurrentItem().isSimilar(mainInv.getItem(4))){
					e.getWhoClicked().openInventory(reloadCreate("Config edit"));
				}
				else if(e.getCurrentItem().isSimilar(mainInv.getItem(5))){
					e.getWhoClicked().openInventory(multiInvCreate());
				}
				e.setCancelled(true);
			}
			else if(e.getInventory().getTitle().equals(ChatColor.DARK_BLUE+"Reload")){
				if(e.getCurrentItem().getType()!=Material.AIR){
					if(e.getCurrentItem().getType()==Material.NETHER_STAR && e.getCurrentItem().getItemMeta().hasLore()){
						Plugin pl = getServer().getPluginManager().getPlugin(e.getCurrentItem().getItemMeta().getDisplayName());
						getServer().getPluginManager().disablePlugin(pl);
						getServer().getPluginManager().enablePlugin(pl);
						pl.reloadConfig();
						e.getWhoClicked().sendMessage(ChatColor.GREEN+pl.getName()+" reloaded !");
					}
					else if(e.getCurrentItem().getType()==Material.EMERALD && e.getCurrentItem().getItemMeta().hasLore()){
						getServer().dispatchCommand(getServer().getConsoleSender(), "reload");
					}
					else{
						e.getWhoClicked().openInventory(mainInv);
					}
				}	
				e.setCancelled(true);
			}
			else if(e.getInventory().getTitle().equals(ChatColor.DARK_BLUE+"Enable/Disable")){
				if(e.getCurrentItem().getType()!=Material.AIR){
					if(e.getCurrentItem().getType()==Material.NETHER_STAR && e.getCurrentItem().getItemMeta().hasLore()){
						Plugin pl =getServer().getPluginManager().getPlugin(e.getCurrentItem().getItemMeta().getDisplayName());
						if(pl.isEnabled()){
							getServer().getPluginManager().disablePlugin(pl);
							List<String> list =new LinkedList<String>();
							list = getConfig().getStringList("disabledPlugins");
							list.add(pl.getName());
							getConfig().set("disabledPlugins", list);
							saveConfig();
							e.getWhoClicked().sendMessage(ChatColor.GREEN+pl.getName()+" disabled !");
						}
						else{
							getServer().getPluginManager().enablePlugin(pl);
							e.getWhoClicked().sendMessage(ChatColor.GREEN+pl.getName()+" enabled !");
							List<String> list =new LinkedList<String>();
							list = getConfig().getStringList("disabledPlugins");
							list.remove(pl.getName());
							getConfig().set("disabledPlugins", list);
							saveConfig();
						}
						e.getWhoClicked().openInventory(reloadCreate("Enable/Disable"));
					}
					else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED+"Back")){
						e.getWhoClicked().openInventory(mainInv);
					}			
				}
				e.setCancelled(true);
			}
			else if(e.getInventory().getTitle().equals(ChatColor.DARK_BLUE+"Cheats")){
				if(e.getCurrentItem()!=null){
					if(e.getCurrentItem().getType()!=Material.AIR){
						if(e.getCurrentItem().getItemMeta().hasDisplayName()){
							if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA+"Fly")){
								if(( (Player) (e.getWhoClicked()) ).getAllowFlight()){
									((Player)(e.getWhoClicked())).setAllowFlight(false);
									e.getWhoClicked().sendMessage(ChatColor.GREEN+"You can't fly anymore");
								}
								else{
									((Player)(e.getWhoClicked())).setAllowFlight(true);
									e.getWhoClicked().sendMessage(ChatColor.GREEN+"You can fly");
								}
							}
							else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA+"Switch Gamemode")){
								Player p =(Player)e.getWhoClicked();
								if(p.getGameMode()==GameMode.CREATIVE){
									p.setGameMode(GameMode.SURVIVAL);
									p.sendMessage(ChatColor.GREEN+"Switched to survival");
								}
								else{
									p.setGameMode(GameMode.CREATIVE);
									p.sendMessage(ChatColor.GREEN+"Switched to creative");
								}
							}
							else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA+"GodMode")){
								Player p =(Player)e.getWhoClicked();
								if(p.isInvulnerable()){
									p.setInvulnerable(false);
									p.sendMessage(ChatColor.GREEN+"GodMode disabled");
								}
								else{
									p.setInvulnerable(true);
									p.sendMessage(ChatColor.GREEN+"GodMode activated");
								}
							}
							else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED+"Back")){
								e.getWhoClicked().openInventory(mainInv);
							}
						}
					}	
				}
				e.setCancelled(true);
			}
			else if(e.getInventory().getTitle().equals(ChatColor.DARK_BLUE+"Players")){
				if(e.getCurrentItem()!=null ){
					if(e.getCurrentItem().getType()!=Material.AIR){
						if(e.getCurrentItem().getType()==Material.SKULL_ITEM){
							if(e.getCurrentItem().getItemMeta().hasLore()){
								if(e.getAction()==InventoryAction.PICKUP_ALL){
									e.getWhoClicked().teleport(Bukkit.getPlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getLore().get(3))).getLocation());
									e.getWhoClicked().sendMessage(ChatColor.GREEN+"Teleported to "+e.getCurrentItem().getItemMeta().getDisplayName());
								}
								else if(e.getAction()==InventoryAction.PICKUP_HALF){
									e.getWhoClicked().openInventory(Bukkit.getPlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getLore().get(3))).getInventory());
								}
								else if(e.getAction()==InventoryAction.CLONE_STACK){
									Bukkit.getPlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getLore().get(3))).kickPlayer(getConfig().getString("kickMsg"));
									e.getWhoClicked().openInventory(invsCreate());
								}
							}
						}
						else if(e.getCurrentItem().getType()==Material.BARRIER){
							e.getWhoClicked().openInventory(mainInv);
						}
					}	
				}
				e.setCancelled(true);
			}
			else if(e.getInventory().getTitle().equals(ChatColor.DARK_BLUE+"Config edit")){
				if(e.getCurrentItem()!=null){
					if(e.getCurrentItem().getType()==Material.NETHER_STAR && e.getCurrentItem().getItemMeta().hasLore()){
						Plugin pl =getServer().getPluginManager().getPlugin(e.getCurrentItem().getItemMeta().getDisplayName());
						e.getWhoClicked().openInventory(fileExplorerCreate(pl));
					}
					else if(e.getCurrentItem().getType()==Material.BARRIER){
						e.getWhoClicked().openInventory(mainInv);
					}
				}
				e.setCancelled(true);
			}
			else if(e.getInventory().getTitle().contains(ChatColor.DARK_BLUE+"Explorer -")){
				if(e.getCurrentItem()!=null){
					if(e.getCurrentItem().getType()==Material.BOOK && e.getCurrentItem().getItemMeta().hasLore()){
						File f = new File(e.getInventory().getTitle().substring(e.getInventory().getTitle().indexOf("-")+2, e.getInventory().getTitle().length())+"\\"+e.getCurrentItem().getItemMeta().getDisplayName());
						if(f.getPath().contains("/")){
							f = new File(f.getPath().replace("\\", "/"));
						}
						e.getWhoClicked().openInventory(fileExplorerCreate(f));
					}
					else if(e.getCurrentItem().getType()==Material.PAPER && e.getCurrentItem().getItemMeta().hasLore()){
						File f = new File(e.getInventory().getTitle().substring(e.getInventory().getTitle().indexOf("-")+2, e.getInventory().getTitle().length())+"\\"+e.getCurrentItem().getItemMeta().getDisplayName());
						if(f.getPath().contains("/")){
							f = new File(f.getPath().replace("\\", "/"));
						}
						if(f.getName().endsWith(".yml")){
							e.getWhoClicked().openInventory(configExplorer(YamlConfiguration.loadConfiguration(f) , f));
						}
					}
					else if(e.getCurrentItem().getType()==Material.BARRIER){
						e.getWhoClicked().openInventory(getParent(e.getInventory().getTitle().substring(e.getInventory().getTitle().indexOf("-")+2, e.getInventory().getTitle().length())));
					}
				}
				e.setCancelled(true);
			}
			else if(e.getInventory().getTitle().contains("plugins")&&e.getInventory().getTitle().contains(".yml")){
				if(e.getCurrentItem()!=null){
					if(e.getCurrentItem().getType()== Material.CHEST && e.getCurrentItem().getItemMeta().hasLore()){
						if(e.getInventory().getTitle().contains(",")){
							File f = new File(e.getInventory().getTitle().substring(2, e.getInventory().getTitle().lastIndexOf(",")-1));
							FileConfiguration fg = YamlConfiguration.loadConfiguration(f);
							e.getWhoClicked().openInventory(configExplorer(fg.getConfigurationSection(e.getInventory().getTitle().substring(e.getInventory().getTitle().lastIndexOf(",")+2)+"."+e.getCurrentItem().getItemMeta().getDisplayName()) , f));
						}
						else{
							File f = new File(e.getInventory().getTitle().substring(2, e.getInventory().getTitle().length()));
							FileConfiguration fg = YamlConfiguration.loadConfiguration(f);
							e.getWhoClicked().openInventory(configExplorer(fg.getConfigurationSection(e.getCurrentItem().getItemMeta().getDisplayName()) , f));
						}
					}
					else if(e.getCurrentItem().getType()== Material.TRIPWIRE_HOOK && e.getCurrentItem().getItemMeta().hasLore()){
						ItemStack is = new ItemStack(Material.BOOK_AND_QUILL);
						ItemMeta im = is.getItemMeta();
						BookMeta bm = (BookMeta) im;
						bm.setDisplayName(ChatColor.DARK_AQUA+"Config : "+e.getCurrentItem().getItemMeta().getDisplayName());
						List<String> list=new LinkedList<String>();
						if(e.getInventory().getTitle().contains(",")){
							list.add(e.getInventory().getTitle().substring(2, e.getInventory().getTitle().lastIndexOf(",")-1));
							list.add(e.getInventory().getTitle().substring(e.getInventory().getTitle().lastIndexOf(",")+2)+"."+e.getCurrentItem().getItemMeta().getDisplayName());
						}
						else{
							list.add(e.getInventory().getTitle().substring(2, e.getInventory().getTitle().length()));
							list.add(e.getCurrentItem().getItemMeta().getDisplayName());
						}
						list.add(ChatColor.BLUE+"Edit this book with the wanted value,");
						list.add(ChatColor.BLUE+"then left click with it");
						bm.setLore(list);
						is.setItemMeta(bm);
						e.getWhoClicked().getInventory().addItem(is);
					}
					else if(e.getCurrentItem().getType()==Material.BARRIER){
						if(e.getInventory().getTitle().contains(",")){
							e.getWhoClicked().openInventory(getParent(e.getInventory().getTitle().substring(2)));
						}
						else
							e.getWhoClicked().openInventory(getParent(e.getInventory().getTitle().substring(2)));
					}
				}
				e.setCancelled(true);
			}
			else if(e.getInventory().getTitle().equals(ChatColor.DARK_BLUE+"Inventories")){
				if(e.getCurrentItem()!=null){
					if(e.getCurrentItem().getType()==Material.ENCHANTMENT_TABLE && e.getCurrentItem().getItemMeta().hasLore()){
						e.getWhoClicked().openEnchanting(e.getWhoClicked().getLocation(), true);
					}
					else if(e.getCurrentItem().getType()==Material.WORKBENCH && e.getCurrentItem().getItemMeta().hasLore()){
						e.getWhoClicked().openWorkbench(e.getWhoClicked().getLocation(), true);
					}
					else if(e.getCurrentItem().getType()==Material.FURNACE && e.getCurrentItem().getItemMeta().hasLore()){
						Inventory inv = Bukkit.createInventory(null, InventoryType.FURNACE);
						e.getWhoClicked().openInventory(inv);
					}
					else if(e.getCurrentItem().getType()==Material.ENDER_CHEST && e.getCurrentItem().getItemMeta().hasLore()){
						e.getWhoClicked().openInventory(  ((Player) e.getWhoClicked()).getEnderChest()  );
					}
					else if(e.getCurrentItem().getType()==Material.ANVIL && e.getCurrentItem().getItemMeta().hasLore()){
						Inventory inv = Bukkit.createInventory(null, InventoryType.ANVIL);
						e.getWhoClicked().openInventory(inv);
					}
					else if(e.getCurrentItem().getType()==Material.CHEST && e.getCurrentItem().getItemMeta().hasLore()){
						e.getWhoClicked().openInventory(inventory2((Player) e.getWhoClicked()));
					}
				}
				e.setCancelled(true);
			}
		}
	}
	
	public static Inventory mainCreate(){
		Inventory inv = Bukkit.createInventory(null, 9, "Utilities");
		ItemStack reload = new ItemStack(Material.COMPASS);
		ItemMeta im = reload.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"Reload");
		List<String> l = new LinkedList<String>();
		l.add(ChatColor.GREEN+"Reload the config of some plugin(s) or reload the server");
		im.setLore(l);
		reload.setItemMeta(im);
		inv.setItem(2, reload);
		
		ItemStack manag = new ItemStack(Material.ANVIL);
		im = manag.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"Manage plugins");
		l = new LinkedList<String>();
		l.add(ChatColor.GREEN+"Enable or Disable plugins");
		im.setLore(l);
		manag.setItemMeta(im);
		inv.setItem(0, manag);
		
		inv.setItem(1, rename(new ItemStack(Material.SLIME_BALL) , ChatColor.AQUA+"Cheats" , ChatColor.GREEN+"Open a cheat menu"));
		inv.setItem(3, rename(new ItemStack(Material.IRON_HELMET) , ChatColor.AQUA+"Players" , ChatColor.GREEN+"Interact with online players"));
		inv.setItem(4, rename(new ItemStack(Material.BOOK) , ChatColor.AQUA+"IG config edit" , ChatColor.GREEN+"In game config editing"));
		inv.setItem(5, rename(new ItemStack(Material.WORKBENCH) , ChatColor.AQUA+"Multi inventories" , ChatColor.GREEN+"Open specials inventories"));
		
		return inv;
	}
	
	public static ItemStack itemCreate(){
		ItemStack is = new ItemStack(Material.COMMAND);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"Dev utilities");
		List<String> l = new LinkedList<String>();
		l.add(ChatColor.GREEN+"Utilities menu");
		im.setLore(l);
		is.setItemMeta(im);
		return is;
	}
	
	public Inventory reloadCreate(String str){
		Plugin[] plugs = getServer().getPluginManager().getPlugins();
		int i = 9;
		if(plugs.length+1>=9){
			do{
				i = i+9;
			}while(i<plugs.length+1);
		}
		Inventory inv = Bukkit.createInventory(null, i ,ChatColor.DARK_BLUE+str);
		for(Plugin p : plugs){
			ItemStack is = new ItemStack(Material.NETHER_STAR);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(p.getName());
			List<String> l = new LinkedList<String>();
			if(p.getDescription().getDescription()!=null){
				if(p.getDescription().getDescription().length()>=50){
					String st = p.getDescription().getDescription();
					st = st.substring(0, 50);
					st = st +"...";
					l.add(st);
				}
				else{
					l.add(p.getDescription().getDescription());
				}
			}
			if(p.getDescription().getAuthors()!=null){
				l.add(ChatColor.DARK_AQUA+"by "+p.getDescription().getAuthors());
				if(str.equals("Enable/Disable")){
					if(p.isEnabled()){
						l.add(ChatColor.GREEN+"Enabled");
					}
					else{
						l.add(ChatColor.RED+"Disabled");
					}
				}
			}
			im.setLore(l);
			is.setItemMeta(im);
			inv.addItem(is);
		}
		if(str.equals("Reload"))
			inv.setItem(inv.getSize()-2, rename(new ItemStack(Material.EMERALD) , ChatColor.DARK_AQUA+"ALL" , "Reload ALL the plugins and the server"));
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemMeta im = back.getItemMeta();
		im.setDisplayName(ChatColor.RED+"Back");
		back.setItemMeta(im);
		inv.setItem(inv.getSize()-1, back);
		return inv;
	}
	
	
	public Inventory cheatsCreate(){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_BLUE+"Cheats");
		inv.addItem(rename(new ItemStack(Material.FEATHER),ChatColor.AQUA+"Fly" , "Allow you to fly"));
		inv.addItem(rename(new ItemStack(Material.BEDROCK),ChatColor.AQUA+"Switch Gamemode" , "Change your Gamemode"));
		inv.addItem(rename(new ItemStack(Material.DIAMOND_CHESTPLATE),ChatColor.AQUA+"GodMode" , "Make you invulnerable"));
		
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemMeta im = back.getItemMeta();
		im.setDisplayName(ChatColor.RED+"Back");
		back.setItemMeta(im);
		inv.setItem(inv.getSize()-1, back);
		return inv;
	}
	
	@SuppressWarnings("unchecked")
	public Inventory invsCreate(){
		int i = 9;
		List<Player> ps = (List<Player>) getServer().getOnlinePlayers();
		if(ps.size()>=i){
			do{
				i=i+9;
			}while(i<ps.size());
		}
		i+=9;
		Inventory inv = Bukkit.createInventory(null, i, ChatColor.DARK_BLUE+"Players");
		for(Player p : ps){
			ItemStack is = rename(new ItemStack(Material.SKULL_ITEM , 1 , (short)3), p.getName() , ChatColor.GREEN+"Left click - TP ");
			List<String> list=new LinkedList<String>();
			list.add(is.getItemMeta().getLore().get(0));
			list.add(ChatColor.AQUA+"Rigth click - open inventory");
			list.add(ChatColor.RED+"Middle click - kick ");
			list.add(p.getUniqueId().toString());
			SkullMeta sm = (SkullMeta) is.getItemMeta();
			sm.setLore(list);
			sm.setOwner(p.getName());
			is.setItemMeta(sm);
			inv.addItem(is);
		}
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemMeta im = back.getItemMeta();
		im.setDisplayName(ChatColor.RED+"Back");
		back.setItemMeta(im);
		inv.setItem(inv.getSize()-1, back);
		return inv;
	}
	
	public static ItemStack rename(ItemStack is , String st , String l){
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(st);
		List<String> list=new LinkedList<String>();
		list.add(l);
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	
	public Inventory fileExplorerCreate(Plugin p){
		int i =9;
		File dir = p.getDataFolder();
		Inventory inv = Bukkit.createInventory(null, i , ChatColor.DARK_BLUE+"Explorer - "+dir.getPath());
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			if(files.length>=i){
				do{
					i = i+9;
				}while(i<files.length);
			}
			inv = Bukkit.createInventory(null, i , ChatColor.DARK_BLUE+"Explorer - "+dir.getPath());
			for(File f:files){
				if(f.isDirectory()){
					ItemStack is = new ItemStack(Material.BOOK);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(f.getName());
					List<String> list=new LinkedList<String>();
					list.add(ChatColor.BLUE+"Directory");
					list.add(f.getPath());
					im.setLore(list);
					is.setItemMeta(im);
					inv.addItem(is);
					
				}
				else{
					ItemStack is = new ItemStack(Material.PAPER);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(f.getName());
					List<String> list=new LinkedList<String>();
					if(f.getName().endsWith(".yml"))
						list.add(ChatColor.BLUE+"Config file");
					else if(f.getName().contains("."))
						list.add(ChatColor.RED+f.getName().substring(f.getName().lastIndexOf("."))+" file");
					else
						list.add(ChatColor.RED+"Unknow");
					list.add(f.getPath());
					im.setLore(list);
					is.setItemMeta(im);
					inv.addItem(is);
				}
			}
		}
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemMeta im = back.getItemMeta();
		im.setDisplayName(ChatColor.RED+"Back");
		back.setItemMeta(im);
		inv.setItem(inv.getSize()-1, back);
		return inv;
	}
	
	public Inventory fileExplorerCreate(File dir){
		int i =9;
		Inventory inv = Bukkit.createInventory(null, i , ChatColor.DARK_BLUE+"Explorer - "+dir.getPath());
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			if(files.length>=i){
				do{
					i = i+9;
				}while(i<files.length);
			}
			inv = Bukkit.createInventory(null, i , ChatColor.DARK_BLUE+"Explorer - "+dir.getPath());
			for(File f:files){
				if(f.isDirectory()){
					ItemStack is = new ItemStack(Material.BOOK);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(f.getName());
					List<String> list=new LinkedList<String>();
					list.add(ChatColor.BLUE+"Directory");
					list.add(f.getPath());
					im.setLore(list);
					is.setItemMeta(im);
					inv.addItem(is);
					
				}
				else{
					ItemStack is = new ItemStack(Material.PAPER);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(f.getName());
					List<String> list=new LinkedList<String>();
					if(f.getName().endsWith(".yml"))
						list.add(ChatColor.BLUE+"Config file");
					else if(f.getName().contains("."))
						list.add(ChatColor.RED+f.getName().substring(f.getName().lastIndexOf("."))+" file");
					else
						list.add(ChatColor.RED+"Unknow");
					list.add(f.getPath());
					im.setLore(list);
					is.setItemMeta(im);
					inv.addItem(is);
				}
			}
		}
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemMeta im = back.getItemMeta();
		im.setDisplayName(ChatColor.RED+"Back");
		back.setItemMeta(im);
		inv.setItem(inv.getSize()-1, back);
		return inv;
	}
	
	public Inventory getParent(String str){
		
		if(str.contains("/")){
			str = new String(str.replace("\\", "/"));
		}
		
		if(str.contains(",")){
			if(str.substring(str.lastIndexOf(",")+2).contains(".")){
				File fi = new File(str.substring(0, str.lastIndexOf(",")-1));
				FileConfiguration fg = YamlConfiguration.loadConfiguration(fi);
				return(configExplorer(fg.getConfigurationSection(str.substring(str.lastIndexOf(",")+2)).getParent() , fi));
			}
			else{
				File fi = new File(str.substring(0, str.indexOf(",")-1));
				getLogger().info(fi.getAbsolutePath());
				FileConfiguration fg = YamlConfiguration.loadConfiguration(fi);
				return(configExplorer(fg, fi));
			}
		}
		else{
			File f = new File(str);
			if(f.getParent().equals("plugins")){
				return reloadCreate("Config edit");
			}
			else{
				return fileExplorerCreate(new File(f.getParent()));
			}
		}
	}
	
	public Inventory configExplorer(FileConfiguration fg , File f){
		int i =9;
		if(f.getPath().contains("/")){
			f = new File(f.getPath().replace("\\", "/"));
			fg= YamlConfiguration.loadConfiguration(f);
		}
		LinkedHashSet<String> l = (LinkedHashSet<String>) fg.getKeys(false);
		if(l.size()>=i){
			do{
				i = i+9;
			}while(i<l.size());
		}
		Inventory inv = Bukkit.createInventory(null, i , ChatColor.DARK_BLUE+f.getPath());
		for(String s : l){
			if(fg.isConfigurationSection(s)){
				ItemStack is = new ItemStack(Material.CHEST);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(s);
				List<String> list=new LinkedList<String>();
				list.add(ChatColor.BLUE+"Configuration section");
				im.setLore(list);
				is.setItemMeta(im);
				inv.addItem(is);
			}
			else{
				ItemStack is = new ItemStack(Material.TRIPWIRE_HOOK);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(s);
				List<String> list=new LinkedList<String>();
				if(fg.getString(s).length()>50){
					String st = fg.getString(s);
					list.add("Value : "+ChatColor.GREEN);
					List<String> lis = new LinkedList<String>();
					do{
						lis.add(st.substring(0, 49));
						st = st.substring(49);
					}while(st.length()>50);
					for (String s3 : lis){
						list.add(s3);
					}
					list.add(st);
				}
				else{
					list.add(ChatColor.GREEN+"Value : ");
					list.add(fg.getString(s));
				}
				im.setLore(list);
				is.setItemMeta(im);
				inv.addItem(is);
			}
		}
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemMeta im = back.getItemMeta();
		im.setDisplayName(ChatColor.RED+"Back");
		back.setItemMeta(im);
		inv.setItem(inv.getSize()-1, back);
		return inv;
	}
	
	public Inventory configExplorer(ConfigurationSection fg , File f){
		int i =9;
		LinkedHashSet<String> l = (LinkedHashSet<String>) fg.getKeys(false);
		if(l.size()>=i){
			do{
				i = i+9;
			}while(i<l.size());
		}
		Inventory inv = Bukkit.createInventory(null, i , ChatColor.DARK_BLUE+f.getPath()+" , " + fg.getCurrentPath());
		for(String s : l){
			if(fg.isConfigurationSection(s)){
				ItemStack is = new ItemStack(Material.CHEST);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(s);
				List<String> list=new LinkedList<String>();
				list.add(ChatColor.BLUE+"Configuration section");
				im.setLore(list);
				is.setItemMeta(im);
				inv.addItem(is);
			}
			else{
				ItemStack is = new ItemStack(Material.TRIPWIRE_HOOK);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(s);
				List<String> list=new LinkedList<String>();
				if(fg.getString(s).length()>50){
					String st = fg.getString(s);
					list.add("Value : "+ChatColor.GREEN);
					List<String> lis = new LinkedList<String>();
					do{
						lis.add(st.substring(0, 49));
						st = st.substring(49);
					}while(st.length()>50);
					for (String s3 : lis){
						list.add(s3);
					}
					list.add(st);
				}
				else{
					list.add(ChatColor.GREEN+"Value : ");
					list.add(fg.getString(s));
				}
				im.setLore(list);
				is.setItemMeta(im);
				inv.addItem(is);
			}
		}
		ItemStack back = new ItemStack(Material.BARRIER);
		ItemMeta im = back.getItemMeta();
		im.setDisplayName(ChatColor.RED+"Back");
		back.setItemMeta(im);
		inv.setItem(inv.getSize()-1, back);
		return inv;
	}
	
	public Inventory multiInvCreate(){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_BLUE+"Inventories");
		
		inv.addItem(rename(new ItemStack(Material.ENCHANTMENT_TABLE),ChatColor.AQUA+"Enchantement table" , "Open an enchantement table"));
		inv.addItem(rename(new ItemStack(Material.ANVIL),ChatColor.AQUA+"Anvil" , "Open an anvil"));
		inv.addItem(rename(new ItemStack(Material.ENDER_CHEST),ChatColor.AQUA+"Ender chest" , "Open your enderchest"));
		inv.addItem(rename(new ItemStack(Material.WORKBENCH),ChatColor.AQUA+"Workbench" , "Open a workbench"));
		inv.addItem(rename(new ItemStack(Material.FURNACE),ChatColor.AQUA+"Furnace" , "Open a furnace"));
		inv.addItem(rename(new ItemStack(Material.CHEST),ChatColor.AQUA+"2nd inventory" , "Open your 2nd inventory"));
		
		return inv;
	}

	@SuppressWarnings("unchecked")
	public Inventory inventory2(Player p){
		Inventory inv = Bukkit.createInventory(null , 27 , ChatColor.DARK_BLUE+"Second inventory");
		FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(invFileLocation));
		if(fg.get(p.getName())==null){
			fg.set(p.getName(),inv.getContents());
			try {
				fg.save(new File(invFileLocation));
			} catch (IOException e1) {
				getLogger().info("Unable to save Invs.yml");
			}
			fg = YamlConfiguration.loadConfiguration(new File(invFileLocation));
			List<ItemStack> lis = (List<ItemStack>) fg.get(p.getName());
			ItemStack[] is = new ItemStack[lis.size()];
			is = lis.toArray(is);
			inv.setContents(is);
		}
		else{
			List<ItemStack> lis = (List<ItemStack>) fg.get(p.getName());
			ItemStack[] is = new ItemStack[lis.size()];
			is = lis.toArray(is);
			inv.setContents(is);
		}
		return inv;
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent e){
		if(e.getInventory().getTitle().equals(ChatColor.DARK_BLUE+"Second inventory")){
			FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(invFileLocation));
			fg.set(e.getPlayer().getName(), e.getView().getTopInventory().getContents());
			try {
				fg.save(new File(invFileLocation));
			} catch (IOException e1) {
				getLogger().info("Unable to save invs.yml");
			}
		}
	}
}
