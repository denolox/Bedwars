package me.clanmember;

import me.clanmember.arena.ArenaManager;
import me.clanmember.commands.BedwarsCommand;
import me.clanmember.commands.SetupCommand;
import me.clanmember.game.GameManager;
import me.clanmember.listeners.*;
import me.clanmember.player.PlayerDataManager;
import me.clanmember.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Bedwars extends JavaPlugin {

    private static Bedwars instance;
    private GameManager gameManager;
    private ShopManager shopManager;
    private PlayerDataManager playerDataManager;
    private ArenaManager arenaManager;

    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        
        gameManager = new GameManager(this);
        shopManager = new ShopManager();
        playerDataManager = new PlayerDataManager(this);
        arenaManager = new ArenaManager(this);
        
        arenaManager.loadArenas();
        
        registerCommands();
        registerListeners();
        
        getLogger().info("Bedwars плагин запущен!");
    }

    @Override
    public void onDisable() {
        if (gameManager != null) {
            for (String arenaName : gameManager.getArenas().keySet()) {
                gameManager.getArena(arenaName).stopGenerators();
            }
        }
        
        if (playerDataManager != null) {
            playerDataManager.saveAllData();
        }
        
        getLogger().info("Bedwars плагин остановлен!");
    }

    private void registerCommands() {
        getCommand("bedwars").setExecutor(new BedwarsCommand(this));
        getCommand("setup").setExecutor(new SetupCommand(this));
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new PlayerDeathListener(this), this);
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new BlockPlaceListener(this), this);
        pm.registerEvents(new EntityDamageListener(this), this);
        pm.registerEvents(new FoodLevelChangeListener(this), this);
        pm.registerEvents(new InventoryClickListener(this), this);
        pm.registerEvents(new PlayerInteractListener(this), this);
        pm.registerEvents(new ItemDropListener(this), this);
        pm.registerEvents(new ItemPickupListener(this), this);
        pm.registerEvents(new ExplosionListener(this), this);
        pm.registerEvents(new WeatherChangeListener(), this);
        pm.registerEvents(new TeamSelectorListener(this), this);
        pm.registerEvents(new LobbyItemListener(this), this);
        pm.registerEvents(new ShopGUIListener(this), this);
        pm.registerEvents(new UpgradeGUIListener(this), this);
        pm.registerEvents(new CompassTracker(this), this);
        pm.registerEvents(new NPCInteractListener(this), this);
        pm.registerEvents(new PlayerRespawnListener(this), this);
    }

    public static Bedwars getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }
}