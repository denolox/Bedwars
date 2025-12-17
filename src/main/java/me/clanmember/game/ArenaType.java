package me.clanmember.game;

public enum ArenaType {
    SOLO(1, "Solo"),
    DOUBLES(2, "Doubles"),
    TRIO(3, "3v3v3v3"),
    SQUAD(4, "4v4v4v4");

    private final int playersPerTeam;
    private final String displayName;

    ArenaType(int playersPerTeam, String displayName) {
        this.playersPerTeam = playersPerTeam;
        this.displayName = displayName;
    }

    public int getPlayersPerTeam() {
        return playersPerTeam;
    }

    public String getDisplayName() {
        return displayName;
    }
}
