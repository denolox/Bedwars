package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.gui.ShopGUI;
import me.clanmember.shop.ShopCategory;
import me.clanmember.shop.ShopItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopGUIListener implements Listener {
    private final Bedwars plugin;

    public ShopGUIListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getInventory().getTitle();

        if (title.contains("Магазин предметов")) {
            event.setCancelled(true);
            handleMainShop(player, event);
        } else if (title.contains("Блоки") || title.contains("Оружие") || title.contains("Броня") ||
                   title.contains("Инструменты") || title.contains("Луки") || title.contains("Зелья") ||
                   title.contains("Особое")) {
            event.setCancelled(true);
            handleCategoryShop(player, event, title);
        }
    }

    private void handleMainShop(Player player, InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        ShopGUI shopGUI = new ShopGUI(plugin);

        switch (clicked.getType()) {
            case WOOL:
                shopGUI.openCategory(player, ShopCategory.BLOCKS);
                break;
            case STONE_SWORD:
                shopGUI.openCategory(player, ShopCategory.WEAPONS);
                break;
            case CHAINMAIL_BOOTS:
                shopGUI.openCategory(player, ShopCategory.ARMOR);
                break;
            case WOOD_PICKAXE:
                shopGUI.openCategory(player, ShopCategory.TOOLS);
                break;
            case BOW:
                shopGUI.openCategory(player, ShopCategory.BOWS);
                break;
            case POTION:
                shopGUI.openCategory(player, ShopCategory.POTIONS);
                break;
            case TNT:
                shopGUI.openCategory(player, ShopCategory.SPECIAL);
                break;
        }
    }

    private void handleCategoryShop(Player player, InventoryClickEvent event, String title) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        if (clicked.getType() == Material.ARROW) {
            new ShopGUI(plugin).openMainShop(player);
            return;
        }

        ShopCategory category = getCategoryFromTitle(title);
        if (category == null) return;

        List<ShopItem> items = plugin.getShopManager().getItems(category);
        for (ShopItem shopItem : items) {
            if (clicked.getType() == shopItem.getItem().getType()) {
                purchaseItem(player, shopItem);
                break;
            }
        }
    }

    private ShopCategory getCategoryFromTitle(String title) {
        if (title.contains("Блоки")) return ShopCategory.BLOCKS;
        if (title.contains("Оружие")) return ShopCategory.WEAPONS;
        if (title.contains("Броня")) return ShopCategory.ARMOR;
        if (title.contains("Инструменты")) return ShopCategory.TOOLS;
        if (title.contains("Луки")) return ShopCategory.BOWS;
        if (title.contains("Зелья")) return ShopCategory.POTIONS;
        if (title.contains("Особое")) return ShopCategory.SPECIAL;
        return null;
    }

    private void purchaseItem(Player player, ShopItem item) {
        int count = countItems(player.getInventory(), item.getCurrency());

        if (count < item.getPrice()) {
            player.sendMessage(ChatColor.RED + "Недостаточно " + getCurrencyName(item.getCurrency()) + "!");
            return;
        }

        removeItems(player.getInventory(), item.getCurrency(), item.getPrice());
        player.getInventory().addItem(item.getItem());
        player.sendMessage(ChatColor.GREEN + "Куплено: " + item.getName());
    }

    private int countItems(Inventory inv, Material material) {
        int count = 0;
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }

    private void removeItems(Inventory inv, Material material, int amount) {
        int remaining = amount;
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getType() == material) {
                int itemAmount = item.getAmount();
                if (itemAmount <= remaining) {
                    remaining -= itemAmount;
                    inv.remove(item);
                } else {
                    item.setAmount(itemAmount - remaining);
                    remaining = 0;
                }
                if (remaining == 0) break;
            }
        }
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
