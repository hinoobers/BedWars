package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.hinoob.game.Game;
import org.hinoob.manager.GameManager;

public class ShopOpenListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            Game game = GameManager.findGameByPlayer(e.getPlayer());

            String displayName = ChatColor.stripColor(e.getRightClicked().getCustomName());
            if (displayName.equalsIgnoreCase("Personal Shop")) {
                game.getPersonalShopGUI().open(e.getPlayer());
                e.setCancelled(true);
            }else if(displayName.equalsIgnoreCase("Team Shop")){
                game.getTeamShopGUI().open(e.getPlayer());
            }
        }
    }
}
