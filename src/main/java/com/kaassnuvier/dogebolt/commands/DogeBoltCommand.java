package com.kaassnuvier.dogebolt.commands;

import com.kaassnuiver.commandslibrary.annotations.*;
import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import com.kaassnuvier.dogebolt.inventories.BuildInventories;
import com.kaassnuvier.dogebolt.objects.DogeBoltGame;
import com.kaassnuvier.dogebolt.objects.enums.GameState;
import com.kaassnuvier.dogebolt.objects.enums.Messages;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

@Command("dogebolt")
@Permission("dogebolt.commands.dogebolt")
@Usage("/dogebolt <create / join / leave / remove>")
@Aliases("db")
@AllArgsConstructor
public class DogeBoltCommand {

    private final DogeBoltPlugin plugin;

    @Permission("dogebolt.commands.dogebolt.create")
    @Usage("/dogebolt create <name of game>, <min players needed>, <max players needed>")
    public void create(CommandSender commandSender, String name, int minPlayers, int maxPlayers) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return;
        }
        if (plugin.getDogeBoltManager().nameTaken(name)) {
            Messages.NAME_TAKEN.sendTo(player);
            return;
        }
        if (minPlayers > maxPlayers) {
            Messages.MIN_BIGGED_THAN_MAX.sendTo(commandSender);
            return;
        }
        Inventory inventory = BuildInventories.setupInventory(player.getUniqueId(), name, minPlayers, maxPlayers);
        plugin.getDogeBoltManager().getSetupMap().put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    @Sub
    @Permission("dogebolt.commands.dogebolt.join")
    public void join(CommandSender commandSender) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return;
        }
        plugin.getDogeBoltManager().joinGame(player);
    }

    @Sub
    @Permission("dogebolt.commands.dogebolt.leave")
    public void leave(CommandSender commandSender) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return;
        }
        Optional<DogeBoltGame> optionalGame = plugin.getDogeBoltManager().getGame(player);
        if (optionalGame.isEmpty()) {
            Messages.NOT_IN_A_AME.sendTo(player);
            return;
        }
        plugin.getDogeBoltManager().leaveGame(player, optionalGame.get());
    }

    @Sub
    @Permission("dogebolt.commands.dogebolt.remove")
    @Usage("/dogebol remove <name of game>")
    public void remove(CommandSender commandSender, String game) {
        Optional<DogeBoltGame> dogeBoltGameOptional = plugin.getDogeBoltManager().getGame(game);
        if (dogeBoltGameOptional.isEmpty()) {
            Messages.GAME_NOT_FOUND.sendTo(commandSender);
            return;
        }
        if (dogeBoltGameOptional.get().getGameState() != GameState.ENDED || dogeBoltGameOptional.get().getPlayers().size() != 0) {
            // Send message
            return;
        }
        if (!(commandSender instanceof Player player)) {
            Messages.GAME_REMOVED.sendTo(commandSender);
            plugin.getDataProvider().deleteGame(game);
            return;
        }
        player.openInventory(BuildInventories.deleteInventory(game));
    }
}
