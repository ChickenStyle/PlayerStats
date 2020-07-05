package me.chickenstyle.stats.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.chickenstyle.stats.Main;
import me.chickenstyle.stats.PlayerStats;

public class PlayerDeath implements Listener{
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			Player player = (Player) e.getEntity().getKiller();
			PlayerStats stats = Main.stats.get(player.getUniqueId());
			stats.addPlayerKilled();
			Main.stats.put(player.getUniqueId(), stats);
		}
	}
}
