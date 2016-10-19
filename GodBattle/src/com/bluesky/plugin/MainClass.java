package com.bluesky.plugin;
 
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.bluesky.godsdata.God;
import com.bluesky.objects.Healer;
import com.bluesky.objects.JumpPad;

 
public class MainClass extends JavaPlugin implements Listener{
   
	int task;
	private static Inventory inventory;
	private static String mapFileLocation = new String("plugins/GodBattle/maps.yml");
	private static HashMap<String, Integer> hm1 = new HashMap<String , Integer>();
	private static List<Player> l1 = new LinkedList<Player>();
	private static HashMap<Player , Object> m = new HashMap<Player , Object>();
    private static HashMap<String , Integer> hm = new HashMap<String , Integer>();
    private static HashMap<String , Integer> hm2 = new HashMap<String , Integer>();
    private static HashMap<String , Integer> hm3 = new HashMap<String , Integer>();
    private static HashMap<String , Integer> hm4 = new HashMap<String , Integer>();
    private static HashMap<World , Integer> hm5 = new HashMap<World , Integer>();
    private static HashMap<World , Sign> siwo = new HashMap<World , Sign>();
    private static ItemStack[] items = { rename(new ItemStack(Material.STICK) , ChatColor.GREEN+"Primary" , ChatColor.GREEN+"Attaque basique")
                                        ,rename(new ItemStack(Material.BLAZE_ROD) , ChatColor.BLUE+"Secondary" , ChatColor.BLUE+"Attaque secondaire")
                                        ,rename(new ItemStack(Material.DIAMOND) , ChatColor.DARK_PURPLE+"Ultimate" , ChatColor.DARK_PURPLE+"ULTIMAAAAATE !")};
   
	@SuppressWarnings("unchecked")
	@Override
    public void onEnable(){
        saveDefaultConfig();
        PluginDescriptionFile pdf = getDescription();
        getLogger().info(pdf.getName()+" "+pdf.getVersion()+" by "+pdf.getAuthors()+" enabled !\n");
        getLogger().info("\t=======================================================================================");
        getLogger().info("\tI   _______  _______  ______   ______   _______ __________________ _        _______   I");
        getLogger().info("\tI  (  ____ \\(  ___  )(  __  \\ (  ___ \\ (  ___  )\\__   __/\\__   __/( \\      (  ____ \\  I");
        getLogger().info("\tI  | (    \\/| (   ) || (  \\  )| (   ) )| (   ) |   ) (      ) (   | (      | (    \\/  I");
        getLogger().info("\tI  | |      | |   | || |   ) || (__/ / | (___) |   | |      | |   | |      | (_       I");
        getLogger().info("\tI  | | ____ | |   | || |   | ||  __ (  |  ___  |   | |      | |   | |      |  __)     I");
        getLogger().info("\tI  | | \\_  )| |   | || |   ) || (  \\ \\ | (   ) |   | |      | |   | |      | (        I");
        getLogger().info("\tI  | (___) || (___) || (__/  )| )___) )| )   ( |   | |      | |   | (____/\\| (____/\\  I");
        getLogger().info("\tI  (_______)(_______)(______/ |/ \\___/ |/     \\|   )_(      )_(   (_______/(_______/  I");
        getLogger().info("\t=======================================================================================\n");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        File f = new File(mapFileLocation);
        if(!f.exists()){
        	try {
				f.createNewFile();
			} catch (IOException e) {
				getLogger().warning("Unable to create maps.yml file !");
			}
        }
        if(getConfig().get("signs")!=null){
	        for(Location l : (List<Location>)getConfig().getList("signs")){
	        	Sign sign = (Sign) l.getBlock().getState();
	        	Bukkit.createWorld(new WorldCreator(sign.getLine(0)));
	        	siwo.put(Bukkit.getWorld(sign.getLine(0)), sign);
	        }
        }
        godChoiceInv();
    }
   
    @Override
    public void onDisable(){
        PluginDescriptionFile pdf = getDescription();
        getLogger().info(pdf.getName()+" "+pdf.getVersion()+" by "+pdf.getAuthors()+" disabled");
    }
   
