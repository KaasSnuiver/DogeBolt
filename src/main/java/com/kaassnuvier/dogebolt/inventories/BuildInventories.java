package com.kaassnuvier.dogebolt.inventories;

import com.kaassnuvier.dogebolt.inventories.inventoryholders.DeleteInventoryHolder;
import com.kaassnuvier.dogebolt.inventories.inventoryholders.SetupInventoryHolder;
import com.kaassnuvier.dogebolt.utils.ItemStackBuilder;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

@UtilityClass
public class BuildInventories {

    public Inventory setupInventory(UUID uuid, String gameName, int minPlayers, int maxPlayers) {
        Inventory inventory = Bukkit.createInventory(new SetupInventoryHolder(uuid, gameName, minPlayers, maxPlayers), 9, ChatColor.DARK_PURPLE + "Setup a new dogebolt game.");
        inventory.setItem(1, new ItemStackBuilder(Material.RED_WOOL)
                .setName("Hub spawn")
                .setLore("You haven't yet set a location for the hub of this arena.", "Click me to set this location.")
                .build());

        inventory.setItem(2, new ItemStackBuilder(Material.RED_WOOL)
                .setName("Red spawn")
                .setLore("You haven't yet set a location for the red spawn", "Click me to set this location.")
                .build());

        inventory.setItem(3, new ItemStackBuilder(Material.RED_WOOL)
                .setName("Blue spawn")
                .setLore("You haven't yet set a location for the blue spawn", "Click me to set this location.")
                .build());

        inventory.setItem(8, new ItemStackBuilder(Material.BARRIER)
                .setName("Cancel")
                .setLore("Press me if you want to cancel the creation of this new arena.")
                .build());

        return inventory;
    }

    public Inventory deleteInventory(String name) {
        Inventory inventory = Bukkit.createInventory(new DeleteInventoryHolder(name), 9, ChatColor.DARK_PURPLE + "Are you sure you want to delete " + name + " ?");

        inventory.setItem(3, new ItemStackBuilder(Material.RED_WOOL)
                .setName("Cancel")
                .setLore("Click me to not remove the game " + name)
                .build());

        inventory.setItem(5, new ItemStackBuilder(Material.GREEN_WOOL)
                .setName("Confirm")
                .setLore("Click me to confirm you want to remove " + name)
                .build());

        return inventory;
    }

}
