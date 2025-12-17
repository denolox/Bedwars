package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import me.clanmember.team.BedwarsTeam;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerRespawnListener implements Listener {
    private final Bedwars plugin;

    public PlayerRespawnListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena == null || arena.getState() != GameState.RUNNING) {
            return;
        }

        BedwarsTeam team = arena.getPlayerTeam(player);
        
        if (team == null) {
            if (arena.getSpectatorSpawn() != null) {
                event.setRespawnLocation(arena.getSpectatorSpawn());
            }
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }.runTaskLater(plugin, 1L);
            return;
        }

        if (team.getSpawnLocation() != null) {
            event.setRespawnLocation(team.getSpawnLocation());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setHealth(20.0);
                player.setFoodLevel(20);
                me.clanmember.items.GameItems.giveStartItems(player);
            }
        }.runTaskLater(plugin, 1L);
    }
}
