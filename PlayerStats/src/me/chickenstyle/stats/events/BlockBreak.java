package me.chickenstyle.stats.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.chickenstyle.stats.Main;
import me.chickenstyle.stats.PlayerStats;

public class BlockBreak implements Listener{
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		PlayerStats stats = Main.stats.get(player.getUniqueId());
		stats.addBlocksMined();
		Main.stats.put(player.getUniqueId(), stats);
		
	}
}
