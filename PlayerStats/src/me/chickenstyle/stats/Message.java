package me.chickenstyle.stats;

import org.bukkit.ChatColor;

public enum Message {

    INVALID_USAGE(Color(getString("messages.invalidUsage"))),
	OFFLINE_PLAYER(Color(getString("messages.offlinePlayer")));
    private String error;

    Message(String error) {
        this.error = error;
    }

    public String getMSG() {
        return error;
    }
    
    private static String Color(String text) {
    	return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    private static String getString(String path) {
    	return Main.getInstance().getConfig().getString(path);
    }
}