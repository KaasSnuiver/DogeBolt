package com.kaassnuvier.dogebolt.listeners.implementations;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.listeners.BaseListener;
import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.kaassnuvier.dogebolt.objects.DogeBoltPlayer;
import com.kaassnuvier.dogebolt.objects.enums.DogeBoltTeam;
import com.kaassnuvier.dogebolt.objects.enums.GameState;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ArrowHitListener extends BaseListener<ProjectileHitEvent> {

    public ArrowHitListener(DogeBoltPlugin plugin) {
        super(plugin);
    }

    @Override
    @EventHandler
    public void execute(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player shooter)) return;
        if (!(event.getHitEntity() instanceof Player player)) {
            Optional<DogeBoltGame> optionalDogeBoltGame = getPlugin().getDogeBoltManager().getGame(shooter);
            if (optionalDogeBoltGame.isEmpty()) return;
            for (DogeBoltPlayer dogeBoltPlayer : optionalDogeBoltGame.get().getPlayers()) {
                if (!dogeBoltPlayer.getUuid().equals(shooter.getUniqueId())) continue;
                Location location = (dogeBoltPlayer.getTeam() == DogeBoltTeam.BLUE) ? optionalDogeBoltGame.get().getRedSpawn() : optionalDogeBoltGame.get().getBlueSpawn();
                location.getWorld().dropItem(location, new ItemStack(Material.ARROW));
                break;
            }
            return;
        }
        Optional<DogeBoltGame> optionalGame = getPlugin().getDogeBoltManager().getGame(player);
        if (optionalGame.isEmpty()) return;
        event.getEntity().remove();
        player.setGameMode(GameMode.SPECTATOR);
        player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
        for (DogeBoltPlayer dogeBoltPlayer : optionalGame.get().getPlayers()) {
            if (!dogeBoltPlayer.getUuid().equals(player.getUniqueId())) continue;
            dogeBoltPlayer.setAlive(true);
            if (!getPlugin().getDogeBoltManager().isTeamDead(dogeBoltPlayer.getTeam(), optionalGame.get())) return;
            optionalGame.get().setGameState(GameState.ENDED);
            Bukkit.getScheduler().runTaskLater(getPlugin(), () -> optionalGame.get().stop(), 200);
            return;
        }
    }
}
