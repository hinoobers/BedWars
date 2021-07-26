package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.hinoob.game.Game;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;

public class BedBreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            Game game = GameManager.findGameByPlayer(e.getPlayer());

            Island island = game.getIslands().stream().filter(p -> p.getBedLocation().distance(e.getBlock().getLocation()) < 5).findAny().get();
            Island playerIsland = game.getIslandByPlayer(e.getPlayer());
            if(e.getBlock().getType().toString().toLowerCase().contains("bed") && e.getBlock().getType() != Material.BEDROCK){
                if(island == playerIsland){
                    e.getPlayer().sendMessage(ChatColor.RED + "You can't break your bed!");
                    e.setCancelled(true);
                }else{
                    game.broadcastMessage(ChatColor.valueOf(island.getColor()) + island.getColor() + ChatColor.GRAY + " team's bed was broken by " + ChatColor.valueOf(playerIsland.getColor()) + e.getPlayer().getName());
                    island.setHasBed(false);
                    e.setDropItems(false);
                }
            }
        }
    }
}
