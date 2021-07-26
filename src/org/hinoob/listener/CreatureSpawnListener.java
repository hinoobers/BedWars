package org.hinoob.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.hinoob.manager.GameManager;

public class CreatureSpawnListener implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e){
        if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL && GameManager.getGames().stream().anyMatch(b -> e.getEntity().getWorld().getName().equalsIgnoreCase(b.gameMap.temporaryWorld.getName()))){

            e.setCancelled(true);
        }
    }
}
