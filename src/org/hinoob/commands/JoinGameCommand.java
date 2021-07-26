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
            Game game = GameManager.findAvailableGame();

            if(game == null){
                commandSender.sendMessage(ChatColor.RED + "Game not found!");
            }else{
                commandSender.sendMessage(ChatColor.GREEN + "Joined!");
                game.addPlayer((Player)commandSender);
            }
        }else{
            System.out.println("[BedWars] Only for players!");
        }
        return true;
    }
}
