package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.hinoob.game.Game;
import org.hinoob.game.GameState;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            Game game = GameManager.findGameByPlayer(e.getPlayer());

            if(game.getState() == GameState.PLAYING){
                Island island = game.getIslandByPlayer(e.getPlayer());
                if(game.maxPlayersPerTeam == 1){
                    // Solo, no need to do team chat
                    e.setFormat(ChatColor.valueOf(island.getColor()) + e.getPlayer().getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + e.getMessage());
                }else{

                    e.setCancelled(true);

                    island.getPlayers().forEach(b -> b.sendMessage(ChatColor.DARK_GRAY + "(" + ChatColor.RED + "Team Chat" + ChatColor.DARK_GRAY + ") " + ChatColor.valueOf(island.getColor()) + e.getPlayer().getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + e.getMessage()));
                }
            }
        }
    }
}
