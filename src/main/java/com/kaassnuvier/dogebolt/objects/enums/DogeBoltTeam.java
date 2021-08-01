package com.kaassnuvier.dogebolt.objects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

/**
 * En
 */
@AllArgsConstructor
public enum DogeBoltTeam {

    RED(ChatColor.RED  + "Team Red"),
    BLUE(ChatColor.BLUE + "Team Blue"),
    UNKOWN(ChatColor.GRAY + "Unkown");

    /**
     * Holds scoreboard name
     *
     * {@link #getScoreboardName()}
     */
    @Getter private final String scoreboardName;
}
