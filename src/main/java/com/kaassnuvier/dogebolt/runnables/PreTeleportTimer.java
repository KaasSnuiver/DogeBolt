package com.kaassnuvier.dogebolt.runnables;

import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.kaassnuvier.dogebolt.objects.DogeBoltPlayer;
import com.kaassnuvier.dogebolt.objects.enums.DogeBoltTeam;
import com.kaassnuvier.dogebolt.objects.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class PreTeleportTimer extends BukkitRunnable {

    private final DogeBoltGame game;
    private int countdown = 10;

    public PreTeleportTimer(DogeBoltGame game) {
        this.game = game;
        this.runTaskTimer(game.getPlugin(), 20, 20);

        int half = game.getPlayers().size() / 2;
        int start = 0;
        for (DogeBoltPlayer dogeBoltPlayer : game.getPlayers()) {
            if (start <= half) dogeBoltPlayer.setTeam(DogeBoltTeam.RED);
            else dogeBoltPlayer.setTeam(DogeBoltTeam.BLUE);
            start++;
        }
    }

    @Override
    public void run() {
        if (game.getPlayers().size() < game.getMinPlayers()) {
            game.getPlayers().forEach(dogeBoltPlayer -> Optional.ofNullable(Bukkit.getPlayer(dogeBoltPlayer.getUuid())).ifPresent(Messages.CANCELLED::sendTo));
            this.cancel();
            return;
        }

        if (countdown == 0) {
            game.getPlayers().forEach(dogeBoltPlayer -> Optional.ofNullable(Bukkit.getPlayer(dogeBoltPlayer.getUuid())).ifPresent(player -> Bukkit.getScheduler().runTask(game.getPlugin(), () -> player.teleport((dogeBoltPlayer.getTeam() == DogeBoltTeam.BLUE) ? game.getBlueSpawn() : game.getRedSpawn()))));
            this.cancel();
            new CountdownTimer(game);
            return;
        }
        countdown--;
    }
}
