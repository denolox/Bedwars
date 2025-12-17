package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import me.clanmember.team.BedwarsTeam;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final Bedwars plugin;

    public BlockBreakListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena == null) {
            return;
        }

        if (arena.getState() != GameState.RUNNING) {
            event.setCancelled(true);
            return;
        }

        Block block = event.getBlock();
        
        if (block.getType() == Material.BED_BLOCK) {
            handleBedBreak(event, player, arena, block);
        }
    }

    private void handleBedBreak(BlockBreakEvent event, Player player, Arena arena, Block block) {
        BedwarsTeam breakerTeam = arena.getPlayerTeam(player);
        if (breakerTeam == null) {
            event.setCancelled(true);
            return;
        }

        for (BedwarsTeam team : arena.getTeams().values()) {
            if (team.getBedLocation() != null && 
                team.getBedLocation().distance(block.getLocation()) < 2) {
                
                if (team == breakerTeam) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "Нельзя ломать свою кровать!");
                    return;
                }

                event.setCancelled(true);
                team.setBedDestroyed(true);
                block.setType(Material.AIR);

                String message = ChatColor.BOLD + "УНИЧТОЖЕНИЕ КРОВАТИ! " + 
                               team.getColor().getColoredName() + ChatColor.GRAY + 
                               " больше не смогут возродиться!";

                for (Player p : arena.getPlayers()) {
                    p.sendMessage(message);
                }

                for (Player teamPlayer : team.getPlayers()) {
                    teamPlayer.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + 
                                         "Ваша кровать была уничтожена!");
                }

                return;
            }
        }
    }
}
