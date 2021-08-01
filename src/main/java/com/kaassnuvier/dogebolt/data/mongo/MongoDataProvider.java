package com.kaassnuvier.dogebolt.data.mongo;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.data.DataProvider;
import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.CompletableFuture;

public class MongoDataProvider implements DataProvider {

    private final DogeBoltPlugin plugin;
    private final MongoClient mongoClient;
    private final MongoCollection<Document> mongoCollection;

    public MongoDataProvider(DogeBoltPlugin plugin) {
        this.plugin = plugin;
        FileConfiguration fileConfiguration = plugin.getConfig();
        String uri = fileConfiguration.getString("mongodb.uri", "uri");
        String database = fileConfiguration.getString("mongodb.database", "databse");
        String collection = fileConfiguration.getString("mongodb.collection", "collection");

        this.mongoClient = MongoClients.create(uri);
        this.mongoCollection = mongoClient.getDatabase(database).getCollection(collection);

    }


    @Override
    public void saveGame(DogeBoltGame game) {
        CompletableFuture.runAsync(() -> {
           Document document = new Document("name", game.getName());

           document.put("hub", plugin.getGson().toJson(game.getHub()));
           document.put("redSpawn", plugin.getGson().toJson(game.getRedSpawn()));
           document.put("blueSpawn", plugin.getGson().toJson(game.getBlueSpawn()));
           document.put("minPlayers", game.getMinPlayers());
           document.put("maxPlayers", game.getMaxPlayers());

           mongoCollection.insertOne(document);
        });
    }

    @Override
    public void deleteGame(String name) {
        CompletableFuture.runAsync(() -> mongoCollection.deleteOne(Filters.eq("name", name)));
    }

    @Override
    public void shutdown() {
        mongoClient.close();
    }
}
