package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import me.clanmember.gui.ShopGUI;
import me.clanmember.gui.UpgradeGUI;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCInteractListener implements Listener {
    private final Bedwars plugin;

    public NPCInteractListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.VILLAGER) return;

        Player player = event.getPlayer();
        Arena arena = plugin.getGameManager().getPlayerArena(player);
        
        if (arena == null || arena.getState() != GameState.RUNNING) return;

        event.setCancelled(true);

        Villager villager = (Villager) event.getRightClicked();
        String name = villager.getCustomName();

        if (name == null) return;

        if (name.contains("Магазин")) {
            new ShopGUI(plugin).openMainShop(player);
        } else if (name.contains("Улучшения")) {
            new UpgradeGUI(plugin).open(player);
        }
    }
}
