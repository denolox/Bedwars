package me.clanmember.player;

import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private int kills;
    private int deaths;
    private int finalKills;
    private int finalDeaths;
    private int wins;
    private int losses;
    private int gamesPlayed;
    private int bedsBroken;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.kills = 0;
        this.deaths = 0;
        this.finalKills = 0;
        this.finalDeaths = 0;
        this.wins = 0;
        this.losses = 0;
        this.gamesPlayed = 0;
        this.bedsBroken = 0;
    }

    public PlayerData(UUID uuid, int kills, int deaths, int finalKills, int finalDeaths, 
                      int wins, int losses, int gamesPlayed, int bedsBroken) {
        this.uuid = uuid;
        this.kills = kills;
        this.deaths = deaths;
        this.finalKills = finalKills;
        this.finalDeaths = finalDeaths;
        this.wins = wins;
        this.losses = losses;
        this.gamesPlayed = gamesPlayed;
        this.bedsBroken = bedsBroken;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        this.kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        this.deaths++;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public void addFinalKill() {
        this.finalKills++;
    }

    public int getFinalDeaths() {
        return finalDeaths;
    }

    public void addFinalDeath() {
        this.finalDeaths++;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        this.wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void addLoss() {
        this.losses++;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void addGamePlayed() {
        this.gamesPlayed++;
    }

    public int getBedsBroken() {
        return bedsBroken;
    }

    public void addBedBroken() {
        this.bedsBroken++;
    }

    public double getKDRatio() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }

    public double getFinalKDRatio() {
        return finalDeaths == 0 ? finalKills : (double) finalKills / finalDeaths;
    }

    public double getWinRate() {
        return gamesPlayed == 0 ? 0 : (double) wins / gamesPlayed * 100;
    }
}
