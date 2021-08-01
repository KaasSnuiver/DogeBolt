package com.kaassnuvier.dogebolt.objects.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.SQLException;

public class MySQLDatabaseManager {

    /**
     * Holds instance of HikaridatasSource {@link #getHikariDataSource()}
     */
    @Getter
    private final HikariDataSource hikariDataSource;

    /**
     * Initializes class
     * Tries to establish a connection with database using the credentials in the config.yml
     */
    public MySQLDatabaseManager(FileConfiguration config) {
        String host = config.getString("mysql.host");
        int port = config.getInt("mysql.port");
        String database = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false");
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setPoolName("DogeBolt-HikariPool");
        hikariConfig.setMaximumPoolSize(6);
        hikariConfig.setMinimumIdle(10);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setConnectionTimeout(5000);
        hikariConfig.setInitializationFailTimeout(-1);

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Checks if a connection with database exists
     *
     * @return If there is a usable connection with database
     */
    public boolean isConnected() {
        if (hikariDataSource == null || hikariDataSource.isClosed()) return false;
        try {
            if (hikariDataSource.getConnection() == null) return false;
            return !hikariDataSource.getConnection().isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Used to close all connections with database upon disabling the plugin
     * Will first close connection (if there is one) before tryin to close HikariDataSource
     * {@link #isConnected()} {@link #getHikariDataSource()}
     */
    public void shutdown() {
        if (isConnected()) {
            try {
                if (getHikariDataSource().getConnection() != null) {
                    getHikariDataSource().getConnection().close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (!getHikariDataSource().isClosed()) {
            getHikariDataSource().close();
        }
    }
}
