package com.kaassnuvier.dogebolt.listeners.implementations;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.inventories.inventoryholders.DeleteInventoryHolder;
import com.kaassnuvier.dogebolt.inventories.inventoryholders.SetupInventoryHolder;
import com.kaassnuvier.dogebolt.listeners.BaseListener;
import com.kaassnuvier.dogebolt.objects.enums.Messages;
import com.kaassnuvier.dogebolt.objects.enums.SetupAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener extends BaseListener<InventoryClickEvent> {

    public InventoryClickListener(DogeBoltPlugin plugin) {
        super(plugin);
    }

    @Override
    @EventHandler
    public void execute(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() instanceof DeleteInventoryHolder) {
            switch (event.getSlot()) {
                // Cancel
                case 3 -> {
                    event.getWhoClicked().closeInventory();
                    Messages.REMOVAL_CANCELLED.sendTo(event.getWhoClicked());
                }

                // Confirm
                case 5 -> {
                    event.getWhoClicked().closeInventory();
                    getPlugin().getDataProvider().deleteGame(((DeleteInventoryHolder) event.getClickedInventory().getHolder()).getGame());
                    Messages.GAME_REMOVED.sendTo(event.getWhoClicked());
                }
            }
            return;
        }
        if (event.getClickedInventory().getHolder() instanceof SetupInventoryHolder holder) {
            switch (event.getSlot()) {
                case 1 -> {
                    event.getWhoClicked().closeInventory();
                    holder.setCurrentAction(SetupAction.HUB_LOCATION);
                    Messages.SETUP_EXPLANATION.sendTo(event.getWhoClicked());
                }
                case 2 -> {
                    event.getWhoClicked().closeInventory();
                    holder.setCurrentAction(SetupAction.RED_SPAWN);
                    Messages.SETUP_EXPLANATION.sendTo(event.getWhoClicked());
                }
                case 3 -> {
                    event.getWhoClicked().closeInventory();
                    holder.setCurrentAction(SetupAction.BLUE_SPAWN);
                    Messages.SETUP_EXPLANATION.sendTo(event.getWhoClicked());
                }
                case 8 -> {
                    event.getWhoClicked().closeInventory();
                    Messages.CANCELLED.sendTo(event.getWhoClicked());
                }
            }
        }
    }
}
