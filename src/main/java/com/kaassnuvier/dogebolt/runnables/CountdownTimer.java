package com.kaassnuvier.dogebolt.runnables;

import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.kaassnuvier.dogebolt.objects.enums.DogeBoltTeam;
import com.kaassnuvier.dogebolt.objects.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class CountdownTimer extends BukkitRunnable {

    private final DogeBoltGame game;
    private int countdown = 10;

    public CountdownTimer(DogeBoltGame game) {
        this.runTaskTimerAsynchronously(game.getPlugin(), 20, 20);
        this.game = game;
    }

    @Override
    public void run() {
        if (countdown <= 0) {
            game.getPlayers().forEach(dogeBoltPlayer -> Optional.ofNullable(Bukkit.getPlayer(dogeBoltPlayer.getUuid())).ifPresent(player -> {
                player.sendTitle(ChatColor.DARK_PURPLE + "GO!", ChatColor.DARK_PURPLE + "The game has started!", 20, 70, 10);
                Bukkit.getScheduler().runTask(game.getPlugin(), () -> {
                    player.getInventory().clear();
                    player.getInventory().addItem(new ItemStack(Material.BOW));
                });
            }));
            game.setGameState(GameState.STARTED);
            this.cancel();
            return;
        }
        game.getPlayers().forEach(dogeBoltPlayer -> Optional.ofNullable(Bukkit.getPlayer(dogeBoltPlayer.getUuid())).ifPresent(player -> player.sendTitle(ChatColor.DARK_PURPLE + "Countdown started!", ChatColor.DARK_PURPLE + "Starting in " + countdown + " seconds", 20, 70, 10)));
        game.getPlayers().stream().filter(dogeBoltPlayer -> dogeBoltPlayer.getTeam() == DogeBoltTeam.RED).findFirst().flatMap(dogeBoltPlayer -> Optional.ofNullable(Bukkit.getPlayer(dogeBoltPlayer.getUuid()))).ifPresent(player -> player.getInventory().addItem(new ItemStack(Material.ARROW)));
        game.getPlayers().stream().filter(dogeBoltPlayer -> dogeBoltPlayer.getTeam() == DogeBoltTeam.BLUE).findFirst().flatMap(dogeBoltPlayer -> Optional.ofNullable(Bukkit.getPlayer(dogeBoltPlayer.getUuid()))).ifPresent(player -> player.getInventory().addItem(new ItemStack(Material.ARROW)));
        countdown--;
    }
}
