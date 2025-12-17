package me.clanmember.generator;

import org.bukkit.Material;

public enum GeneratorType {
    IRON(Material.IRON_INGOT, "Железо", 20),
    GOLD(Material.GOLD_INGOT, "Золото", 160),
    DIAMOND(Material.DIAMOND, "Алмаз", 600),
    EMERALD(Material.EMERALD, "Изумруд", 1200);

    private final Material material;
    private final String displayName;
    private final int baseDelay;

    GeneratorType(Material material, String displayName, int baseDelay) {
        this.material = material;
        this.displayName = displayName;
        this.baseDelay = baseDelay;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBaseDelay() {
        return baseDelay;
    }
}
