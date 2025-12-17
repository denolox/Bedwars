package me.clanmember.gui;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.team.BedwarsTeam;
import me.clanmember.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UpgradeGUI {
    private final Bedwars plugin;

    public UpgradeGUI(Bedwars plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Arena arena = plugin.getGameManager().getPlayerArena(player);
        if (arena == null) return;

        BedwarsTeam team = arena.getPlayerTeam(player);
        if (team == null) return;

        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Улучшения команды");

        ItemStack sharpness = new ItemBuilder(Material.IRON_SWORD)
                .setName(ChatColor.GREEN + "Заточенные мечи")
                .addEnchantment(Enchantment.DAMAGE_ALL, 1)
                .setLore(
                    ChatColor.GRAY + "Ваша команда получает",
                    ChatColor.GRAY + "Заточенность I на все мечи!",
                    "",
                    ChatColor.GRAY + "Уровень: " + ChatColor.YELLOW + team.getSharpnessLevel() + "/1",
                    "",
                    team.getSharpnessLevel() >= 1 ? ChatColor.GREEN + "КУПЛЕНО!" : ChatColor.YELLOW + "Цена: " + ChatColor.AQUA + "4 алмаза"
                )
                .build();

        ItemStack protection = new ItemBuilder(Material.IRON_CHESTPLATE)
                .setName(ChatColor.GREEN + "Усиленная броня")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setLore(
                    ChatColor.GRAY + "Ваша команда получает",
                    ChatColor.GRAY + "Защиту на всю броню!",
                    "",
                    ChatColor.GRAY + "Уровень: " + ChatColor.YELLOW + team.getProtectionLevel() + "/4",
                    "",
                    getProtectionPrice(team.getProtectionLevel())
                )
                .build();

        ItemStack haste = new ItemBuilder(Material.GOLD_PICKAXE)
                .setName(ChatColor.GREEN + "Спешка")
                .addEnchantment(Enchantment.DIG_SPEED, 2)
                .setLore(
                    ChatColor.GRAY + "Ваша команда получает",
                    ChatColor.GRAY + "Спешку II на базе!",
                    "",
                    team.isHasteEnabled() ? ChatColor.GREEN + "КУПЛЕНО!" : ChatColor.YELLOW + "Цена: " + ChatColor.AQUA + "4 алмаза"
                )
                .build();

        ItemStack trap = new ItemBuilder(Material.STRING)
                .setName(ChatColor.GREEN + "Ловушка")
                .setLore(
                    ChatColor.GRAY + "Враги получат эффекты",
                    ChatColor.GRAY + "при входе на вашу базу!",
                    "",
                    team.isTrapEnabled() ? ChatColor.GREEN + "АКТИВНА!" : ChatColor.YELLOW + "Цена: " + ChatColor.AQUA + "1 алмаз"
                )
                .build();

        inv.setItem(10, sharpness);
        inv.setItem(12, protection);
        inv.setItem(14, haste);
        inv.setItem(16, trap);

        player.openInventory(inv);
    }

    private String getProtectionPrice(int level) {
        switch (level) {
            case 0: return ChatColor.YELLOW + "Цена: " + ChatColor.AQUA + "2 алмаза";
            case 1: return ChatColor.YELLOW + "Цена: " + ChatColor.AQUA + "4 алмаза";
            case 2: return ChatColor.YELLOW + "Цена: " + ChatColor.AQUA + "8 алмазов";
            case 3: return ChatColor.YELLOW + "Цена: " + ChatColor.AQUA + "16 алмазов";
            default: return ChatColor.GREEN + "МАКСИМУМ!";
        }
    }
}
