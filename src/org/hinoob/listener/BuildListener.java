package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.hinoob.game.Game;
import org.hinoob.game.GameState;
import org.hinoob.manager.GameManager;

public class BuildListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            Game game = GameManager.findGameByPlayer(e.getPlayer());
            if(game.getState() == GameState.WAITING) return; // They shouldn't have blocks anyway, and team selector placing is already cancelled

            game.blocksPlaced.add(e.getBlock());
        }
    }

    @EventHandler()
    public void onBreak(BlockBreakEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            Game game = GameManager.findGameByPlayer(e.getPlayer());
            if (game.getState() == GameState.WAITING) {
                e.getPlayer().sendMessage(ChatColor.RED + "You can't do that yet!");
                e.setCancelled(true);
            }
            if(!game.blocksPlaced.contains(e.getBlock())){

                boolean isBed = e.getBlock().getType().toString().toLowerCase().contains("bed") && e.getBlock().getType() != Material.BEDROCK;

                if(!isBed && e.getBlock().getType() != Material.FIRE){
                    e.getPlayer().sendMessage(ChatColor.RED + "You can only destroy player placed blocks!");
                    e.setCancelled(true);
                }
            }
        }
    }
}
