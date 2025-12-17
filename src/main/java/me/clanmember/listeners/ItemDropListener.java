package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener {
    private final Bedwars plugin;

    public ItemDropListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena == null) {
            return;
        }

        if (arena.getState() != GameState.RUNNING) {
            event.setCancelled(true);
            return;
        }

        Material type = event.getItemDrop().getItemStack().getType();
        if (type == Material.WOOD_SWORD || type == Material.STONE_SWORD || 
            type == Material.IRON_SWORD || type == Material.DIAMOND_SWORD ||
            type == Material.WOOD_PICKAXE || type == Material.IRON_PICKAXE ||
            type == Material.GOLD_PICKAXE || type == Material.DIAMOND_PICKAXE ||
            type == Material.WOOD_AXE || type == Material.STONE_AXE ||
            type == Material.SHEARS || type == Material.COMPASS) {
            event.setCancelled(true);
        }
    }
}
