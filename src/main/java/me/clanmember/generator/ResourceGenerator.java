package me.clanmember.generator;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import me.clanmember.Bedwars;

public class ResourceGenerator {
    private final Location location;
    private final GeneratorType type;
    private int level;
    private BukkitRunnable task;

    public ResourceGenerator(Location location, GeneratorType type) {
        this.location = location;
        this.type = type;
        this.level = 1;
    }

    public void start() {
        if (task != null) {
            task.cancel();
        }

        int delay = calculateDelay();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                spawnItem();
            }
        };
        task.runTaskTimer(Bedwars.getInstance(), delay, delay);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void spawnItem() {
        ItemStack item = new ItemStack(type.getMaterial(), 1);
        location.getWorld().dropItem(location, item);
    }

    private int calculateDelay() {
        int baseDelay = type.getBaseDelay();
        return Math.max(baseDelay / level, 1);
    }

    public void upgrade() {
        if (level < 4) {
            level++;
            start();
        }
    }

    public Location getLocation() {
        return location;
    }

    public GeneratorType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }
}
