package me.clanmember.team;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public enum TeamColor {
    RED(ChatColor.RED, DyeColor.RED, Material.RED_ROSE, "Красная"),
    BLUE(ChatColor.BLUE, DyeColor.BLUE, Material.LAPIS_BLOCK, "Синяя"),
    GREEN(ChatColor.GREEN, DyeColor.GREEN, Material.EMERALD, "Зелёная"),
    YELLOW(ChatColor.YELLOW, DyeColor.YELLOW, Material.GOLD_INGOT, "Жёлтая"),
    AQUA(ChatColor.AQUA, DyeColor.CYAN, Material.DIAMOND, "Голубая"),
    WHITE(ChatColor.WHITE, DyeColor.WHITE, Material.IRON_INGOT, "Белая"),
    PINK(ChatColor.LIGHT_PURPLE, DyeColor.PINK, Material.CLAY_BRICK, "Розовая"),
    GRAY(ChatColor.GRAY, DyeColor.GRAY, Material.FLINT, "Серая");

    private final ChatColor chatColor;
    private final DyeColor dyeColor;
    private final Material material;
    private final String displayName;

    TeamColor(ChatColor chatColor, DyeColor dyeColor, Material material, String displayName) {
        this.chatColor = chatColor;
        this.dyeColor = dyeColor;
        this.material = material;
        this.displayName = displayName;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColoredName() {
        return chatColor + displayName;
    }
}
