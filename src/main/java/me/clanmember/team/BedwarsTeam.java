package me.clanmember.team;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BedwarsTeam {
    private final TeamColor color;
    private final List<Player> players;
    private Location spawnLocation;
    private Location bedLocation;
    private boolean bedDestroyed;
    private int sharpnessLevel;
    private int protectionLevel;
    private boolean hasteEnabled;
    private boolean trapEnabled;

    public BedwarsTeam(TeamColor color) {
        this.color = color;
        this.players = new ArrayList<>();
        this.bedDestroyed = false;
        this.sharpnessLevel = 0;
        this.protectionLevel = 0;
        this.hasteEnabled = false;
        this.trapEnabled = false;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public int getSize() {
        return players.size();
    }

    public TeamColor getColor() {
        return color;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getBedLocation() {
        return bedLocation;
    }

    public void setBedLocation(Location bedLocation) {
        this.bedLocation = bedLocation;
    }

    public boolean isBedDestroyed() {
        return bedDestroyed;
    }

    public void setBedDestroyed(boolean bedDestroyed) {
        this.bedDestroyed = bedDestroyed;
    }

    public int getSharpnessLevel() {
        return sharpnessLevel;
    }

    public void setSharpnessLevel(int sharpnessLevel) {
        this.sharpnessLevel = sharpnessLevel;
    }

    public int getProtectionLevel() {
        return protectionLevel;
    }

    public void setProtectionLevel(int protectionLevel) {
        this.protectionLevel = protectionLevel;
    }

    public boolean isHasteEnabled() {
        return hasteEnabled;
    }

    public void setHasteEnabled(boolean hasteEnabled) {
        this.hasteEnabled = hasteEnabled;
    }

    public boolean isTrapEnabled() {
        return trapEnabled;
    }

    public void setTrapEnabled(boolean trapEnabled) {
        this.trapEnabled = trapEnabled;
    }

    public boolean isEliminated() {
        return bedDestroyed && players.isEmpty();
    }
}
