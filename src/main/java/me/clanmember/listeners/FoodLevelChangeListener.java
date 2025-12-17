package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
    private final Bedwars plugin;

    public FoodLevelChangeListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena != null) {
            event.setCancelled(true);
            player.setFoodLevel(20);
        }
    }
}
