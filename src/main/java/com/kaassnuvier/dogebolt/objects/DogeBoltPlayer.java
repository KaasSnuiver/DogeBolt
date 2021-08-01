package com.kaassnuvier.dogebolt.objects;

import com.kaassnuvier.dogebolt.objects.enums.DogeBoltTeam;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Class holding data of a player who's playing dogebolt.
 * View individual elements for more information.
 */
@Data
@RequiredArgsConstructor
public class DogeBoltPlayer {

    /**
     * UUID of Player, used to link back to original CraftPlayer
     * {@link #getUuid()}
     */
    private final UUID uuid;

    /**
     * Holds which team a player is part of.
     * Defaults to unknown because instance of created before player has chosen a team.
     * {@link #getTeam()}
     * {@link #setTeam(DogeBoltTeam)}
     */
    private DogeBoltTeam team = DogeBoltTeam.UNKOWN;

    /**
     * Holds how many kills a player has made.
     * {@link #getKills()}
     * {@link #setKills(int)}
     */
    private int kills = 0;

    /**
     * Holds whether a player is alive or not
     * True = alive
     * False = dead
     * {@link #isAlive()}
     * {@link #setAlive(boolean)}
     */
    private boolean alive = true;
}
