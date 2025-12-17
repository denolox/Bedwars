package me.clanmember.arena;

import me.clanmember.Bedwars;
import me.clanmember.game.Arena;
import me.clanmember.game.ArenaType;
import me.clanmember.generator.GeneratorType;
import me.clanmember.team.BedwarsTeam;
import me.clanmember.team.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ArenaManager {
    private final Bedwars plugin;
    private final File arenasFolder;

    public ArenaManager(Bedwars plugin) {
        this.plugin = plugin;
        this.arenasFolder = new File(plugin.getDataFolder(), "arenas");
        if (!arenasFolder.exists()) {
            arenasFolder.mkdirs();
        }
    }

    public void saveArena(Arena arena) {
        File file = new File(arenasFolder, arena.getName() + ".yml");
        FileConfiguration config = new YamlConfiguration();

        config.set("name", arena.getName());
        config.set("type", arena.getType().name());
        config.set("min-players", arena.getMinPlayers());
        config.set("max-players", arena.getMaxPlayers());

        if (arena.getLobbySpawn() != null) {
            saveLocation(config, "lobby-spawn", arena.getLobbySpawn());
        }

        if (arena.getSpectatorSpawn() != null) {
            saveLocation(config, "spectator-spawn", arena.getSpectatorSpawn());
        }

        for (TeamColor color : arena.getTeams().keySet()) {
            BedwarsTeam team = arena.getTeams().get(color);
            String path = "teams." + color.name().toLowerCase();

            if (team.getSpawnLocation() != null) {
                saveLocation(config, path + ".spawn", team.getSpawnLocation());
            }

            if (team.getBedLocation() != null) {
                saveLocation(config, path + ".bed", team.getBedLocation());
            }
        }

        int genIndex = 0;
        for (me.clanmember.generator.ResourceGenerator gen : arena.getGenerators()) {
            String path = "generators." + genIndex;
            config.set(path + ".type", gen.getType().name());
            saveLocation(config, path + ".location", gen.getLocation());
            genIndex++;
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Не удалось сохранить арену " + arena.getName());
        }
    }

    public void loadArenas() {
        File[] files = arenasFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            loadArena(file);
        }
    }

    private void loadArena(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String name = config.getString("name");
        ArenaType type = ArenaType.valueOf(config.getString("type", "SOLO"));

        plugin.getGameManager().createArena(name, type);
        Arena arena = plugin.getGameManager().getArena(name);

        arena.setMinPlayers(config.getInt("min-players", 2));
        arena.setMaxPlayers(config.getInt("max-players", 16));

        if (config.contains("lobby-spawn")) {
            arena.setLobbySpawn(loadLocation(config, "lobby-spawn"));
        }

        if (config.contains("spectator-spawn")) {
            arena.setSpectatorSpawn(loadLocation(config, "spectator-spawn"));
        }

        ConfigurationSection teamsSection = config.getConfigurationSection("teams");
        if (teamsSection != null) {
            for (String colorStr : teamsSection.getKeys(false)) {
                try {
                    TeamColor color = TeamColor.valueOf(colorStr.toUpperCase());
                    BedwarsTeam team = arena.getTeams().get(color);
                    String path = "teams." + colorStr;

                    if (config.contains(path + ".spawn")) {
                        team.setSpawnLocation(loadLocation(config, path + ".spawn"));
                    }

                    if (config.contains(path + ".bed")) {
                        team.setBedLocation(loadLocation(config, path + ".bed"));
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Неверный цвет команды: " + colorStr);
                }
            }
        }

        ConfigurationSection gensSection = config.getConfigurationSection("generators");
        if (gensSection != null) {
            for (String key : gensSection.getKeys(false)) {
                String path = "generators." + key;
                try {
                    GeneratorType genType = GeneratorType.valueOf(config.getString(path + ".type"));
                    Location loc = loadLocation(config, path + ".location");
                    arena.addGenerator(loc, genType);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Неверный тип генератора");
                }
            }
        }
    }

    private void saveLocation(FileConfiguration config, String path, Location loc) {
        config.set(path + ".world", loc.getWorld().getName());
        config.set(path + ".x", loc.getX());
        config.set(path + ".y", loc.getY());
        config.set(path + ".z", loc.getZ());
        config.set(path + ".yaw", loc.getYaw());
        config.set(path + ".pitch", loc.getPitch());
    }

    private Location loadLocation(FileConfiguration config, String path) {
        World world = Bukkit.getWorld(config.getString(path + ".world"));
        if (world == null) return null;

        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        float yaw = (float) config.getDouble(path + ".yaw");
        float pitch = (float) config.getDouble(path + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
}
