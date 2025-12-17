package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import me.clanmember.team.BedwarsTeam;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

public class CompassTracker implements Listener {
    private final Bedwars plugin;

    public CompassTracker(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCompassUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getItem() == null || event.getItem().getType() != Material.COMPASS) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Arena arena = plugin.getGameManager().getPlayerArena(player);
        if (arena == null || arena.getState() != GameState.RUNNING) return;

        event.setCancelled(true);

        BedwarsTeam playerTeam = arena.getPlayerTeam(player);
        if (playerTeam == null) return;

        Player nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (BedwarsTeam team : arena.getTeams().values()) {
            if (team == playerTeam) continue;
            if (team.isEliminated()) continue;

            for (Player enemy : team.getPlayers()) {
                double distance = player.getLocation().distance(enemy.getLocation());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearest = enemy;
                }
            }
        }

        if (nearest != null) {
            player.setCompassTarget(nearest.getLocation());
            player.sendMessage(ChatColor.GREEN + "Трекер указывает на " + 
                             arena.getPlayerTeam(nearest).getColor().getChatColor() + nearest.getName());
        } else {
            for (BedwarsTeam team : arena.getTeams().values()) {
                if (team == playerTeam) continue;
                if (team.getBedLocation() != null && !team.isBedDestroyed()) {
                    player.setCompassTarget(team.getBedLocation());
                    player.sendMessage(ChatColor.GREEN + "Трекер указывает на кровать " + 
                                     team.getColor().getColoredName());
                    return;
                }
            }
            player.sendMessage(ChatColor.RED + "Нет доступных целей!");
        }
    }
}
