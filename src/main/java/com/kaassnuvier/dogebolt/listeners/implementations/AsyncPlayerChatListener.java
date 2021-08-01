package com.kaassnuvier.dogebolt.listeners.implementations;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.inventories.inventoryholders.SetupInventoryHolder;
import com.kaassnuvier.dogebolt.listeners.BaseListener;
import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.kaassnuvier.dogebolt.objects.enums.Messages;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

public class AsyncPlayerChatListener extends BaseListener<AsyncPlayerChatEvent> {

    public AsyncPlayerChatListener(DogeBoltPlugin plugin) {
        super(plugin);
    }

    @Override
    @EventHandler
    public void execute(AsyncPlayerChatEvent event) {
        if (!getPlugin().getDogeBoltManager().getSetupMap().containsKey(event.getPlayer().getUniqueId())) return;
        Inventory inventory = getPlugin().getDogeBoltManager().getSetupMap().get(event.getPlayer().getUniqueId());
        if (inventory == null || !(inventory.getHolder() instanceof SetupInventoryHolder holder)) return;
        event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase("cancel")) {
            Messages.CANCELLED.sendTo(event.getPlayer());
            getPlugin().getDogeBoltManager().getSetupMap().remove(event.getPlayer().getUniqueId());
            return;
        }
        if (event.getMessage().equalsIgnoreCase("set")) {
            Location location = event.getPlayer().getLocation();
            switch (holder.getCurrentAction()) {
                case HUB_LOCATION -> holder.setHubSpawn(location);
                case RED_SPAWN -> holder.setRedSpawn(location);
                case BLUE_SPAWN -> holder.setBlueSpawn(location);
            }
            if (holder.getHubSpawn() != null && holder.getRedSpawn() != null && holder.getBlueSpawn() != null) {
                getPlugin().getDogeBoltManager().getSetupMap().remove(event.getPlayer().getUniqueId());
                Messages.GAME_CREATED.sendTo(event.getPlayer());
                DogeBoltGame game = new DogeBoltGame(
                        getPlugin(),
                        holder.getName(),
                        holder.getHubSpawn(),
                        holder.getRedSpawn(),
                        holder.getBlueSpawn(),
                        holder.getMinPlayers(),
                        holder.getMaxPlayers()
                );
                getPlugin().getDataProvider().saveGame(game);
                getPlugin().getDogeBoltManager().getGames().add(game);
                return;
            }
            event.getPlayer().openInventory(inventory);
        } else Messages.UNKNOWN_MESSAGE.sendTo(event.getPlayer());
    }
}
