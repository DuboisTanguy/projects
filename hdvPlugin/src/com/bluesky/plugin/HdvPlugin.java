package com.bluesky.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HdvPlugin extends JavaPlugin implements Listener{
	
	public static String hdvFileLocation = new String("plugins/HdvPlugin/HDV.yml");
	public static String uuidFileLocation = new String("plugins/HdvPlugin/UUIDs.yml");
	public static String nbItemsFileLocation = new String("plugins/HdvPlugin/NbItems.yml");
	public static String mailFileLocation = new String("plugins/HdvPlugin/MailFile.yml");
	
	public static Economy eco = null;
	public static EconomyResponse r;
	public static EconomyResponse r2;
	
	@Override
	public void onEnable(){
		File hdvFile = new File(hdvFileLocation);
		File uuidFile = new File(uuidFileLocation);
		File nbItemFile = new File(nbItemsFileLocation);
		File mailFile = new File(mailFileLocation);
		FileConfiguration hdv = YamlConfiguration.loadConfiguration(hdvFile);
		PluginDescriptionFile pdf = getDescription();
		getLogger().info("Plugin "+pdf.getName() + " demarre !");
		saveDefaultConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this , this);
		if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		Inventory hdvInv = Bukkit.createInventory(null, 54, "Hotel des ventes");
		
		if (!hdvFile.exists()){
			try {
				hdvFile.createNewFile();
			} catch (IOException e) {
				getLogger().info("Impossible de creer le fichier "+hdvFile.getName());
			}
		}
		
		if (!uuidFile.exists()){
			try {
				uuidFile.createNewFile();
			} catch (IOException e) {
				getLogger().info("Impossible de creer le fichier "+uuidFile.getName());
			}
		}
		
		if (!nbItemFile.exists()){
			try {
				nbItemFile.createNewFile();
			} catch (IOException e) {
				getLogger().info("Impossible de creer le fichier " + nbItemFile.getName());
			}
		}
		
		if(!mailFile.exists()){
			try {
				mailFile.createNewFile();
			} catch (IOException e) {
				getLogger().info("Impossible de creer le fichier "+mailFile.getName());
			}
		}
		
		if (hdv.get("HDV1")!=null){
			getLogger().info("HDV charge !");
		}
		else{
			hdv.set("HDV1", hdvInv.getContents());
			getLogger().info("HDV cree !");
			try {
				hdv.save(hdvFile);
			} catch (IOException e) {
				getLogger().info("Impossible de sauvegarder le fichier HDV.yml");
			}
		}
	}
	
	@Override
	public void onDisable(){
		PluginDescriptionFile pdf = getDescription();
		Bukkit.getLogger().info("Plugin "+pdf.getName() + " s arrete !");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender , Command cmd , String label , String args[]){
		File hdvFile = new File(hdvFileLocation);
		FileConfiguration hdv = YamlConfiguration.loadConfiguration(hdvFile);
		File nbFile = new File(nbItemsFileLocation);
		FileConfiguration nb = YamlConfiguration.loadConfiguration(nbFile);	
		Inventory hdvInv = Bukkit.createInventory(null, 54, "Hotel des ventes");
		List<ItemStack> list = (List<ItemStack>) (hdv.get("HDV1"));
		ItemStack[] a = new ItemStack[list.size()];
		a = list.toArray(a);
		if (a!=null){
			hdvInv.setContents(a);
		}
		if(cmd.getName().equalsIgnoreCase("hdv")){
			if(sender!=getServer().getConsoleSender()){
				Player p = (Player)sender;
				if(args.length>=1){
					if(args[0].equalsIgnoreCase("open")&&args.length==1){
						p.openInventory(hdvInv);
						if(hdvInv.getTitle().equals("Hotel des ventes")){
							if(hdv.get("HDV2")!=null){
								ItemStack isNext = new ItemStack(Material.ARROW);
								ItemMeta im = isNext.getItemMeta();
								im.setDisplayName(ChatColor.AQUA+"Page suivante");
								isNext.setItemMeta(im);
								hdvInv.setItem(53, isNext);
							}
						}
					}
					else if (args[0].equalsIgnoreCase("open")&&args.length!=1){
						p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Syntaxe invalide ! Usage : /hdv open");
					}
					else if(args[0].equalsIgnoreCase("create")&&args.length>1){
						if(p.getInventory().getItemInMainHand().getType()!=Material.AIR){
							if(args.length==3){
								try {
									Integer.parseInt(args[1]);
								} catch (NumberFormatException e) {
									p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Le nombre d'items à vendre entré est invalide !");
									return true;
								}
								try {
									Integer.parseInt(args[2]);
								} catch (NumberFormatException e) {
									p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Le prix entré est invalide !");
									return true;
								}
								if (Integer.parseInt(args[2])>0 && Integer.parseInt(args[2])<=1000000 && Integer.parseInt(args[1])!=0 && Integer.parseInt(args[1]) <= p.getInventory().getItemInMainHand().getAmount() && nb.getInt(p.getName()) < getConfig().getInt("nbItemsMax")){
									ItemStack b = p.getInventory().getItemInMainHand().clone();
									int i = Integer.parseInt(args[1]);
									b.setAmount(i);
									int v = 1;
									if (hdvInv.firstEmpty()>=45){
										do{
											File hdvFile2 = new File(hdvFileLocation);
											FileConfiguration hdv2 = YamlConfiguration.loadConfiguration(hdvFile2);
											v++;
											if (hdv2.get("HDV"+v)==null){
												hdv2.set("HDV"+v, Bukkit.createInventory(null, 54, "Chest").getContents());
												try {
													hdv2.save(hdvFile2);
												} catch (IOException e) {
													getLogger().info("Impossible de sauvegarder le fichier HDV.yml");
												}
											}
											hdvInv = Bukkit.createInventory(null, 54, "Hotel des ventes");
											try{
												hdvFile2 = new File(hdvFileLocation);
												hdv2 = YamlConfiguration.loadConfiguration(hdvFile2);
												List<ItemStack> list2 = (List<ItemStack>) (hdv2.get("HDV"+v));
												ItemStack[] a2 = new ItemStack[list2.size()];
												a2 = list2.toArray(a2);
												hdvInv.clear();
												hdvInv.setContents(a2);
											}
											catch(ClassCastException e){
												p.sendMessage("Fatal Error !");
												return true;
											}				
										}while(hdvInv.firstEmpty()>45);
									}
									if (p.getInventory().getItemInMainHand().getAmount()==i){
										p.getInventory().remove(p.getInventory().getItemInMainHand());
									}
									else{
										p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-i);
									}
									ItemMeta im = b.getItemMeta();
									List<String> lore = Arrays.asList(ChatColor.AQUA + "PRIX : " , args[2] ,ChatColor.GREEN + "Vendu par : ", p.getName());
									im.setLore(lore);
									b.setItemMeta(im);
									if(nb.get(p.getName())==null){
										nb.set(p.getName(), 1);
									}
									else{
										nb.set(p.getName(), nb.getInt(p.getName())+1);
									}
									try {
										nb.save(nbFile);
									} catch (IOException e1) {
										getLogger().info("error occured");
									}
									if (b.getItemMeta().hasDisplayName()){
										p.sendMessage(ChatColor.GREEN+b.getItemMeta().getDisplayName() + " x "+b.getAmount()+" mis en vente avec succès !");
									}
									else{
										p.sendMessage(ChatColor.GREEN+b.getType().name() + " x "+b.getAmount()+" mis en vente avec succès !");
									}
									hdvInv.setItem(hdvInv.firstEmpty(), b);
									hdv.set("HDV"+v, hdvInv.getContents());						
									try {
										hdv.save(hdvFile);
									} catch (IOException e) {
										getLogger().info("Impossible de sauvegarder le fichier HDV.yml");
									}
									File uuidFile = new File(uuidFileLocation);
									FileConfiguration uuid = YamlConfiguration.loadConfiguration(uuidFile);
									uuid.set(p.getName(), p.getUniqueId().toString());
									try {
										uuid.save(uuidFile);
									} catch (IOException e) {
										getLogger().info("Impossible de sauvegarder le fichier UUIDs.yml");
									}
								}
								else if (nb.getInt(p.getName()) >= getConfig().getInt("nbItemsMax")){
									p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Vous ne pouvez pas vendre plus d'items !");
								}
								else if (Integer.parseInt(args[1])==0 || Integer.parseInt(args[1]) > p.getInventory().getItemInMainHand().getAmount()){
									p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Le nombre d'items à vendre doit être compris entre 1 et le nombre que vous en possédez !");
								}
								else if (Integer.parseInt(args[2])>=0 || Integer.parseInt(args[2])<=1000000){
									p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Votre prix doit être compris entre 0 et 1000000 !");
								}
							}
							else{
								p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Syntaxe invalide ! Usage : /hdv create [nombre d'items] [prix]");
							}
						}
						else{
							p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Ce block ne peut pas se vendre !");
						}
					}
					else if(args[0].equalsIgnoreCase("create")&&(args.length==1) || args.length>3){
						p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Syntaxe invalide ! Usage : /hdv create [nombre d'items] [prix]");
					}
					else{
						p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Veuillez préciser une action : /hdv [open/create] !");
					}
				}
				else{
					p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Veuillez préciser une action : /hdv [open/create] !");
				}
				return true;
			}
		}
		return false;
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }
	
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e){
		File mailFile = new File(mailFileLocation);
		FileConfiguration mail = YamlConfiguration.loadConfiguration(mailFile);
		File hdvFile = new File(hdvFileLocation);
		FileConfiguration hdv = YamlConfiguration.loadConfiguration(hdvFile);
		Inventory hdvInv = e.getInventory();
		List<ItemStack> list = (List<ItemStack>) (hdv.get("HDV1"));
		ItemStack[] a = new ItemStack[list.size()];
		a = list.toArray(a);
		int v = 1;
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getType()==InventoryType.CHEST ){
			if (e.getInventory().getTitle().contains("Hotel des ventes")){
				if (e.getInventory().getSize()==54){
					if(e.getCurrentItem()!=null){
						if (e.getCurrentItem().getType()!=Material.AIR){
							if (e.getCurrentItem().getItemMeta().getLore()!=null){
								if (e.getCurrentItem().getItemMeta().getLore().size()==4){
									double d = Double.parseDouble(e.getCurrentItem().getItemMeta().getLore().get(1));
									r = eco.withdrawPlayer(getServer().getOfflinePlayer(p.getUniqueId()), d);
									File uuidFile = new File(uuidFileLocation);
									FileConfiguration uuid = YamlConfiguration.loadConfiguration(uuidFile);
									File nbFile = new File(nbItemsFileLocation);
									FileConfiguration nb = YamlConfiguration.loadConfiguration(nbFile);
									UUID te = UUID.fromString(uuid.getString(e.getCurrentItem().getItemMeta().getLore().get(3)));
									if(r.transactionSuccess()){
										r2 = eco.depositPlayer(getServer().getOfflinePlayer(te), d);
									}
									else{
										p.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Vous n'avez pas assez d'argent !");
									}
									if (r.transactionSuccess() && r2.transactionSuccess()){
											nb.set(e.getCurrentItem().getItemMeta().getLore().get(3), nb.getInt(e.getCurrentItem().getItemMeta().getLore().get(3))-1);
										try {
											nb.save(nbFile);
										} catch (IOException e2) {
											getLogger().info("Unable to save "+nbFile.getName());
										}
										if (e.getCurrentItem().getItemMeta().hasDisplayName()){
											p.sendMessage(ChatColor.GREEN+e.getCurrentItem().getItemMeta().getDisplayName() + " x "+e.getCurrentItem().getAmount()+" acheté(s) avec succès !");
										}
										else{
											p.sendMessage(ChatColor.GREEN+e.getCurrentItem().getType().name() + " x "+e.getCurrentItem().getAmount()+" acheté(s) avec succès !");
										}
										if(Bukkit.getOfflinePlayer(te).isOnline()){
											if (e.getCurrentItem().getItemMeta().hasDisplayName()){
												p.sendMessage(ChatColor.GREEN+"Votre "+e.getCurrentItem().getItemMeta().getDisplayName() + " x "+e.getCurrentItem().getAmount()+" a été vendu avec succès !");
											}
											else{
												p.sendMessage(ChatColor.GREEN+"Votre "+e.getCurrentItem().getType().name() + " x "+e.getCurrentItem().getAmount()+" a été vendu avec succès !");
											}
										}
										else{
											String str = null;
											if(mail.get(Bukkit.getOfflinePlayer(te).getName())==null){
												if (e.getCurrentItem().getItemMeta().hasDisplayName()){
													str = new String(ChatColor.GREEN+"Votre "+e.getCurrentItem().getItemMeta().getDisplayName() + " x "+e.getCurrentItem().getAmount()+" a été vendu avec succès !");
												}
												else{
													str = new String(ChatColor.GREEN+"Votre "+e.getCurrentItem().getType().name() + " x "+e.getCurrentItem().getAmount()+" a été vendu avec succès !");
												}
											}
											else{
												List<String> msg=(List<String>) (mail.get(Bukkit.getOfflinePlayer(te).getName()));
												if (e.getCurrentItem().getItemMeta().hasDisplayName()){
													msg.add(ChatColor.GREEN+"Votre "+e.getCurrentItem().getItemMeta().getDisplayName() + " x "+e.getCurrentItem().getAmount()+" a été vendu avec succès !");
												}
												else{
													msg.add(ChatColor.GREEN+"Votre "+e.getCurrentItem().getType().name() + " x "+e.getCurrentItem().getAmount()+" a été vendu avec succès !");
												}
											}
											mail.set(Bukkit.getOfflinePlayer(te).getName(), str);
											try {
												mail.save(mailFile);
											} catch (IOException e1) {
												getLogger().info("Unable to save "+mailFile.getName());
											}
										}
										ItemStack achat = e.getCurrentItem().clone();
										ItemMeta achatIm = achat.getItemMeta();
										achatIm.setLore(null);
										achat.setItemMeta(achatIm);
										p.getInventory().addItem(achat);
										hdvInv.clear(e.getSlot());
										if(hdvInv.getTitle().contains("Page :")){
											v =Integer.parseInt(e.getView().getTopInventory().getTitle().substring(e.getView().getTopInventory().getTitle().indexOf(":")+2));
										}
										hdv.set("HDV"+v, hdvInv.getContents());
										try {
											hdv.save(hdvFile);
										} catch (IOException e1) {
											getLogger().info("Impossible de sauvegarder le fichier HDV.yml");
										}
									}
									else{
										p.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"ECHEC DE L ACHAT !");
									}
									e.setCancelled(true);
									e.getView().close();
								}
							}
							else if(e.getCurrentItem().getItemMeta().hasDisplayName()){
								if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA+"Page suivante")){
									e.setCancelled(true);
									hdvFile = new File(hdvFileLocation);
									hdv = YamlConfiguration.loadConfiguration(hdvFile);
									if (e.getView().getTopInventory().getTitle().contains(":"))
										v = Integer.parseInt(e.getView().getTopInventory().getTitle().substring(e.getView().getTopInventory().getTitle().indexOf(":")+2));
									v++;
									hdvInv = Bukkit.createInventory(null, 54, "Hotel des ventes - Page : "+v);		
									List<ItemStack> list2 = (List<ItemStack>) (hdv.get("HDV"+v));
									a = new ItemStack[list2.size()];
									a = list2.toArray(a);
									e.getView().close();
									hdvInv.clear();
									hdvInv.setContents(a);
									ItemStack isNext = new ItemStack(Material.ARROW);
									ItemMeta im = isNext.getItemMeta();
									im.setDisplayName(ChatColor.AQUA+"Page suivante");
									isNext.setItemMeta(im);
									if(hdvInv.getTitle().contains("Page :")){
										if(hdv.get("HDV"+(v+1))!=null){
											hdvInv.setItem(53, isNext);
										}
									}
									ItemStack isPrev = new ItemStack(Material.ARROW);
									ItemMeta im2 = isPrev.getItemMeta();
									im2.setDisplayName(ChatColor.AQUA+"Page précédente");
									isPrev.setItemMeta(im2);
									hdvInv.setItem(45, isPrev);
									p.openInventory(hdvInv);
								}
								else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA+"Page précédente")){
									e.setCancelled(true);
									hdvFile = new File(hdvFileLocation);
									hdv = YamlConfiguration.loadConfiguration(hdvFile);
									v = Integer.parseInt(e.getView().getTopInventory().getTitle().substring(e.getView().getTopInventory().getTitle().indexOf(":")+2));
									v--;
									hdvInv = Bukkit.createInventory(null, 54, "Hotel des ventes - Page : "+v);		
									List<ItemStack> list2 = (List<ItemStack>) (hdv.get("HDV"+v));
									a = new ItemStack[list2.size()];
									a = list2.toArray(a);
									e.getView().close();
									hdvInv.clear();
									hdvInv.setContents(a);
									ItemStack isNext = new ItemStack(Material.ARROW);
									ItemMeta im = isNext.getItemMeta();
									im.setDisplayName(ChatColor.AQUA+"Page suivante");
									isNext.setItemMeta(im);
									hdvInv.setItem(53, isNext);
									if (v!=1){
										ItemStack isPrev = new ItemStack(Material.ARROW);
										ItemMeta im2 = isPrev.getItemMeta();
										im2.setDisplayName(ChatColor.AQUA+"Page précédente");
										isPrev.setItemMeta(im2);
										hdvInv.setItem(45, isPrev);
										
									}
									p.openInventory(hdvInv);
								}
							}
						}
					}
				e.setCancelled(true);
				}			
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e){
		File mailFile = new File(mailFileLocation);
		FileConfiguration mail = YamlConfiguration.loadConfiguration(mailFile);
		if(mail.get(e.getPlayer().getName())!=null){
			if(mail.isList(e.getPlayer().getName())){
				List<String> list = (List<String>) mail.getList(e.getPlayer().getName());
				for(String str : list){
					e.getPlayer().sendMessage(str);
				}
			}
			else{
				e.getPlayer().sendMessage(mail.getString(e.getPlayer().getName()));
			}
			mail.set(e.getPlayer().getName(),null);
			try {
				mail.save(mailFile);
			} catch (IOException e1) {
				getLogger().info("unable to save "+mailFile.getName());
			}
		}
	}
}
