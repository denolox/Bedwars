package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.team.BedwarsTeam;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UpgradeGUIListener implements Listener {
    private final Bedwars plugin;

    public UpgradeGUIListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getInventory().getTitle();

        if (!title.contains("Улучшения команды")) return;

        event.setCancelled(true);

        Arena arena = plugin.getGameManager().getPlayerArena(player);
        if (arena == null) return;

        BedwarsTeam team = arena.getPlayerTeam(player);
        if (team == null) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        switch (clicked.getType()) {
            case IRON_SWORD:
                purchaseSharpness(player, team);
                break;
            case IRON_CHESTPLATE:
                purchaseProtection(player, team);
                break;
            case GOLD_PICKAXE:
                purchaseHaste(player, team);
                break;
            case STRING:
                purchaseTrap(player, team);
                break;
        }
    }

    private void purchaseSharpness(Player player, BedwarsTeam team) {
        if (team.getSharpnessLevel() >= 1) {
            player.sendMessage(ChatColor.RED + "Уже куплено!");
            return;
        }

        if (removeDiamonds(player, 4)) {
            team.setSharpnessLevel(1);
            broadcastToTeam(team, ChatColor.GREEN + "Заточенные мечи куплены!");
            player.closeInventory();
        } else {
            player.sendMessage(ChatColor.RED + "Недостаточно алмазов!");
        }
    }

    private void purchaseProtection(Player player, BedwarsTeam team) {
        int level = team.getProtectionLevel();
        if (level >= 4) {
            player.sendMessage(ChatColor.RED + "Максимальный уровень!");
            return;
        }

        int[] costs = {2, 4, 8, 16};
        if (removeDiamonds(player, costs[level])) {
            team.setProtectionLevel(level + 1);
            broadcastToTeam(team, ChatColor.GREEN + "Усиленная броня " + (level + 1) + " куплена!");
            player.closeInventory();
        } else {
            player.sendMessage(ChatColor.RED + "Недостаточно алмазов!");
        }
    }

    private void purchaseHaste(Player player, BedwarsTeam team) {
        if (team.isHasteEnabled()) {
            player.sendMessage(ChatColor.RED + "Уже куплено!");
            return;
        }

        if (removeDiamonds(player, 4)) {
            team.setHasteEnabled(true);
            broadcastToTeam(team, ChatColor.GREEN + "Спешка куплена!");
            player.closeInventory();
        } else {
            player.sendMessage(ChatColor.RED + "Недостаточно алмазов!");
        }
    }

    private void purchaseTrap(Player player, BedwarsTeam team) {
        if (team.isTrapEnabled()) {
            player.sendMessage(ChatColor.RED + "Уже активна!");
            return;
        }

        if (removeDiamonds(player, 1)) {
            team.setTrapEnabled(true);
            broadcastToTeam(team, ChatColor.GREEN + "Ловушка установлена!");
            player.closeInventory();
        } else {
            player.sendMessage(ChatColor.RED + "Недостаточно алмазов!");
        }
    }

    private boolean removeDiamonds(Player player, int amount) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.DIAMOND) {
                count += item.getAmount();
            }
        }

        if (count < amount) return false;

        int remaining = amount;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.DIAMOND) {
                int itemAmount = item.getAmount();
                if (itemAmount <= remaining) {
                    remaining -= itemAmount;
                    player.getInventory().remove(item);
                } else {
                    item.setAmount(itemAmount - remaining);
                    remaining = 0;
                }
                if (remaining == 0) break;
            }
        }

        return true;
    }

    private void broadcastToTeam(BedwarsTeam team, String message) {
        for (Player p : team.getPlayers()) {
            p.sendMessage(message);
        }
    }
}
