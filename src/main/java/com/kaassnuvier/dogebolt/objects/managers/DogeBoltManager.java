package com.kaassnuvier.dogebolt.objects.managers;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.kaassnuvier.dogebolt.objects.DogeBoltPlayer;
import com.kaassnuvier.dogebolt.objects.enums.DogeBoltTeam;
import com.kaassnuvier.dogebolt.objects.enums.GameState;
import com.kaassnuvier.dogebolt.objects.enums.Messages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

@Getter
@AllArgsConstructor
public class DogeBoltManager {

    private final DogeBoltPlugin plugin;

    private final Set<DogeBoltGame> games = new HashSet<>();

    private final Map<UUID, Inventory> setupMap = new HashMap<>();

    public boolean nameTaken(String name) {
        for (DogeBoltGame dogeBoltGame : games) {
            if (!dogeBoltGame.getName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public void joinGame(Player player) {
        for (DogeBoltGame game : games) {
            if (game.getGameState() != GameState.WAITING || game.getPlayers().size() >= game.getMaxPlayers()) continue;
            player.teleport(game.getHub());
            game.getPlayers().add(new DogeBoltPlayer(player.getUniqueId()));
            Messages.JOINED_GAME.sendTo(player);

            if (game.getPlayers().size() == game.getMinPlayers()) game.start();
            break;
        }
        Messages.NO_GAME_AVAILABLE.sendTo(player);
    }

    public void leaveGame(Player player, DogeBoltGame game) {
        if (game.getGameState() != GameState.WAITING || game.getGameState() != GameState.ENDED) {
            Messages.COULD_NOT_LEAVE.sendTo(player);
            return;
        }
        for (DogeBoltPlayer dogeBoltPlayer : game.getPlayers()) {
            if (!dogeBoltPlayer.getUuid().equals(player.getUniqueId())) continue;
            game.getPlayers().remove(dogeBoltPlayer);
            player.teleport(player.getLocation());
            Messages.LEFT_GAME.sendTo(player);
            return;
        }
    }

    public Optional<DogeBoltGame> getGame(String name) {
        for (DogeBoltGame game : games) {
            if (!game.getName().equalsIgnoreCase(name)) continue;
            return Optional.of(game);
        }
        return Optional.empty();
    }

    public Optional<DogeBoltGame> getGame(Player player) {
        for (DogeBoltGame game : games) {
            for (DogeBoltPlayer dogeBoltPlayer : game.getPlayers()) {
                if (!dogeBoltPlayer.getUuid().equals(player.getUniqueId())) continue;
                return Optional.of(game);
            }
        }
        return Optional.empty();
    }

    public boolean isTeamDead(DogeBoltTeam team, DogeBoltGame game) {
        for (DogeBoltPlayer player : game.getPlayers()) {
            if (player.getTeam() != team) continue;
            if (player.isAlive()) return false;
        }
        return true;
    }
}
