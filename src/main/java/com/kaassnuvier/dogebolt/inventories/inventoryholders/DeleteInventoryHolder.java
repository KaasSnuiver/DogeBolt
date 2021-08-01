package com.kaassnuvier.dogebolt.inventories.inventoryholders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@AllArgsConstructor
public class DeleteInventoryHolder implements InventoryHolder {

    @Getter private final String game;

    @Override
    public Inventory getInventory() {
        return null;
    }
}
