package org.hinoob.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

@UtilityClass
public class ConfigUtil {


    public Location processLocation(FileConfiguration configuration, String key, World world){
        double x = configuration.getDouble(key + ".x");
        double y = configuration.getDouble(key + ".y");
        double z = configuration.getDouble(key + ".z");

        double yaw, pitch;
        if(configuration.get(key + ".yaw") != null && configuration.get(key + ".pitch") != null){
            yaw = configuration.getDouble(key + ".yaw");
            pitch = configuration.getDouble(key + ".pitch");
            return new Location(world, x, y, z, (float)yaw, (float)pitch);
        }


        return new Location(world, x, y, z);
    }
}
