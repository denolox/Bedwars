package me.clanmember.commands;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.ArenaType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedwarsCommand implements CommandExecutor {
    private final Bedwars plugin;

    public BedwarsCommand(Bedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                if (args.length < 2) {
                    handleAutoJoin(player, ArenaType.SOLO);
                } else {
                    handleJoin(player, args[1]);
                }
                break;

            case "solo":
                handleAutoJoin(player, ArenaType.SOLO);
                break;

            case "doubles":
                handleAutoJoin(player, ArenaType.DOUBLES);
                break;

            case "trio":
                handleAutoJoin(player, ArenaType.TRIO);
                break;

            case "squad":
                handleAutoJoin(player, ArenaType.SQUAD);
                break;

            case "leave":
                handleLeave(player);
                break;

            case "create":
                if (!player.hasPermission("bedwars.admin")) {
                    player.sendMessage(ChatColor.RED + "Недостаточно прав!");
                    return true;
                }
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Использование: /bw create <название> <тип>");
                    return true;
                }
                handleCreate(player, args[1], args[2]);
                break;

            case "list":
                handleList(player);
                break;

            default:
                sendHelp(player);
                break;
        }

        return true;
    }

    private void handleJoin(Player player, String arenaName) {
        Arena arena = plugin.getGameManager().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(ChatColor.RED + "Арена не найдена!");
            return;
        }

        plugin.getGameManager().joinArena(player, arena);
    }

    private void handleAutoJoin(Player player, ArenaType type) {
        Arena bestArena = plugin.getGameManager().findBestArena(type);
        if (bestArena == null) {
            player.sendMessage(ChatColor.RED + "Нет доступных арен для режима " + type.getDisplayName());
            return;
        }

        plugin.getGameManager().joinArena(player, bestArena);
    }

    private void handleLeave(Player player) {
        Arena arena = plugin.getGameManager().getPlayerArena(player);
        if (arena == null) {
            player.sendMessage(ChatColor.RED + "Вы не в игре!");
            return;
        }

        plugin.getGameManager().leaveArena(player);
        player.sendMessage(ChatColor.GREEN + "Вы покинули арену!");
    }

    private void handleCreate(Player player, String name, String typeStr) {
        ArenaType type;
        try {
            type = ArenaType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Неверный тип! Доступные: SOLO, DOUBLES, TRIO, SQUAD");
            return;
        }

        plugin.getGameManager().createArena(name, type);
        player.sendMessage(ChatColor.GREEN + "Арена создана!");
    }

    private void handleList(Player player) {
        player.sendMessage(ChatColor.GOLD + "Доступные арены:");
        for (String arenaName : plugin.getGameManager().getArenas().keySet()) {
            Arena arena = plugin.getGameManager().getArena(arenaName);
            player.sendMessage(ChatColor.YELLOW + "- " + arenaName + " (" + 
                             arena.getPlayers().size() + "/" + arena.getMaxPlayers() + ")");
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Bedwars Команды ===");
        player.sendMessage(ChatColor.YELLOW + "/bw solo - Быстрый вход Solo");
        player.sendMessage(ChatColor.YELLOW + "/bw doubles - Быстрый вход Doubles");
        player.sendMessage(ChatColor.YELLOW + "/bw trio - Быстрый вход 3v3v3v3");
        player.sendMessage(ChatColor.YELLOW + "/bw squad - Быстрый вход 4v4v4v4");
        player.sendMessage(ChatColor.YELLOW + "/bw join <арена> - Присоединиться к конкретной арене");
        player.sendMessage(ChatColor.YELLOW + "/bw leave - Покинуть игру");
        player.sendMessage(ChatColor.YELLOW + "/bw list - Список арен");
        if (player.hasPermission("bedwars.admin")) {
            player.sendMessage(ChatColor.YELLOW + "/bw create <название> <тип> - Создать арену");
        }
    }
}
