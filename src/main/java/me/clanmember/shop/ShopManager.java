package me.clanmember.shop;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopManager {
    private final Map<ShopCategory, List<ShopItem>> items;

    public ShopManager() {
        this.items = new HashMap<>();
        loadDefaultItems();
    }

    private void loadDefaultItems() {
        addItem(new ShopItem("Шерсть", new ItemStack(Material.WOOL, 16), Material.IRON_INGOT, 4, ShopCategory.BLOCKS));
        addItem(new ShopItem("Твёрдая глина", new ItemStack(Material.STAINED_CLAY, 16), Material.IRON_INGOT, 12, ShopCategory.BLOCKS));
        addItem(new ShopItem("Взрывоустойчивое стекло", new ItemStack(Material.GLASS, 4), Material.IRON_INGOT, 12, ShopCategory.BLOCKS));
        addItem(new ShopItem("Эндер-камень", new ItemStack(Material.ENDER_STONE, 12), Material.IRON_INGOT, 24, ShopCategory.BLOCKS));
        addItem(new ShopItem("Лестница", new ItemStack(Material.LADDER, 16), Material.IRON_INGOT, 4, ShopCategory.BLOCKS));
        addItem(new ShopItem("Дерево", new ItemStack(Material.WOOD, 16), Material.GOLD_INGOT, 4, ShopCategory.BLOCKS));
        addItem(new ShopItem("Обсидиан", new ItemStack(Material.OBSIDIAN, 4), Material.EMERALD, 4, ShopCategory.BLOCKS));

        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        addItem(new ShopItem("Каменный меч", stoneSword, Material.IRON_INGOT, 10, ShopCategory.WEAPONS));

        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        addItem(new ShopItem("Железный меч", ironSword, Material.GOLD_INGOT, 7, ShopCategory.WEAPONS));

        ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
        addItem(new ShopItem("Алмазный меч", diamondSword, Material.EMERALD, 4, ShopCategory.WEAPONS));

        ItemStack stick = new ItemStack(Material.STICK);
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        addItem(new ShopItem("Палка отбрасывания", stick, Material.GOLD_INGOT, 5, ShopCategory.WEAPONS));

        addItem(new ShopItem("Кольчужная броня", new ItemStack(Material.CHAINMAIL_BOOTS), Material.IRON_INGOT, 40, ShopCategory.ARMOR));
        addItem(new ShopItem("Железная броня", new ItemStack(Material.IRON_BOOTS), Material.GOLD_INGOT, 12, ShopCategory.ARMOR));
        addItem(new ShopItem("Алмазная броня", new ItemStack(Material.DIAMOND_BOOTS), Material.EMERALD, 6, ShopCategory.ARMOR));

        addItem(new ShopItem("Кирка I", new ItemStack(Material.WOOD_PICKAXE), Material.IRON_INGOT, 10, ShopCategory.TOOLS));
        addItem(new ShopItem("Кирка II", new ItemStack(Material.IRON_PICKAXE), Material.IRON_INGOT, 10, ShopCategory.TOOLS));
        addItem(new ShopItem("Кирка III", new ItemStack(Material.GOLD_PICKAXE), Material.GOLD_INGOT, 3, ShopCategory.TOOLS));
        addItem(new ShopItem("Топор I", new ItemStack(Material.WOOD_AXE), Material.IRON_INGOT, 10, ShopCategory.TOOLS));
        addItem(new ShopItem("Топор II", new ItemStack(Material.STONE_AXE), Material.IRON_INGOT, 10, ShopCategory.TOOLS));
        addItem(new ShopItem("Ножницы", new ItemStack(Material.SHEARS), Material.IRON_INGOT, 20, ShopCategory.TOOLS));

        addItem(new ShopItem("Лук", new ItemStack(Material.BOW), Material.GOLD_INGOT, 12, ShopCategory.BOWS));
        addItem(new ShopItem("Лук (Сила)", new ItemStack(Material.BOW), Material.GOLD_INGOT, 20, ShopCategory.BOWS));
        addItem(new ShopItem("Лук (Сила, Откидывание)", new ItemStack(Material.BOW), Material.EMERALD, 6, ShopCategory.BOWS));
        addItem(new ShopItem("Стрелы", new ItemStack(Material.ARROW, 8), Material.GOLD_INGOT, 2, ShopCategory.BOWS));

        addItem(new ShopItem("Скорость II (45 сек)", new ItemStack(Material.POTION, 1, (short) 8226), Material.EMERALD, 1, ShopCategory.POTIONS));
        addItem(new ShopItem("Прыгучесть V (45 сек)", new ItemStack(Material.POTION, 1, (short) 8235), Material.EMERALD, 1, ShopCategory.POTIONS));
        addItem(new ShopItem("Невидимость (30 сек)", new ItemStack(Material.POTION, 1, (short) 8238), Material.EMERALD, 2, ShopCategory.POTIONS));

        addItem(new ShopItem("Золотое яблоко", new ItemStack(Material.GOLDEN_APPLE), Material.GOLD_INGOT, 3, ShopCategory.SPECIAL));
        addItem(new ShopItem("Огненный шар", new ItemStack(Material.FIREBALL), Material.IRON_INGOT, 40, ShopCategory.SPECIAL));
        addItem(new ShopItem("TNT", new ItemStack(Material.TNT), Material.GOLD_INGOT, 4, ShopCategory.SPECIAL));
        addItem(new ShopItem("Эндер-жемчуг", new ItemStack(Material.ENDER_PEARL), Material.EMERALD, 4, ShopCategory.SPECIAL));
        addItem(new ShopItem("Платформа", new ItemStack(Material.BLAZE_ROD), Material.EMERALD, 20, ShopCategory.SPECIAL));
        addItem(new ShopItem("Губка", new ItemStack(Material.SPONGE, 4), Material.GOLD_INGOT, 6, ShopCategory.SPECIAL));
    }

    private void addItem(ShopItem item) {
        items.computeIfAbsent(item.getCategory(), k -> new ArrayList<>()).add(item);
    }

    public List<ShopItem> getItems(ShopCategory category) {
        return items.getOrDefault(category, new ArrayList<>());
    }

    public Map<ShopCategory, List<ShopItem>> getAllItems() {
        return items;
    }
}
