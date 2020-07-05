package me.chickenstyle.stats;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;

public class PlayerStatsCommand implements CommandExecutor {
	
	public static HashMap<UUID,ItemStack> itemInHand = new HashMap<UUID,ItemStack>();
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				if (Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
					Player target = Bukkit.getPlayer(args[0]);
					openBookInHand(player, target);
				} else {
					player.sendMessage(Message.OFFLINE_PLAYER.getMSG());
				}
			} else {
				player.sendMessage(Message.INVALID_USAGE.getMSG());
			}
			
			
		} else {
			sender.sendMessage(ChatColor.RED + "You cannot use this command in the console!");
		}
		return false;
	}
	
	

	public void openBookInHand(Player player,Player target) {
		//Data
    	HashMap<UUID,PlayerStats> stats = Main.stats;
    	int blocksMined = stats.get(target.getUniqueId()).getBlocksMined();
    	int playersKilled = stats.get(target.getUniqueId()).getPlayersKilled();
    	String timePlayed = (target.getStatistic(Statistic.PLAY_ONE_TICK)/72000) + "h";


    	// Creating and setting the book!
    	ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    	BookMeta meta = (BookMeta) book.getItemMeta();
    	meta.setTitle("Blank");
    	meta.setAuthor("Chickenstyle");
    	meta.addPage( 
    			Utils.color("&3\n" + target.getName() + " Stats:\n"
    					+   "\n"
    					+   "Blocks Mined: &6" + blocksMined
    					+   "\n&3Players Killed: &6" + playersKilled 
    					+   "\n&3Time Played: &6" + timePlayed
    					+   "\n&3Balance: &6" + (int) Main.econ.getBalance(target) + "$"));
    	book.setItemMeta(meta);
    	itemInHand.put(player.getUniqueId(), player.getItemInHand());
    	player.getInventory().setItemInHand(book);

    	//NMS stuff
    	ByteBuf buf = Unpooled.buffer(256);
    	buf.setByte(0, (byte)0);
    	buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

    	
        // Give the item back
        new BukkitRunnable() {
           
            @Override
            public void run() {
            	player.getInventory().setItemInHand(itemInHand.get(player.getUniqueId()));
            	itemInHand.remove(player.getUniqueId());
            }
        }.runTaskLater(Main.getInstance(), 5L);
    	

    }
}
