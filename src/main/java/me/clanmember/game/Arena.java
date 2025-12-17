package me.clanmember.game;

import me.clanmember.generator.GeneratorType;
import me.clanmember.generator.ResourceGenerator;
import me.clanmember.team.BedwarsTeam;
import me.clanmember.team.TeamColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Arena {
    private final String name;
    private final ArenaType type;
    private final Map<TeamColor, BedwarsTeam> teams;
    private final List<ResourceGenerator> generators;
    private final Set<Player> players;
    private final Set<Player> spectators;
    private GameState state;
    private Location lobbySpawn;
    private Location spectatorSpawn;
    private int minPlayers;
    private int maxPlayers;
    private int countdown;

    public Arena(String name, ArenaType type) {
        this.name = name;
        this.type = type;
        this.teams = new HashMap<>();
        this.generators = new ArrayList<>();
        this.players = new HashSet<>();
        this.spectators = new HashSet<>();
        this.state = GameState.WAITING;
        this.countdown = 60;
        this.minPlayers = 2;
        this.maxPlayers = type.getPlayersPerTeam() * 8;
        initializeTeams();
    }

    private void initializeTeams() {
        TeamColor[] colors = {TeamColor.RED, TeamColor.BLUE, TeamColor.GREEN, TeamColor.YELLOW,
                              TeamColor.AQUA, TeamColor.WHITE, TeamColor.PINK, TeamColor.GRAY};
        for (int i = 0; i < Math.min(8, colors.length); i++) {
            teams.put(colors[i], new BedwarsTeam(colors[i]));
        }
    }

    public void addPlayer(Player player) {
        if (state == GameState.WAITING || state == GameState.STARTING) {
            players.add(player);
            assignToTeam(player);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        for (BedwarsTeam team : teams.values()) {
            team.removePlayer(player);
        }
    }

    private void assignToTeam(Player player) {
        BedwarsTeam smallestTeam = null;
        int smallestSize = Integer.MAX_VALUE;

        for (BedwarsTeam team : teams.values()) {
            if (team.getSize() < smallestSize && team.getSize() < type.getPlayersPerTeam()) {
                smallestTeam = team;
                smallestSize = team.getSize();
            }
        }

        if (smallestTeam != null) {
            smallestTeam.addPlayer(player);
        }
    }

    public void addGenerator(Location location, GeneratorType type) {
        generators.add(new ResourceGenerator(location, type));
    }

    public void startGenerators() {
        for (ResourceGenerator generator : generators) {
            generator.start();
        }
    }

    public void stopGenerators() {
        for (ResourceGenerator generator : generators) {
            generator.stop();
        }
    }

    public BedwarsTeam getPlayerTeam(Player player) {
        for (BedwarsTeam team : teams.values()) {
            if (team.hasPlayer(player)) {
                return team;
            }
        }
        return null;
    }

    public List<BedwarsTeam> getAliveTeams() {
        List<BedwarsTeam> aliveTeams = new ArrayList<>();
        for (BedwarsTeam team : teams.values()) {
            if (!team.isEliminated()) {
                aliveTeams.add(team);
            }
        }
        return aliveTeams;
    }

    public String getName() {
        return name;
    }

    public ArenaType getType() {
        return type;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(players);
    }

    public Set<Player> getSpectators() {
        return new HashSet<>(spectators);
    }

    public void addSpectator(Player player) {
        spectators.add(player);
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);
    }

    public Map<TeamColor, BedwarsTeam> getTeams() {
        return teams;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public void setSpectatorSpawn(Location spectatorSpawn) {
        this.spectatorSpawn = spectatorSpawn;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public void decrementCountdown() {
        countdown--;
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean canStart() {
        return players.size() >= minPlayers;
    }

    public List<ResourceGenerator> getGenerators() {
        return generators;
    }
}
