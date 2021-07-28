package org.hinoob.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.hinoob.manager.GameManager;

public class InventoryBlockListener implements Listener {

    @EventHandler
    public void onInteract(InventoryClickEvent e){
        if(GameManager.findGameByPlayer((Player) e.getWhoClicked()) != null){
            if(e.getSlotType() == InventoryType.SlotType.ARMOR){
                e.setCancelled(true);

                // Not allowing them to take off their armor
            }
        }
    }
}
