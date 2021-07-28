package org.hinoob.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hinoob.game.Game;
import org.hinoob.manager.GameManager;
import org.hinoob.map.GameMap;

public class JoinGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(strings.length == 0){
                commandSender.sendMessage(ChatColor.RED + "You must provide a mode!");
            }else if(strings.length == 1){
                if(strings[0].equalsIgnoreCase("solo") || strings[0].equalsIgnoreCase("duo") || strings[0].equalsIgnoreCase("trio") || strings[0].equalsIgnoreCase("squads")){

                    if(GameManager.findGameByPlayer((Player)commandSender) != null){
                        commandSender.sendMessage(ChatColor.RED + "You're already in a game (Execute /leavegame to leave!)");
                        return true;
                    }

                    Game game = GameManager.findAvailableGame(strings[0]);

                    if(game == null){
                        commandSender.sendMessage(ChatColor.RED + "Game not found!");
                    }else{
                        commandSender.sendMessage(ChatColor.GREEN + "Joined!");
                        game.addPlayer((Player)commandSender);
                    }
                }else{
                    commandSender.sendMessage(ChatColor.RED + "Invalid mode!");
                }
            }else{
                commandSender.sendMessage(ChatColor.RED + "Invalid argument(s)!");
            }
        }else{
            System.out.println("[BedWars] Only for players!");
        }
        return true;
    }
}
