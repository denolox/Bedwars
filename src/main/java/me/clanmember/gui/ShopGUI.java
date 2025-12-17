package me.clanmember.gui;

import me.clanmember.Bedwars;
import me.clanmember.shop.ShopCategory;
import me.clanmember.shop.ShopItem;
import me.clanmember.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopGUI {
    private final Bedwars plugin;

    public ShopGUI(Bedwars plugin) {
        this.plugin = plugin;
    }

    public void openMainShop(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "Магазин предметов");

        inv.setItem(19, new ItemBuilder(Material.WOOL, 16)
                .setName(ChatColor.GREEN + "Блоки")
                .build());

        inv.setItem(20, new ItemBuilder(Material.STONE_SWORD)
                .setName(ChatColor.GREEN + "Оружие")
                .build());

        inv.setItem(21, new ItemBuilder(Material.CHAINMAIL_BOOTS)
                .setName(ChatColor.GREEN + "Броня")
                .build());

        inv.setItem(22, new ItemBuilder(Material.WOOD_PICKAXE)
                .setName(ChatColor.GREEN + "Инструменты")
                .build());

        inv.setItem(23, new ItemBuilder(Material.BOW)
                .setName(ChatColor.GREEN + "Луки")
                .build());

        inv.setItem(24, new ItemBuilder(Material.POTION)
                .setName(ChatColor.GREEN + "Зелья")
                .build());

        inv.setItem(25, new ItemBuilder(Material.TNT)
                .setName(ChatColor.GREEN + "Особое")
                .build());

        player.openInventory(inv);
    }

    public void openCategory(Player player, ShopCategory category) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + category.getDisplayName());

        List<ShopItem> items = plugin.getShopManager().getItems(category);
        int slot = 0;

        for (ShopItem shopItem : items) {
            if (slot >= 45) break;

            ItemStack display = shopItem.getItem().clone();
            ItemBuilder builder = new ItemBuilder(display.getType(), display.getAmount(), display.getDurability());
            builder.setName(ChatColor.GREEN + shopItem.getName());

            String currencyName = getCurrencyName(shopItem.getCurrency());
            builder.setLore(
                ChatColor.GRAY + "Цена: " + ChatColor.GOLD + shopItem.getPrice() + " " + currencyName,
                "",
                ChatColor.YELLOW + "Нажмите для покупки"
            );

            inv.setItem(slot, builder.build());
            slot++;
        }

        inv.setItem(49, new ItemBuilder(Material.ARROW)
                .setName(ChatColor.RED + "Назад")
                .build());

        player.openInventory(inv);
    }

    private String getCurrencyName(Material material) {
        switch (material) {
            case IRON_INGOT: return "железа";
            case GOLD_INGOT: return "золота";
            case DIAMOND: return "алмазов";
            case EMERALD: return "изумрудов";
            default: return material.name();
        }
    }
}
