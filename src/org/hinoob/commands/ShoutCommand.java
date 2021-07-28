package org.hinoob.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hinoob.BedWars;
import org.hinoob.game.Game;
import org.hinoob.game.GameState;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;

public class ShoutCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (GameManager.findGameByPlayer(player) == null) {
                commandSender.sendMessage(ChatColor.RED + "This command can be used only in games!");
            } else {
                Game game =GameManager.findGameByPlayer(player);

                if(game.maxPlayersPerTeam == 1){
                    commandSender.sendMessage(ChatColor.RED + "This command can not be used in solo games!");
                }else{
                    if(strings.length == 0){
                        commandSender.sendMessage(ChatColor.RED + "You can't shout a empty message!");
                    }else{
                        String message = "";
                        for(int i = 0; i < strings.length; i++){
                            message = message + strings[i] + " ";
                        }
                        message = message.trim();

                        String finalMessage = message;
                        game.getGlobalPlayers().forEach(b -> b.sendMessage(ChatColor.DARK_GRAY + "(" + ChatColor.RED + "Shout" + ChatColor.DARK_GRAY + ") " + ChatColor.valueOf(game.getIslandByPlayer((Player)commandSender).getColor()) + commandSender.getName() + ChatColor.GRAY + " : " + ChatColor.WHITE + finalMessage));
                    }
                }
            }
        }else{
            System.out.println("[BedWars] Only for players!");
        }
        return true;
    }

}
