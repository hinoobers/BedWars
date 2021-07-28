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

public class LeaveGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(strings.length == 0){
                Player player = (Player) commandSender;
                if(GameManager.findGameByPlayer(player) == null){
                    commandSender.sendMessage(ChatColor.RED + "You must join a game first!");
                }else{
                    Game game = GameManager.findGameByPlayer(player);

                    player.sendMessage(ChatColor.GREEN + "Left!");
                    player.setPlayerListName(ChatColor.WHITE + player.getName());
                    player.teleport(BedWars.INSTANCE.getSpawnLocation());

                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);

                    if(game.scoreboardMap.containsKey(player)){
                        game.scoreboardMap.get(player).delete();
                    }

                    if(game.getState() == GameState.WAITING){
                        game.getGlobalPlayers().remove(player);
                        game.broadcastMessage(player.getName() + ChatColor.RED + " left!");
                    }else{
                        if(game.getGlobalPlayers().size() == 2){
                            game.broadcastMessage(ChatColor.RED + "Game ended because " + player.getName() + " left!");
                            new BukkitRunnable(){
                                public void run(){
                                    game.end(true);
                                }
                            }.runTaskLater(BedWars.INSTANCE, 2 * 20);
                        }else{
                            game.getGlobalPlayers().remove(player);
                            Island island = game.getIslandByPlayer(player);
                            if(island.getPlayers().size() - 1 <= 0){
                                game.broadcastMessage(ChatColor.valueOf(island.getColor()) + island.getColor() + ChatColor.GRAY + " was eliminated! (No Players)");
                                island.setAlive(false);
                                island.setHasBed(false);
                            }
                        }
                    }
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
