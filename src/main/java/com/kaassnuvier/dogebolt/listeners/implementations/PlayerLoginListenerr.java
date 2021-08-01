package com.kaassnuvier.dogebolt.listeners.implementations;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.listeners.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLoginListenerr extends BaseListener<PlayerJoinEvent> {

    public PlayerLoginListenerr(DogeBoltPlugin plugin) {
        super(plugin);
    }

    @Override
    @EventHandler
    public void execute(PlayerJoinEvent event) {
        event.getPlayer().teleport(getPlugin().getHub());
        event.getPlayer().getInventory().clear();
    }
}
