package me.chickenstyle.stats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.chickenstyle.stats.events.BlockBreak;
import me.chickenstyle.stats.events.PlayerDeath;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Main extends JavaPlugin implements Listener{

	public static Economy econ;

	private static Main instance;
	
	public static HashMap<UUID,PlayerStats> stats;
	
    public void onEnable() {
        
        //Configs
		this.getConfig().options().copyDefaults();
		saveDefaultConfig();
    	
        if (!setupEconomy()) {
            getPluginLoader().disablePlugin(this);
            System.out.println("PlayerStats has been disabled because you dont have vault plugin!");
            return;
        }
        
        

        
        instance = this;
        
        //register events
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
		
		try {
			Gson gson = new Gson();
			Reader reader = Files.newBufferedReader(Paths.get(getDataFolder() + "/PlayerStats.json"));
			stats = gson.fromJson(reader, new TypeToken<HashMap<UUID, PlayerStats>>() {
			}.getType());
			reader.close();
			System.out.println("PlayerStats - Data has been loaded!");
		} catch (IOException e) {
			stats = new HashMap<>();
		}
		
        getCommand("playerstats").setExecutor(new PlayerStatsCommand());
    }
    
    public void onDisable() {
    	if (!PlayerStatsCommand.itemInHand.isEmpty()) {
        	for (UUID uuid:PlayerStatsCommand.itemInHand.keySet()) {
        		Player player = Bukkit.getPlayer(uuid);
        		player.getInventory().setItemInHand(PlayerStatsCommand.itemInHand.get(uuid));
        	}
    	}
    	
		try {
			Writer writer = new FileWriter(getDataFolder() + "/PlayerStats.json");
			new Gson().toJson(stats, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
    	if (!stats.containsKey(e.getPlayer().getUniqueId())) {
    		stats.put(e.getPlayer().getUniqueId(), new PlayerStats(0,0));
    	}
    }
    
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChatEvent(AsyncPlayerChatEvent e) {
    	e.setCancelled(true);
    	Player player = e.getPlayer();
    	String prefix = PermissionsEx.getUser(player).getPrefix();
    	TextComponent message = new TextComponent(Utils.color(prefix) + " ");
    	
    	TextComponent name = new TextComponent(Utils.color(e.getPlayer().getDisplayName() + ": "));
    	name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(Utils.color("&7Click to see " + player.getName() + " stats!")).create()));
    	name.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playerstats " + player.getName()));
    	
    	TextComponent text = new TextComponent(e.getMessage());
    	
    	message.addExtra(name);
    	message.addExtra(text);

    	for (Player online:getServer().getOnlinePlayers()) {
    		online.spigot().sendMessage(message);
    	}
    }
    
    //Setups and Stuff
	
	

    
    public static Main getInstance() {
    	return instance;
    }
    
    private boolean setupEconomy() {
    	if (getServer().getPluginManager().getPlugin("Vault") == null) {
    	return false;
    	}
    	RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    	if (rsp == null) {
    	return false;
    	}
    	econ = rsp.getProvider();
    	return econ != null;
    	}
}
