package me.clanmember.commands;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.generator.GeneratorType;
import me.clanmember.team.TeamColor;
import me.clanmember.team.BedwarsTeam;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {
    private final Bedwars plugin;

    public SetupCommand(Bedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.setup")) {
            player.sendMessage(ChatColor.RED + "Недостаточно прав!");
            return true;
        }

        if (args.length < 2) {
            sendHelp(player);
            return true;
        }

        String arenaName = args[0];
        Arena arena = plugin.getGameManager().getArena(arenaName);

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "Арена не найдена!");
            return true;
        }

        String action = args[1].toLowerCase();
        Location loc = player.getLocation();

        switch (action) {
            case "lobby":
                arena.setLobbySpawn(loc);
                player.sendMessage(ChatColor.GREEN + "Спавн лобби установлен!");
                break;

            case "spectator":
                arena.setSpectatorSpawn(loc);
                player.sendMessage(ChatColor.GREEN + "Спавн наблюдателей установлен!");
                break;

            case "teamspawn":
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Использование: /setup <арена> teamspawn <цвет>");
                    return true;
                }
                handleTeamSpawn(player, arena, args[2], loc);
                break;

            case "bed":
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Использование: /setup <арена> bed <цвет>");
                    return true;
                }
                handleBed(player, arena, args[2], loc);
                break;

            case "generator":
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Использование: /setup <арена> generator <тип>");
                    return true;
                }
                handleGenerator(player, arena, args[2], loc);
                break;

            case "minplayers":
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Использование: /setup <арена> minplayers <число>");
                    return true;
                }
                try {
                    int min = Integer.parseInt(args[2]);
                    arena.setMinPlayers(min);
                    player.sendMessage(ChatColor.GREEN + "Минимум игроков: " + min);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Неверное число!");
                }
                break;

            case "maxplayers":
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Использование: /setup <арена> maxplayers <число>");
                    return true;
                }
                try {
                    int max = Integer.parseInt(args[2]);
                    arena.setMaxPlayers(max);
                    player.sendMessage(ChatColor.GREEN + "Максимум игроков: " + max);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Неверное число!");
                }
                break;

            case "save":
                plugin.getArenaManager().saveArena(arena);
                player.sendMessage(ChatColor.GREEN + "Арена сохранена!");
                break;

            default:
                sendHelp(player);
                break;
        }

        return true;
    }

    private void handleTeamSpawn(Player player, Arena arena, String colorStr, Location loc) {
        try {
            TeamColor color = TeamColor.valueOf(colorStr.toUpperCase());
            BedwarsTeam team = arena.getTeams().get(color);
            if (team != null) {
                team.setSpawnLocation(loc);
                player.sendMessage(ChatColor.GREEN + "Спавн команды " + color.getColoredName() + " установлен!");
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Неверный цвет! Доступные: RED, BLUE, GREEN, YELLOW, AQUA, WHITE, PINK, GRAY");
        }
    }

    private void handleBed(Player player, Arena arena, String colorStr, Location loc) {
        try {
            TeamColor color = TeamColor.valueOf(colorStr.toUpperCase());
            BedwarsTeam team = arena.getTeams().get(color);
            if (team != null) {
                team.setBedLocation(loc);
                player.sendMessage(ChatColor.GREEN + "Кровать команды " + color.getColoredName() + " установлена!");
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Неверный цвет!");
        }
    }

    private void handleGenerator(Player player, Arena arena, String typeStr, Location loc) {
        try {
            GeneratorType type = GeneratorType.valueOf(typeStr.toUpperCase());
            arena.addGenerator(loc, type);
            player.sendMessage(ChatColor.GREEN + "Генератор " + type.getDisplayName() + " добавлен!");
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Неверный тип! Доступные: IRON, GOLD, DIAMOND, EMERALD");
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Настройка арены ===");
        player.sendMessage(ChatColor.YELLOW + "/setup <арена> lobby - Установить спавн лобби");
        player.sendMessage(ChatColor.YELLOW + "/setup <арена> spectator - Установить спавн наблюдателей");
        player.sendMessage(ChatColor.YELLOW + "/setup <арена> teamspawn <цвет> - Установить спавн команды");
        player.sendMessage(ChatColor.YELLOW + "/setup <арена> bed <цвет> - Установить кровать команды");
        player.sendMessage(ChatColor.YELLOW + "/setup <арена> generator <тип> - Добавить генератор");
        player.sendMessage(ChatColor.YELLOW + "/setup <арена> minplayers <число> - Минимум игроков");
        player.sendMessage(ChatColor.YELLOW + "/setup <арена> maxplayers <число> - Максимум игроков");
        player.sendMessage(ChatColor.YELLOW + "/setup <арена> save - Сохранить арену");
    }
}
