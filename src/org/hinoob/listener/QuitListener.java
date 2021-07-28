package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.hinoob.BedWars;
import org.hinoob.game.Game;
import org.hinoob.game.GameState;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;

public class QuitListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            Game game = GameManager.findGameByPlayer(e.getPlayer());

            e.getPlayer().teleport(BedWars.INSTANCE.getSpawnLocation());

            if(game.scoreboardMap.containsKey(e.getPlayer())){
                game.scoreboardMap.get(e.getPlayer()).delete();
            }

            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setArmorContents(null);

            if(game.getState() == GameState.WAITING){
                game.getGlobalPlayers().remove(e.getPlayer());
                game.broadcastMessage(e.getPlayer().getName() + ChatColor.RED + " left!");
            }else{
                if(game.getGlobalPlayers().size() == 2){
                    game.broadcastMessage(ChatColor.RED + "Game ended because " + e.getPlayer().getName() + " left!");
                    new BukkitRunnable(){
                        public void run(){
                            game.end(true);
                        }
                    }.runTaskLater(BedWars.INSTANCE, 2 * 20);
                }else{
                    game.getGlobalPlayers().remove(e.getPlayer());
                    Island island = game.getIslandByPlayer(e.getPlayer());
                    if(island.getPlayers().size() - 1 <= 0){
                        game.broadcastMessage(ChatColor.valueOf(island.getColor()) + island.getColor() + ChatColor.GRAY + " was eliminated! (No Players)");
                        island.setAlive(false);
                        island.setHasBed(false);
                    }
                }
            }

            game.updateScoreboard("Waiting: " + game.getGlobalPlayers().size() + "/" + game.minPlayers);
        }
    }
}
