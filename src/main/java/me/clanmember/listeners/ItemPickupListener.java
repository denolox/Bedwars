package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemPickupListener implements Listener {
    private final Bedwars plugin;

    public ItemPickupListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena == null) {
            return;
        }

        ItemStack item = event.getItem().getItemStack();
        Material type = item.getType();

        if (type == Material.IRON_INGOT || type == Material.GOLD_INGOT || 
            type == Material.DIAMOND || type == Material.EMERALD) {
            
            if (player.getInventory().firstEmpty() == -1) {
                if (!hasSpace(player, type)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean hasSpace(Player player, Material material) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material && item.getAmount() < 64) {
                return true;
            }
        }
        return false;
    }
}
