package me.clanmember.utils;

import org.bukkit.ChatColor;

public class ColorUtils {
    
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String stripColor(String text) {
        return ChatColor.stripColor(text);
    }
}
