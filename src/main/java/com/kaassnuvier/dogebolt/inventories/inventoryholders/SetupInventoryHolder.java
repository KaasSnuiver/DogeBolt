package com.kaassnuvier.dogebolt.inventories.inventoryholders;

import com.kaassnuvier.dogebolt.objects.enums.SetupAction;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

@Data
public class SetupInventoryHolder implements InventoryHolder {

    /**
     * Person who's setting up a dogebolt arena.
     */
    private final UUID uuid;

    /**
     * Location of default hub
     */

    private final String name;
    private final int minPlayers;
    private final int maxPlayers;
    private Location hubSpawn;
    private Location redSpawn;
    private Location blueSpawn;
    private SetupAction currentAction;

    public SetupInventoryHolder(UUID uuid, String name, int minPlayers, int maxPlayers) {
        this.uuid = uuid;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
