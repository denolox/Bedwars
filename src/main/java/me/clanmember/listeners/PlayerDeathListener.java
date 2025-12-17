package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import me.clanmember.team.BedwarsTeam;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener {
    private final Bedwars plugin;

    public PlayerDeathListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena == null || arena.getState() != GameState.RUNNING) {
            return;
        }

        event.setDeathMessage(null);
        event.getDrops().clear();

        BedwarsTeam team = arena.getPlayerTeam(player);
        if (team == null) return;

        Player killer = player.getKiller();
        String deathMessage;

        if (killer != null) {
            BedwarsTeam killerTeam = arena.getPlayerTeam(killer);
            deathMessage = team.getColor().getChatColor() + player.getName() + 
                         ChatColor.GRAY + " был убит игроком " +
                         (killerTeam != null ? killerTeam.getColor().getChatColor() : ChatColor.WHITE) + 
                         killer.getName();
        } else {
            deathMessage = team.getColor().getChatColor() + player.getName() + 
                         ChatColor.GRAY + " погиб";
        }

        for (Player p : arena.getPlayers()) {
            p.sendMessage(deathMessage);
        }

        if (team.isBedDestroyed()) {
            team.removePlayer(player);
            arena.removePlayer(player);
            arena.addSpectator(player);
            
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isDead()) {
                        return;
                    }
                    player.setGameMode(GameMode.SPECTATOR);
                    if (arena.getSpectatorSpawn() != null) {
                        player.teleport(arena.getSpectatorSpawn());
                    }
                    player.sendMessage(ChatColor.RED + "Вы выбыли! Ваша кровать была уничтожена.");
                }
            }.runTaskLater(plugin, 2L);
            
            plugin.getGameManager().checkWinCondition(arena);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isDead()) {
                        return;
                    }
                    if (team.getSpawnLocation() != null) {
                        player.teleport(team.getSpawnLocation());
                    }
                    player.setHealth(20.0);
                    player.setFoodLevel(20);
                    me.clanmember.items.GameItems.giveStartItems(player);
                }
            }.runTaskLater(plugin, 2L);
        }
    }
}
