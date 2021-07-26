package org.hinoob.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.hinoob.manager.GameManager;

public class WeatherKeepEvent implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e){
        if(GameManager.getGames().stream().anyMatch(b -> b.gameMap.temporaryWorld != null && b.gameMap.temporaryWorld.getName().equalsIgnoreCase(e.getWorld().getName()))){
            e.getWorld().setStorm(false);
            e.getWorld().setTime(1000);
            e.setCancelled(true);
        }
    }
}
