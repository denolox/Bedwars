package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final Bedwars plugin;

    public PlayerQuitListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Arena arena = plugin.getGameManager().getPlayerArena(event.getPlayer());
        if (arena != null) {
            plugin.getGameManager().leaveArena(event.getPlayer());
        }
    }
}
