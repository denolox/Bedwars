package me.clanmember.listeners;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.team.BedwarsTeam;
import me.clanmember.team.TeamColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TeamSelectorListener implements Listener {
    private final Bedwars plugin;

    public TeamSelectorListener(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        String title = event.getInventory().getTitle();
        if (!title.contains("Выбор команды")) return;

        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        
        if (clicked == null || clicked.getType() != Material.WOOL) return;

        Arena arena = plugin.getGameManager().getPlayerArena(player);
        if (arena == null) return;

        BedwarsTeam currentTeam = arena.getPlayerTeam(player);
        if (currentTeam != null) {
            currentTeam.removePlayer(player);
        }

        for (TeamColor color : TeamColor.values()) {
            if (clicked.getDurability() == color.getDyeColor().getWoolData()) {
                BedwarsTeam team = arena.getTeams().get(color);
                if (team != null && team.getSize() < arena.getType().getPlayersPerTeam()) {
                    team.addPlayer(player);
                    player.sendMessage(ChatColor.GREEN + "Вы выбрали команду " + color.getColoredName());
                    player.closeInventory();
                } else {
                    player.sendMessage(ChatColor.RED + "Команда заполнена!");
                }
                break;
            }
        }
    }
}
