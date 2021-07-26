package org.hinoob.map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.hinoob.BedWars;
import org.hinoob.util.FileUtil;

import java.io.File;

public class GameMap {

    private String worldName;

    private File sourceWorldDirectory, temporaryWorldDirectory;
    public World temporaryWorld;

    
    public GameMap(String worldName){
        this.worldName = worldName;

        this.sourceWorldDirectory = new File(BedWars.INSTANCE.getServer().getWorldContainer(), worldName);
    }

    public void load(){
        this.temporaryWorldDirectory = new File(
                Bukkit.getWorldContainer().getParentFile(),
                sourceWorldDirectory.getName() + "-temp");

        try {
            FileUtil.copy(sourceWorldDirectory, temporaryWorldDirectory);
        }catch(Exception e){

        }

        if(Bukkit.getWorld(temporaryWorldDirectory.getName()) != null){
            this.temporaryWorld = Bukkit.getWorld(temporaryWorldDirectory.getName());

            temporaryWorld.setAutoSave(false);
        }else{
            this.temporaryWorld = Bukkit.createWorld(new WorldCreator(temporaryWorldDirectory.getName()));

            if(temporaryWorld != null){
                temporaryWorld.setAutoSave(false);
            }
        }

    }

    public void delete(){
        for(Player player : temporaryWorld.getPlayers()){
            player.teleport(BedWars.INSTANCE.getSpawnLocation());
        }

        Bukkit.unloadWorld(temporaryWorld, false);

        FileUtil.delete(temporaryWorldDirectory);

        temporaryWorld = null;

    }
}
