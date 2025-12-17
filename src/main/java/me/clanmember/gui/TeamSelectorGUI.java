package me.clanmember.gui;

import me.clanmember.game.Arena;
import me.clanmember.team.BedwarsTeam;
import me.clanmember.team.TeamColor;
import me.clanmember.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TeamSelectorGUI {
    private final Arena arena;

    public TeamSelectorGUI(Arena arena) {
        this.arena = arena;
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Выбор команды");

        int slot = 10;
        for (TeamColor color : TeamColor.values()) {
            if (slot >= 18) break;
            
            BedwarsTeam team = arena.getTeams().get(color);
            if (team == null) continue;

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Игроков: " + ChatColor.WHITE + team.getSize());
            lore.add("");
            
            if (team.getSize() >= arena.getType().getPlayersPerTeam()) {
                lore.add(ChatColor.RED + "Команда заполнена!");
            } else {
                lore.add(ChatColor.GREEN + "Нажмите для выбора");
            }

            ItemStack item = new ItemBuilder(Material.WOOL, 1, color.getDyeColor().getWoolData())
                    .setName(color.getColoredName())
                    .setLore(lore)
                    .build();

            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }
}
