package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class ExplosionListener implements Listener {
    private final Bedwars plugin;

    public ExplosionListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            Material type = block.getType();
            
            if (type == Material.ENDER_STONE || type == Material.OBSIDIAN || 
                type == Material.BEDROCK || type == Material.BED_BLOCK) {
                iterator.remove();
            }
        }
    }
}
