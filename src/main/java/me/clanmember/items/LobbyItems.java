package me.clanmember.items;

import me.clanmember.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LobbyItems {
    
    public static void giveLobbyItems(Player player) {
        player.getInventory().clear();
        
        ItemStack teamSelector = new ItemBuilder(Material.BED)
                .setName(ChatColor.GREEN + "Выбор команды")
                .setLore(ChatColor.GRAY + "Нажмите для выбора команды")
                .build();
        
        ItemStack leaveItem = new ItemBuilder(Material.SLIME_BALL)
                .setName(ChatColor.RED + "Выйти из игры")
                .setLore(ChatColor.GRAY + "Нажмите для выхода")
                .build();
        
        player.getInventory().setItem(0, teamSelector);
        player.getInventory().setItem(8, leaveItem);
        player.updateInventory();
    }
}
