package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

public class PlayerInteractListener implements Listener {
    private final Bedwars plugin;

    public PlayerInteractListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena == null) {
            return;
        }

        if (arena.getState() != GameState.RUNNING) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || 
                event.getAction() == Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(true);
            }
            return;
        }

        if (event.getItem() != null && event.getItem().getType() == Material.TNT) {
            event.setCancelled(true);
        }
    }
}
