package me.clanmember.game;

import me.clanmember.Bedwars;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaLoader {
    private final Bedwars plugin;

    public ArenaLoader(Bedwars plugin) {
        this.plugin = plugin;
    }

    public void startAutoLoad() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Arena arena : plugin.getGameManager().getArenas().values()) {
                    if (arena.getState() == GameState.WAITING && arena.getPlayers().isEmpty()) {
                        continue;
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
