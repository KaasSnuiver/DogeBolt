package com.kaassnuvier.dogebolt.listeners.implementations;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.listeners.BaseListener;
import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.kaassnuvier.dogebolt.objects.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerQuitListener extends BaseListener<PlayerQuitEvent> {

    public PlayerQuitListener(DogeBoltPlugin plugin) {
        super(plugin);
    }

    @Override
    @EventHandler
    public void execute(PlayerQuitEvent event) {
        Optional<DogeBoltGame> optionalGame = getPlugin().getDogeBoltManager().getGame(event.getPlayer());
        if (optionalGame.isEmpty()) return;

        optionalGame.get().getPlayers().forEach(dogeBoltPlayer -> Optional.ofNullable(Bukkit.getPlayer(dogeBoltPlayer.getUuid())).ifPresent(Messages.MIDGAME_LEAVE::sendTo));
    }
}
