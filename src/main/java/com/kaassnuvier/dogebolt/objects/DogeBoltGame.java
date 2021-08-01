package com.kaassnuvier.dogebolt.objects;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.objects.enums.GameState;
import com.kaassnuvier.dogebolt.runnables.PreTeleportTimer;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
public class DogeBoltGame {

    private final DogeBoltPlugin plugin;
    private final String name;

    private final Set<DogeBoltPlayer> players = new HashSet<>();

    private GameState gameState = GameState.WAITING;

    private final Location hub;
    private final Location redSpawn;
    private final Location blueSpawn;

    private int minPlayers;
    private int maxPlayers;

    public DogeBoltGame(DogeBoltPlugin plugin, String name, Location hub, Location redSpawn, Location blueSpawn, int minPlayers, int maxPlayers) {
        this.plugin = plugin;
        this.name = name;
        this.hub = hub;
        this.redSpawn = redSpawn;
        this.blueSpawn = blueSpawn;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public void start() {
        new PreTeleportTimer(this);
    }

    public void stop() {
        players.forEach(dogeBoltPlayer -> Optional.ofNullable(Bukkit.getPlayer(dogeBoltPlayer.getUuid())).ifPresent(player -> player.teleport(plugin.getHub())));
        players.clear();
        this.gameState = GameState.WAITING;
    }
}