    @SuppressWarnings("unused")
	@Override
    public boolean onCommand(CommandSender sender , Command cmd , String label , String[] args){
        if(label.equalsIgnoreCase("godbattle")){
            Player p = (Player)sender;
            if(args.length == 1 && args[0].equalsIgnoreCase("set")){
               
                if(p.isOp()){
                    getConfig().set("lobby", p.getLocation());
                    p.sendMessage("§6[§cSilkRoad§6]§a Lobby mis en place !");
                    saveConfig();
                }
            }
                else if(args.length == 1 && args[0].equalsIgnoreCase("pvp")){
                   
                    if(p.isOp()){
                        p.sendMessage("§6[§cSilkRoad§6]§a Le spawn du PvP mis en place !");
                        getConfig().set("lobbypvp", p.getLocation());
                        saveConfig();
                    }
            }
            else if(args.length == 0){
                if(!m.containsKey(p)){
                    p.teleport((Location) getConfig().get("lobby"));
                    p.sendMessage("§6[§cSilkRoad§6]§a Vous avez été téléporté au lobby GodBattle !");
                }else{
                    p.sendMessage("§6[§cSilkRoad§6]§a Vous avez déjà un dieu !");
                }
               
            }
           
            return true;
        }
        else if(label.equalsIgnoreCase("healer")){
        	Player p = (Player)sender;
        	Healer h = new Healer(p.getLocation());
        	return true;
        }
            else if(label.equalsIgnoreCase("test")){
        	Player p = (Player)sender;
        	Party party = new Party();
        	party.setPlayers(p.getWorld().getPlayers());
        	party.createTeams();
        	party.start(p.getWorld());
        	return true;
        }else if(label.equalsIgnoreCase("jumppad")){
        	Player p = (Player)sender;
        	JumpPad jp = new JumpPad(p.getLocation());
        }
        return false;
    }
   
