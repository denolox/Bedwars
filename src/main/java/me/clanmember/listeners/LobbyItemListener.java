package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import me.clanmember.gui.TeamSelectorGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class LobbyItemListener implements Listener {
    private final Bedwars plugin;

    public LobbyItemListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.getGameManager().getPlayerArena(player);
        
        if (arena == null) return;
        if (arena.getState() != GameState.WAITING && arena.getState() != GameState.STARTING) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.BED) {
                event.setCancelled(true);
                new TeamSelectorGUI(arena).open(player);
            } else if (item.getType() == Material.SLIME_BALL) {
                event.setCancelled(true);
                plugin.getGameManager().leaveArena(player);
            }
        }
    }
}
