package com.kaassnuvier.dogebolt.data;

import com.kaassnuvier.dogebolt.objects.DogeBoltGame;

import java.util.concurrent.CompletableFuture;

public interface DataProvider {

    void saveGame(DogeBoltGame game);

    void deleteGame(String name);

    void shutdown();
}
