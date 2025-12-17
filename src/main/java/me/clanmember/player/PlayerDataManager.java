package me.clanmember.player;

import me.clanmember.Bedwars;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final Bedwars plugin;
    private final Map<UUID, PlayerData> playerDataMap;
    private final File dataFolder;

    public PlayerDataManager(Bedwars plugin) {
        this.plugin = plugin;
        this.playerDataMap = new HashMap<>();
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid) {
        if (!playerDataMap.containsKey(uuid)) {
            PlayerData data = loadPlayerData(uuid);
            playerDataMap.put(uuid, data);
        }
        return playerDataMap.get(uuid);
    }

    private PlayerData loadPlayerData(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        if (!file.exists()) {
            return new PlayerData(uuid);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        return new PlayerData(
            uuid,
            config.getInt("kills", 0),
            config.getInt("deaths", 0),
            config.getInt("final-kills", 0),
            config.getInt("final-deaths", 0),
            config.getInt("wins", 0),
            config.getInt("losses", 0),
            config.getInt("games-played", 0),
            config.getInt("beds-broken", 0)
        );
    }

    public void savePlayerData(UUID uuid) {
        PlayerData data = playerDataMap.get(uuid);
        if (data == null) return;

        File file = new File(dataFolder, uuid.toString() + ".yml");
        FileConfiguration config = new YamlConfiguration();

        config.set("kills", data.getKills());
        config.set("deaths", data.getDeaths());
        config.set("final-kills", data.getFinalKills());
        config.set("final-deaths", data.getFinalDeaths());
        config.set("wins", data.getWins());
        config.set("losses", data.getLosses());
        config.set("games-played", data.getGamesPlayed());
        config.set("beds-broken", data.getBedsBroken());

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Не удалось сохранить данные игрока " + uuid);
        }
    }

    public void saveAllData() {
        for (UUID uuid : playerDataMap.keySet()) {
            savePlayerData(uuid);
        }
    }
}
