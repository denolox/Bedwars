package me.clanmember.game;

import me.clanmember.Bedwars;
import me.clanmember.team.BedwarsTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private final Bedwars plugin;
    private final Map<String, Arena> arenas;
    private final Map<Player, Arena> playerArenas;

    public GameManager(Bedwars plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        this.playerArenas = new HashMap<>();
    }

    public void createArena(String name, ArenaType type) {
        Arena arena = new Arena(name, type);
        arenas.put(name, arena);
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public Arena getPlayerArena(Player player) {
        return playerArenas.get(player);
    }

    public void joinArena(Player player, Arena arena) {
        if (arena.isFull()) {
            player.sendMessage(ChatColor.RED + "Арена заполнена!");
            return;
        }

        if (arena.getState() != GameState.WAITING && arena.getState() != GameState.STARTING) {
            player.sendMessage(ChatColor.RED + "Игра уже началась!");
            return;
        }

        arena.addPlayer(player);
        playerArenas.put(player, arena);
        player.teleport(arena.getLobbySpawn());
        player.setGameMode(GameMode.SURVIVAL);
        
        me.clanmember.items.LobbyItems.giveLobbyItems(player);

        BedwarsTeam team = arena.getPlayerTeam(player);
        if (team != null) {
            broadcastToArena(arena, team.getColor().getChatColor() + player.getName() + 
                           ChatColor.GRAY + " присоединился к команде " + team.getColor().getColoredName());
        }

        if (arena.canStart() && arena.getState() == GameState.WAITING) {
            startCountdown(arena);
        }
    }

    public void leaveArena(Player player) {
        Arena arena = playerArenas.get(player);
        if (arena == null) return;

        arena.removePlayer(player);
        playerArenas.remove(player);

        if (arena.getState() == GameState.RUNNING) {
            checkWinCondition(arena);
        }

        if (arena.getPlayers().size() < arena.getMinPlayers() && arena.getState() == GameState.STARTING) {
            arena.setState(GameState.WAITING);
            arena.setCountdown(60);
            broadcastToArena(arena, ChatColor.RED + "Недостаточно игроков! Отсчёт остановлен.");
        }
    }

    private void startCountdown(Arena arena) {
        arena.setState(GameState.STARTING);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arena.getState() != GameState.STARTING) {
                    cancel();
                    return;
                }

                if (!arena.canStart()) {
                    arena.setState(GameState.WAITING);
                    arena.setCountdown(60);
                    broadcastToArena(arena, ChatColor.RED + "Недостаточно игроков!");
                    cancel();
                    return;
                }

                int countdown = arena.getCountdown();
                if (countdown <= 0) {
                    startGame(arena);
                    cancel();
                    return;
                }

                if (countdown <= 10 || countdown % 10 == 0) {
                    broadcastToArena(arena, ChatColor.YELLOW + "Игра начнётся через " + 
                                   ChatColor.RED + countdown + ChatColor.YELLOW + " сек.");
                }

                arena.decrementCountdown();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void startGame(Arena arena) {
        arena.setState(GameState.RUNNING);
        arena.startGenerators();
        broadcastToArena(arena, ChatColor.GREEN + "Игра началась!");

        for (Player player : arena.getPlayers()) {
            BedwarsTeam team = arena.getPlayerTeam(player);
            if (team != null && team.getSpawnLocation() != null) {
                player.teleport(team.getSpawnLocation());
                player.setGameMode(GameMode.SURVIVAL);
                giveStartItems(player, team);
            }
        }

        startGameEvents(arena);
    }

    private void giveStartItems(Player player, BedwarsTeam team) {
        player.getInventory().clear();
        me.clanmember.items.GameItems.giveStartItems(player);
    }

    private void startGameEvents(Arena arena) {
        new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                if (arena.getState() != GameState.RUNNING) {
                    cancel();
                    return;
                }

                time++;

                if (time == 120) {
                    broadcastToArena(arena, ChatColor.GOLD + ChatColor.BOLD.toString() + "АЛМАЗЫ II");
                    broadcastToArena(arena, ChatColor.YELLOW + "Алмазные генераторы улучшены до уровня II!");
                    upgradeGenerators(arena, me.clanmember.generator.GeneratorType.DIAMOND, 2);
                } else if (time == 360) {
                    broadcastToArena(arena, ChatColor.GOLD + ChatColor.BOLD.toString() + "ИЗУМРУДЫ II");
                    broadcastToArena(arena, ChatColor.YELLOW + "Изумрудные генераторы улучшены до уровня II!");
                    upgradeGenerators(arena, me.clanmember.generator.GeneratorType.EMERALD, 2);
                } else if (time == 600) {
                    broadcastToArena(arena, ChatColor.GOLD + ChatColor.BOLD.toString() + "АЛМАЗЫ III");
                    broadcastToArena(arena, ChatColor.YELLOW + "Алмазные генераторы улучшены до уровня III!");
                    upgradeGenerators(arena, me.clanmember.generator.GeneratorType.DIAMOND, 3);
                } else if (time == 900) {
                    broadcastToArena(arena, ChatColor.RED + ChatColor.BOLD.toString() + "ВНЕЗАПНАЯ СМЕРТЬ!");
                    broadcastToArena(arena, ChatColor.YELLOW + "Все кровати уничтожены!");
                    destroyAllBeds(arena);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void destroyAllBeds(Arena arena) {
        for (BedwarsTeam team : arena.getTeams().values()) {
            if (!team.isBedDestroyed()) {
                team.setBedDestroyed(true);
                if (team.getBedLocation() != null) {
                    team.getBedLocation().getBlock().setType(org.bukkit.Material.AIR);
                }
            }
        }
    }

    private void upgradeGenerators(Arena arena, me.clanmember.generator.GeneratorType type, int level) {
        for (me.clanmember.generator.ResourceGenerator gen : arena.getGenerators()) {
            if (gen.getType() == type) {
                gen.upgrade();
            }
        }
    }

    public void checkWinCondition(Arena arena) {
        if (arena.getState() != GameState.RUNNING) return;

        int aliveTeams = arena.getAliveTeams().size();
        if (aliveTeams <= 1) {
            endGame(arena);
        }
    }

    private void endGame(Arena arena) {
        arena.setState(GameState.ENDING);
        arena.stopGenerators();

        BedwarsTeam winner = arena.getAliveTeams().isEmpty() ? null : arena.getAliveTeams().get(0);
        if (winner != null) {
            broadcastToArena(arena, ChatColor.GOLD + "Победила команда " + winner.getColor().getColoredName() + "!");
        } else {
            broadcastToArena(arena, ChatColor.YELLOW + "Игра окончена!");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                resetArena(arena);
            }
        }.runTaskLater(plugin, 100L);
    }

    private void resetArena(Arena arena) {
        arena.setState(GameState.RESTARTING);

        for (Player player : arena.getPlayers()) {
            playerArenas.remove(player);
            player.teleport(arena.getLobbySpawn());
            player.getInventory().clear();
        }

        arena.getPlayers().clear();
        arena.getSpectators().clear();

        for (BedwarsTeam team : arena.getTeams().values()) {
            team.getPlayers().clear();
            team.setBedDestroyed(false);
            team.setSharpnessLevel(0);
            team.setProtectionLevel(0);
            team.setHasteEnabled(false);
            team.setTrapEnabled(false);
        }

        arena.setState(GameState.WAITING);
        arena.setCountdown(60);
    }

    private void broadcastToArena(Arena arena, String message) {
        for (Player player : arena.getPlayers()) {
            player.sendMessage(message);
        }
        for (Player spectator : arena.getSpectators()) {
            spectator.sendMessage(message);
        }
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }

    public Arena findBestArena(ArenaType type) {
        Arena bestArena = null;
        int lowestPlayers = Integer.MAX_VALUE;

        for (Arena arena : arenas.values()) {
            if (arena.getType() != type) continue;
            if (arena.getState() != GameState.WAITING && arena.getState() != GameState.STARTING) continue;
            if (arena.isFull()) continue;

            int playerCount = arena.getPlayers().size();
            if (playerCount < lowestPlayers) {
                lowestPlayers = playerCount;
                bestArena = arena;
            }
        }

        return bestArena;
    }
}
