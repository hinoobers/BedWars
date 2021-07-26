package org.hinoob.island;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Island {

    private String color;
    private Location teamShopLocation, personalShopLocation;
    private Location spawnLocation, resourceSpawnLocation;
    private Location bedLocation;

    private List<Player> players = new ArrayList<>();

    public void setWorlds(World world){
        teamShopLocation.setWorld(world);
        personalShopLocation.setWorld(world);
        spawnLocation.setWorld(world);
        resourceSpawnLocation.setWorld(world);
        bedLocation.setWorld(world);
    }
}