    @SuppressWarnings("rawtypes")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e){
        if(e.getInventory().getTitle().equals(ChatColor.BLUE+"What god do you choose ?")){
            if(e.getCurrentItem()!=null){
                if(e.getCurrentItem().getType()!=Material.AIR){
                    if(e.getCurrentItem().getItemMeta().hasLore()){
                        e.setCancelled(true);
                        try {
                            Player p = (Player)e.getWhoClicked();
                            Class c = Class.forName("com.bluesky.godsdata."+e.getCurrentItem().getItemMeta().getDisplayName());
                            try {
                                God g = (God) c.newInstance();
                                p.getInventory().clear();
                                g.apply(p);
                                m.put(p, g);
                                Bukkit.broadcastMessage(ChatColor.RED+""+ChatColor.BOLD + p.getName()+" est devenu "+g.getName());
                                p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation(), 1000);
                                p.closeInventory();
                                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,Integer.MAX_VALUE, 0 ,true));
                                for(ItemStack is : items){
                                    p.getInventory().addItem(is);
                                }
                                if(p.getWorld()==((Location) getConfig().get("lobbypvp")).getWorld())
                                	p.teleport((Location) getConfig().get("lobbypvp"));
                            } catch (InstantiationException | IllegalAccessException e1) {
                                getLogger().info("Can't instantiate the God type "+c.getName());
                            }
                        } catch (ClassNotFoundException e1) {
                            getLogger().info("Class not found !");
                        }
                    }
                }
            }
        }
        else{
        	if(m.get(e.getWhoClicked())!=null){
        		e.setCancelled(true);
        	}
        }
    }
   
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e){
        Player p = (Player)e.getPlayer();
       
        if(e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK){
            if(e.getItem()!=null){         
            	ItemStack item = new ItemStack(Material.BARRIER);
            	ItemMeta meta = item.getItemMeta();
            	meta.setDisplayName(ChatColor.RED + "You can't do it right now !");
            	item.setItemMeta(meta);
                if(e.getItem().getType()==Material.STICK){
                	
                    if(m.get(p)!=null){
                        e.setCancelled(true);
                        ( (God) (m.get(p))).primary(p);
                        int i = p.getInventory().getHeldItemSlot();
                        p.getInventory().remove(e.getItem());
                        p.getInventory().setItem(i, item);
                        Cooldown cool = new Cooldown(this);
                        cool.setCooldownLength(p, 1 , hm);
                        cool.startCooldown(p, hm  , e.getItem() , 1);
                    }
                }
                else if(e.getItem().getType()==Material.BLAZE_ROD){
                    if(m.get(p)!=null){
                        e.setCancelled(true);
                        ( (God) (m.get(p))).secondary(p);
                        int i = p.getInventory().getHeldItemSlot();
                        p.getInventory().remove(e.getItem());
                        p.getInventory().setItem(i, item);
                        Cooldown cool = new Cooldown(this);
                        cool.setCooldownLength(p, 15 , hm2);
                        cool.startCooldown(p, hm2  , e.getItem() , 2);
                    }
                }
                else if(e.getItem().getType()==Material.DIAMOND){
                    if(m.get(p)!=null){
                        e.setCancelled(true);
                        ( (God) (m.get(p))).ultimate(p);
                        int i = p.getInventory().getHeldItemSlot();
                        p.getInventory().remove(e.getItem());
                        p.getInventory().setItem(i, item);
                        Cooldown cool = new Cooldown(this);
                        cool.setCooldownLength(p,30 , hm3);
                        cool.startCooldown(p, hm3  , e.getItem() , 3);
                    }
                }
                else if(e.getItem().getType() == Material.BARRIER){
                	if(m.get(p)!=null){
                		int i = e.getPlayer().getInventory().getHeldItemSlot();
        				e.setCancelled(true);
        				p.getInventory().setItem(i, item);
        			}
                }
            }
        }
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
   
    @SuppressWarnings("unchecked")
    public Inventory godChoiceInv(){
        List<ItemStack> li = (List<ItemStack>) getConfig().get("GodsItems");
        int i = 9;
        if(li.size()>=i){
            do{
                i = i+9;
            }while(i<li.size());
        }
        Inventory inv = Bukkit.createInventory(null, i, ChatColor.BLUE+"What god do you choose ?");
        for(ItemStack is : li){
            inv.addItem(is);
        }
        inventory = inv;
        return inv;
    }
   
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e){
        if(m.get(e.getEntity())!=null){
            m.remove(e.getEntity());
            e.getDrops().clear();
        }
        FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(mapFileLocation));
        if(fg.get(e.getEntity().getWorld().getName())!=null){
        	if(fg.getString(e.getEntity().getWorld().getName()+".gamemode").equals("TDM")){
        		Scoreboard s = e.getEntity().getScoreboard();
        		e.getEntity().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        		e.getEntity().setGameMode(GameMode.SPECTATOR);
        		int red = 0;
        		int blue = 0;
        		for(Team t : s.getTeams()){
        			for(String str : t.getEntries()){
        				Player p2 = Bukkit.getPlayer(str);
        				if(!p2.isDead() && p2.getGameMode()!=GameMode.SPECTATOR && p2.isOnline()){
        					if(t.getName().equals("Red")){
        						red++;
        					}
        					else if(t.getName().equals("Blue")){
        						blue++;
        					}
        				}
        			}
        		}
        		if(blue == 0 || red == 0){
        			for(Player p : e.getEntity().getWorld().getPlayers()){
        				if(blue==0){
        					p.sendTitle(ChatColor.RED+"RED TEAM WINS !", "GG everyone !");
        				}
        				else{
        					p.sendTitle(ChatColor.BLUE+"BLUE TEAM WINS !", "GG everyone !");
        				}
        				p.setGameMode(GameMode.SPECTATOR);
    					p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    					CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
						c.setCooldownLength(p, 3, hm4);
						c.tpToSpawn(p, hm4, (Location) getConfig().get("spawn"));
        			}
        		}
        	}
        }
    }
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onFallDamage(EntityDamageEvent event){
                if(event.getEntity() instanceof Player){
                    Player player = (Player)event.getEntity();
                    if(event.getCause() == DamageCause.FALL){
                        if(m.get(player) == null){
                            return;
                        }
                        else if(l1.contains(player)){
                        	event.setDamage(0);
                			event.setCancelled(true);
                		}
                        else if( ( (God) (m.get(player)) ).getName().equals("Appolon") ){
                        	event.setDamage(0);
                            event.setCancelled(true);
                        }
                    }
                    if(player.getHealth() - event.getFinalDamage() <= 0){
                    	 FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(mapFileLocation));
                         if(fg.get(event.getEntity().getWorld().getName())!=null){
                         	if(fg.getString(event.getEntity().getWorld().getName()+".gamemode").equals("TDM")){
                         		event.setCancelled(true);
                            	player.setGameMode(GameMode.SPECTATOR);
                            	player.sendTitle(ChatColor.RED+"You've been killed !", "wait for the match to end");
                         		Scoreboard s = ((Player)event.getEntity()).getScoreboard();
                         		int red = 0;
                         		int blue = 0;
                         		for(Team t : s.getTeams()){
                         			for(String str : t.getEntries()){
                         				Player p2 = Bukkit.getPlayer(str);
                         				if(!p2.isDead() && p2.getGameMode()!=GameMode.SPECTATOR && p2.isOnline()){
                         					if(t.getName().equals("Red")){
                         						red++;
                         					}
                         					else if(t.getName().equals("Blue")){
                         						blue++;
                         					}
                         				}
                         			}
                         		}
                         		if(blue == 0 || red == 0){
                         			for(Player p : event.getEntity().getWorld().getPlayers()){
                         				if(blue==0){
                         					p.sendTitle(ChatColor.RED+"RED TEAM WINS !", "GG everyone !");
                         				}
                         				else{
                         					p.sendTitle(ChatColor.BLUE+"BLUE TEAM WINS !", "GG everyone !");
                         				}
                         				p.setGameMode(GameMode.SPECTATOR);
                     					p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                     					CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
                 						c.setCooldownLength(p, 3, hm4);
                 						c.tpToSpawn(p, hm4, (Location) getConfig().get("spawn"));
                         			}
                         		}
                         	}
                         }
                    }
                }
            }
    @EventHandler
    public void onHit(ProjectileHitEvent event) {
       
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().hasMetadata("Explosive")) {
                event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 6);  
            }
               
               
        }
    }
    @EventHandler
    public void onProjectileShoot(ProjectileLaunchEvent event) {
    Projectile projectile = event.getEntity();
    LivingEntity shooter = (LivingEntity) projectile.getShooter();
    if (shooter instanceof Player) {
     if(((God)(m.get(shooter))).getName().equals("Eole")){
         projectile.setMetadata("Explosive", (MetadataValue) new FixedMetadataValue(this, true));
     }
    }
    }
   
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player p = event.getPlayer();
        if(p.isOp()){
            if(event.getLine(1).toLowerCase().equals("godbattleinv")){
                event.setLine(0, "§cClique ici");
                event.setLine(1, "§6[§cKit§6]");
                event.setLine(2, "§cpour choisir");
                event.setLine(3, "§cton §6kit§c !");
            }
            else if(event.getLine(1).equalsIgnoreCase("{[godbattle]}")){
            	event.setLine(1, ChatColor.GREEN+"[JOIN]");
                siwo.put( Bukkit.getWorld(event.getLine(0)) ,(Sign)event.getBlock().getState());
                if(getConfig().get("signs")==null){
                	List<Location> ll = new LinkedList<Location>();
                	ll.add(event.getBlock().getLocation());
                	getConfig().set("signs" , ll);
                }
                else{
	                @SuppressWarnings("unchecked")
					List<Location> ll = (List<Location>) getConfig().get("signs");
	                ll.add(event.getBlock().getLocation());
	                getConfig().set("signs" , ll);
                }
                saveConfig();
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        try{
            Player player = (Player)event.getPlayer();
            Block block = event.getClickedBlock();
            if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN){
                if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    Sign sign = (Sign) block.getState();
                    String warp = "§6[§cKit§6]";
                    String gotoworld = ChatColor.GREEN+"[JOIN]";
                    if(sign.getLine(1).equals(warp)){
                        player.openInventory(godChoiceInv());
                    }
                    else if(sign.getLine(1).equals(gotoworld)){
                    	@SuppressWarnings("unused")
						World w = Bukkit.getServer().createWorld(new WorldCreator(sign.getLine(0)));
                    	player.teleport(Bukkit.getWorld(sign.getLine(0)).getSpawnLocation());
                    	player.setGameMode(GameMode.SPECTATOR);
                    	if(Bukkit.getWorld(sign.getLine(0)).getPlayers().size()==2){
                			CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
                			c.setCooldownLength(Bukkit.getWorld(sign.getLine(0)), 25, hm5);
                			c.wait(Bukkit.getWorld(sign.getLine(0)), hm5);
                		}
                    }
            }
        }
            
        }catch(Exception e){
           
        }
    }
    
    public God getGod(Player p){
    	return (God) m.get(p);
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
    	if(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK && e.getPlayer().getLocation().add(0d, -1d, 0d).getBlock().isBlockPowered() && m.get(e.getPlayer())!=null){
    		Vector v = e.getPlayer().getEyeLocation().getDirection();
    		v.setX(v.getX());
    		v.setZ(v.getZ());
    		v.multiply(1.1d);
    		v.setY(1.0d);
    		e.getPlayer().setVelocity(v);	
    		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);
    		Bukkit.getWorld(e.getPlayer().getWorld().getName()).spawnParticle(Particle.FLAME, e.getPlayer().getLocation(), 10);
    		if(l1.contains(e.getPlayer())){
    			l1.remove(e.getPlayer());
    		}
    		l1.add(e.getPlayer());
    		if(!( ( (Entity)e.getPlayer()).isOnGround() )){
	    		CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
				c.setCooldownLength(e.getPlayer(), 2 , hm1);
	    		c.PlayerJump(e.getPlayer() , hm1, l1);
    		}
    	}
    	else if(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK && e.getPlayer().getLocation().add(0d, -1d, 0d).getBlock().isBlockPowered() && m.get(e.getPlayer())!=null){
    		Vector v = new Vector(0,2.4,0);
    		e.getPlayer().setVelocity(v);
    		e.getPlayer().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, e.getPlayer().getLocation() , 150);
    	}
    }
    
    @EventHandler
    public void onBlockExplodeEvent(BlockExplodeEvent e){
    	FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(mapFileLocation));
    	if(e.getBlock().getWorld() == ((Location) getConfig().get("lobbypvp")).getWorld()){
    		e.setCancelled(true);
    	}
    	if(fg.get(e.getBlock().getWorld().getName())!=null){
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent e){
    	FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(mapFileLocation));
    	if(e.getEntity().getWorld() == ((Location) getConfig().get("lobbypvp")).getWorld()){
    		e.setCancelled(true);
    	}
    	if(fg.get(e.getEntity().getWorld().getName())!=null){
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent e){
    	if(m.get(e.getPlayer())!=null){
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent e){
    	if(m.get(e.getPlayer())!=null){
    		e.setCancelled(true);
    		e.getItem().remove();
    	}
    }
    
    @EventHandler
    public void onPlayerSwapHandItemsevent(PlayerSwapHandItemsEvent e){
    	if(m.get(e.getPlayer())!=null){
    		e.setCancelled(true);
    	}
    }
    
    public static String getMapFileConf(){
    	return mapFileLocation;
    }
    
    public static Sign getSignForWorld(World w){
    	return siwo.get(w);
    }
    
    public static Inventory getInventory(){
    	return inventory;
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e){
    	FileConfiguration fg = YamlConfiguration.loadConfiguration(new File(mapFileLocation));
    	if(fg.get(e.getPlayer().getWorld().getName())!=null){
    		if(fg.getString(e.getPlayer().getWorld().getName()+".gamemode").equals("TDM")){
        		Scoreboard s = e.getPlayer().getWorld().getPlayers().get(0).getScoreboard();
        		int red = 0;
        		int blue = 0;
        		for(Team t : s.getTeams()){
        			for(String str : t.getEntries()){
        				Player p2 = Bukkit.getPlayer(str);
        				if(!p2.isDead() && p2.getGameMode()!=GameMode.SPECTATOR && p2.isOnline()){
        					if(t.getName().equals("Red")){
        						red++;
        					}
        					else if(t.getName().equals("Blue")){
        						blue++;
        					}
        				}
        			}
        		}
        		if(blue == 0 || red == 0){
        			for(Player p : e.getPlayer().getWorld().getPlayers()){
        				if(blue==0){
        					p.sendTitle(ChatColor.RED+"RED TEAM WINS !", "GG everyone !");
        				}
        				else{
        					p.sendTitle(ChatColor.BLUE+"BLUE TEAM WINS !", "GG everyone !");
        				}
        				p.setGameMode(GameMode.SPECTATOR);
    					p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    					CoolDown2 c = new CoolDown2(Bukkit.getServer().getPluginManager().getPlugin("GodBattle"));
						c.setCooldownLength(p, 3, hm4);
						c.tpToSpawn(p, hm4, (Location) getConfig().get("spawn"));
        			}
        		}
        	}
    	}
    }
    
}