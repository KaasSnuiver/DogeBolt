package com.kaassnuvier.dogebolt.data.mysql;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.data.DataProvider;
import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.kaassnuvier.dogebolt.objects.enums.SQLQueries;
import com.kaassnuvier.dogebolt.objects.managers.MySQLDatabaseManager;
import lombok.Cleanup;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class MySQLDataProvider implements DataProvider {

    private final MySQLDatabaseManager databaseManager;
    private final DogeBoltPlugin plugin;

    public MySQLDataProvider(DogeBoltPlugin plugin, MySQLDatabaseManager databaseManager) throws SQLException {
        this.databaseManager = databaseManager;
        this.plugin = plugin;

        @Cleanup
        Connection connection = databaseManager.getHikariDataSource().getConnection();

        {
            @Cleanup
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.CREATE_TABLE_QUERY.getQuery());
            preparedStatement.execute();
        }
        {
            @Cleanup
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SELECT_ALL_QUERY.getQuery());

            @Cleanup
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Using do-while instead of a while-loop to prevent skipping of the first elements
                do {
                    Location hub = plugin.getGson().fromJson(resultSet.getString("hub_location"), Location.class);
                    Location redSpawn = plugin.getGson().fromJson(resultSet.getString("red_spawn"), Location.class);
                    Location blueSpawn = plugin.getGson().fromJson(resultSet.getString("blue_spawn"), Location.class);

                    if (hub == null || redSpawn == null || blueSpawn == null) continue;

                    plugin.getDogeBoltManager().getGames().add(new DogeBoltGame(
                            plugin,
                            resultSet.getString("name"),
                            hub,
                            redSpawn,
                            blueSpawn,
                            resultSet.getInt("min_players"),
                            resultSet.getInt("max_players")
                    ));
                } while (resultSet.next());
            }
        }
    }

    @Override
    public void saveGame(DogeBoltGame game) {
        CompletableFuture.runAsync(() -> {
            try {

                @Cleanup
                Connection connection = databaseManager.getHikariDataSource().getConnection();

                @Cleanup
                PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SAVE_GAME_QUERY.getQuery());

                preparedStatement.setString(1, game.getName());
                preparedStatement.setString(2, plugin.getGson().toJson(game.getHub()));
                preparedStatement.setString(3, plugin.getGson().toJson(game.getRedSpawn()));
                preparedStatement.setString(4, plugin.getGson().toJson(game.getBlueSpawn()));
                preparedStatement.setInt(5, game.getMinPlayers());
                preparedStatement.setInt(6, game.getMaxPlayers());

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void deleteGame(String game) {
        CompletableFuture.runAsync(() -> {
            try {

                @Cleanup
                Connection connection = databaseManager.getHikariDataSource().getConnection();

                @Cleanup
                PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.DELETE_GAME_QUERY.getQuery());
                preparedStatement.setString(1, game);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void shutdown() {
        databaseManager.shutdown();
    }
}
