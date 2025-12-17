package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final Bedwars plugin;

    public BlockPlaceListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Arena arena = plugin.getGameManager().getPlayerArena(event.getPlayer());

        if (arena == null) {
            return;
        }

        if (arena.getState() != GameState.RUNNING) {
            event.setCancelled(true);
        }
    }
}
