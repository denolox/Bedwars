package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
import me.clanmember.team.BedwarsTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private final Bedwars plugin;

    public EntityDamageListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena == null) {
            return;
        }

        if (arena.getState() != GameState.RUNNING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        Arena arena = plugin.getGameManager().getPlayerArena(victim);
        if (arena == null || arena.getState() != GameState.RUNNING) {
            return;
        }

        BedwarsTeam victimTeam = arena.getPlayerTeam(victim);
        BedwarsTeam damagerTeam = arena.getPlayerTeam(damager);

        if (victimTeam != null && victimTeam == damagerTeam) {
            event.setCancelled(true);
        }
    }
}
