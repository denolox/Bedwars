package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.GameState;
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

public class InventoryClickListener implements Listener {
    private final Bedwars plugin;

    public InventoryClickListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Arena arena = plugin.getGameManager().getPlayerArena(player);

        if (arena == null) {
            return;
        }

        String title = event.getInventory().getTitle();
        
        if (title.contains("Магазин")) {
            event.setCancelled(true);
            handleShopClick(player, event);
        }
    }

    private void handleShopClick(Player player, InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }

        for (ShopCategory category : ShopCategory.values()) {
            List<ShopItem> items = plugin.getShopManager().getItems(category);
            for (ShopItem shopItem : items) {
                if (clicked.isSimilar(shopItem.getItem())) {
                    purchaseItem(player, shopItem);
                    return;
                }
            }
        }
    }

    private void purchaseItem(Player player, ShopItem item) {
        int count = countItems(player.getInventory(), item.getCurrency());
        
        if (count < item.getPrice()) {
            player.sendMessage(ChatColor.RED + "Недостаточно " + 
                             getCurrencyName(item.getCurrency()) + "!");
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
