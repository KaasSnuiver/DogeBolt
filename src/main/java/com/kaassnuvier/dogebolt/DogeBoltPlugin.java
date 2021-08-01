package com.kaassnuvier.dogebolt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaassnuiver.commandslibrary.CommandsLibrary;
import com.kaassnuvier.dogebolt.commands.DogeBoltCommand;
import com.kaassnuvier.dogebolt.data.DataProvider;
import com.kaassnuvier.dogebolt.data.mongo.MongoDataProvider;
import com.kaassnuvier.dogebolt.data.mysql.MySQLDataProvider;
import com.kaassnuvier.dogebolt.data.sqlite.SQLiteDataProvider;
import com.kaassnuvier.dogebolt.listeners.implementations.ArrowHitListener;
import com.kaassnuvier.dogebolt.listeners.implementations.InventoryClickListener;
import com.kaassnuvier.dogebolt.listeners.implementations.PlayerLoginListenerr;
import com.kaassnuvier.dogebolt.listeners.implementations.PlayerQuitListener;
import com.kaassnuvier.dogebolt.objects.managers.DogeBoltManager;
import com.kaassnuvier.dogebolt.objects.managers.MySQLDatabaseManager;
import com.kaassnuvier.dogebolt.utils.LocationSerializerAdapter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;

@Getter
public final class DogeBoltPlugin extends JavaPlugin {

    /**
     * Default hub location where people go on join and after a game is done
     * {@link #getHub()}
     */
    private Location hub;

    /**
     * Hodls instance of DataProvider, used to be the link with chosen dataprovider and plugin
     * {@link #getDataProvider()}
     */
    private DataProvider dataProvider;

    /**
     * Holds instance of Gson, used to serialize locations into a JSON Element
     * {@link #getGson()}
     */
    private Gson gson;

    /**
     * Holds instance of dogeboltManager
     * {@link #getDogeBoltManager()}
     */
    private DogeBoltManager dogeBoltManager;

    /**
     * Called when plugin is enabled by the server software, handles the following:
     * - Initializing gameManager
     * - Initializing gameManager
     * - Initializing GSON
     * - Initializing config.yml
     * - Registering commands
     * - Registering listeners
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.gson = new GsonBuilder().registerTypeHierarchyAdapter(Location.class, new LocationSerializerAdapter()).create();
        this.dogeBoltManager = new DogeBoltManager(this);
        saveDefaultConfig();

        String dataProvider = this.getConfig().getString("dataprovider", "sqlite");
        if (!dataProvider.equalsIgnoreCase("sqlite") && !dataProvider.equalsIgnoreCase("mongodb") && !dataProvider.equalsIgnoreCase("mysql")) {
            this.getLogger().log(Level.INFO, ChatColor.YELLOW + "Chosen dataprovider " + ChatColor.RED + dataProvider + ChatColor.YELLOW + " is not supported. Data will now be saved using SQLite.");
            dataProvider = "sqlite";
        }
        if (dataProvider.equalsIgnoreCase("mysql")) {
            if (!setupMySQL()) dataProvider = "sqlite";
            else this.getLogger().log(Level.INFO, ChatColor.RED + "Dataprovider connected. Data is now being saved using " + ChatColor.YELLOW + "MYSQL");
        }
        if (dataProvider.equalsIgnoreCase("mongodb")) {
            if (!setupMongo()) dataProvider = "mongodb";
            else this.getLogger().log(Level.INFO, ChatColor.RED + "Dataprovider connected. Data is now being saved using " + ChatColor.YELLOW + "MONGODB");
        }
        if (dataProvider.equalsIgnoreCase("sqlite")) {
            if (!setupSQLite()) {
                this.getLogger().log(Level.SEVERE, ChatColor.RED + "No connection could be made. Please report this issue and include any errors. Plugin is now shutting down.");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            else this.getLogger().log(Level.INFO, ChatColor.RED + "Dataprovider connected. Data is now being saved using " + ChatColor.YELLOW + "SQLITE");
        }

        this.hub = new Location(
                Bukkit.getWorld(this.getConfig().getString("hub.world", "world")),
                this.getConfig().getDouble("hub.x"),
                this.getConfig().getDouble("hub.y"),
                this.getConfig().getDouble("hub.z")
        );

        registerListeners(
                new PlayerQuitListener(this),
                new PlayerLoginListenerr(this),
                new ArrowHitListener(this),
                new InventoryClickListener(this)
        );

        registerCommands(
                new DogeBoltCommand(this)
        );
    }

    /**
     * Called when server software gets disabled, note, if the server crashes or is closed incorrectly, this method will NOT be called.
     * Handles the following
     * - Shuts down dataprovider
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        dataProvider.shutdown();
        hub = null;
        dogeBoltManager = null;
        gson = null;
    }

    /**
     * Sets up data to be saved using sqlite
     * @return whether or not setup was successful, true = succesful, false = successful
     */
    private boolean setupSQLite() {
        SQLiteDataProvider sqLiteDataProvider;
        try {
            sqLiteDataProvider = new SQLiteDataProvider(this);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        this.dataProvider = sqLiteDataProvider;
        return true;
    }

    /**
     * Sets up data to be sabed using MySQL
     * @return whether or not setup was successful, true = succesful, false = successful
     */
    private boolean setupMySQL() {
        MySQLDatabaseManager databaseManager = new MySQLDatabaseManager(this.getConfig());
        if (!databaseManager.isConnected()) return false;
        MySQLDataProvider mySQLDataProvider;
        try {
            mySQLDataProvider = new MySQLDataProvider(this, databaseManager);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        this.dataProvider = mySQLDataProvider;
        return true;
    }

    /**
     * Sets up data to be sabed using MongoDB
     * @return whether or not setup was successful, true = succesful, false = successful
     */
    public boolean setupMongo() {
        this.dataProvider = new MongoDataProvider(this);
        return true;
    }

    /**
     * Registers listeners
     * @param listeners array of listeners that will be registered
     */
    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    /**
     * Registers commands, using external library
     * @param objects array of objects of which commands will be registered
     * @see CommandsLibrary
     */
    private void registerCommands(Object... objects) {
        Arrays.stream(objects).forEach(object -> CommandsLibrary.registerCommand(() -> object));
    }
}
