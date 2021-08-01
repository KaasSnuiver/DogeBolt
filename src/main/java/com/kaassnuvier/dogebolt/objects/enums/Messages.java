package com.kaassnuvier.dogebolt.objects.enums;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
@AllArgsConstructor
public enum Messages {

    JOINED_GAME(ChatColor.YELLOW + "You have joined a game of dogebolt."),
    NO_GAME_AVAILABLE(ChatColor.YELLOW + "Couldn't add you to a game, all are filled! Please try again later."),
    LEFT_GAME(ChatColor.YELLOW + "You have left the game."),
    COULD_NOT_LEAVE(ChatColor.YELLOW + "You couldn't leave the game because it has already started."),
    CANCELLED(ChatColor.YELLOW + "The game has been cancelled because there weren't enough players."),
    MIDGAME_LEAVE(ChatColor.YELLOW + "A player has logged out during the game."),
    GAME_NOT_FOUND(ChatColor.YELLOW + "Could not find a game with this name."),
    GAME_REMOVED(ChatColor.YELLOW + "Game has successfully been removed."),
    REMOVAL_CANCELLED(ChatColor.YELLOW + "Removal of game has been cancelled."),
    MIN_BIGGED_THAN_MAX(ChatColor.YELLOW + "Minimum players cannot be bigger than max."),
    SETUP_EXPLANATION(ChatColor.YELLOW + "Go to the location you want to set and type " + ChatColor.RED + "set" + ChatColor.YELLOW + " to set the location. Or type " + ChatColor.RED + "cancel " + ChatColor.YELLOW + "to cancel."),
    UNKNOWN_MESSAGE(ChatColor.YELLOW + "Please use "  + ChatColor.RED  + "set" + ChatColor.YELLOW + " or " + ChatColor.RED + "cancel" + ChatColor.YELLOW + "."),
    GAME_CREATED(ChatColor.YELLOW + "Setup successful! Game has been created."),
    NOT_IN_A_AME(ChatColor.YELLOW + "You have to join a game before you can leave one."),
    NAME_TAKEN(ChatColor.YELLOW + "This name is already in use, please choose a different one.");

    private final String message;

    public void sendTo(CommandSender receiver) {
        receiver.sendMessage(message);
    }
}
