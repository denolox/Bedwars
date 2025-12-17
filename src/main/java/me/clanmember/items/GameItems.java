package me.clanmember.items;

import me.clanmember.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GameItems {
    
    public static void giveStartItems(Player player) {
        player.getInventory().clear();
        
        ItemStack sword = new ItemBuilder(Material.WOOD_SWORD)
                .setUnbreakable(true)
                .build();
        
        ItemStack compass = new ItemBuilder(Material.COMPASS)
                .setName(ChatColor.GREEN + "Трекер")
                .setLore(ChatColor.GRAY + "Найти ближайшего врага")
                .build();
        
        player.getInventory().setItem(0, sword);
        player.getInventory().setItem(8, compass);
    }
    
    public static void upgradePickaxe(Player player, int tier) {
        ItemStack pickaxe = null;
        
        switch (tier) {
            case 1:
                pickaxe = new ItemBuilder(Material.WOOD_PICKAXE)
                        .addEnchantment(Enchantment.DIG_SPEED, 1)
                        .setUnbreakable(true)
                        .build();
                break;
            case 2:
                pickaxe = new ItemBuilder(Material.IRON_PICKAXE)
                        .addEnchantment(Enchantment.DIG_SPEED, 2)
                        .setUnbreakable(true)
                        .build();
                break;
            case 3:
                pickaxe = new ItemBuilder(Material.GOLD_PICKAXE)
                        .addEnchantment(Enchantment.DIG_SPEED, 3)
                        .setUnbreakable(true)
                        .build();
                break;
            case 4:
                pickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE)
                        .addEnchantment(Enchantment.DIG_SPEED, 3)
                        .setUnbreakable(true)
                        .build();
                break;
        }
        
        if (pickaxe != null) {
            removePickaxe(player);
            player.getInventory().addItem(pickaxe);
        }
    }
    
    public static void upgradeAxe(Player player, int tier) {
        ItemStack axe = null;
        
        switch (tier) {
            case 1:
                axe = new ItemBuilder(Material.WOOD_AXE)
                        .addEnchantment(Enchantment.DIG_SPEED, 1)
                        .setUnbreakable(true)
                        .build();
                break;
            case 2:
                axe = new ItemBuilder(Material.STONE_AXE)
                        .addEnchantment(Enchantment.DIG_SPEED, 1)
                        .setUnbreakable(true)
                        .build();
                break;
            case 3:
                axe = new ItemBuilder(Material.IRON_AXE)
                        .addEnchantment(Enchantment.DIG_SPEED, 2)
                        .setUnbreakable(true)
                        .build();
                break;
            case 4:
                axe = new ItemBuilder(Material.DIAMOND_AXE)
                        .addEnchantment(Enchantment.DIG_SPEED, 3)
                        .setUnbreakable(true)
                        .build();
                break;
        }
        
        if (axe != null) {
            removeAxe(player);
            player.getInventory().addItem(axe);
        }
    }
    
    public static void upgradeSword(Player player, Material type) {
        removeSword(player);
        
        ItemStack sword = new ItemBuilder(type)
                .setUnbreakable(true)
                .build();
        
        player.getInventory().addItem(sword);
    }
    
    public static void upgradeArmor(Player player, Material boots, Material leggings) {
        player.getInventory().setBoots(new ItemBuilder(boots).setUnbreakable(true).build());
        player.getInventory().setLeggings(new ItemBuilder(leggings).setUnbreakable(true).build());
    }
    
    private static void removePickaxe(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && (item.getType().name().contains("PICKAXE"))) {
                player.getInventory().remove(item);
            }
        }
    }
    
    private static void removeAxe(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && (item.getType().name().contains("_AXE"))) {
                player.getInventory().remove(item);
            }
        }
    }
    
    private static void removeSword(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && (item.getType().name().contains("SWORD"))) {
                player.getInventory().remove(item);
            }
        }
    }
}
