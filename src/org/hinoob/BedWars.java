package org.hinoob;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.type.Bed;
import org.bukkit.plugin.java.JavaPlugin;
import org.hinoob.commands.BedWarsCommand;
import org.hinoob.commands.JoinGameCommand;
import org.hinoob.game.Game;
import org.hinoob.manager.GameManager;
import org.hinoob.map.GameMap;
import org.hinoob.setup.SetupDataListener;

import java.io.File;
import java.util.Objects;

public class BedWars extends JavaPlugin {


    public static BedWars INSTANCE;
    public File activeMapsFolder = new File(this.getDataFolder() + "/maps");


    @Override
    public void onEnable(){
        INSTANCE = this;
        this.saveDefaultConfig();

        if(!activeMapsFolder.exists()){
            activeMapsFolder.mkdirs();
        }



        System.out.println("[BedWars] Enabled");

        // Registering events
        this.getServer().getPluginManager().registerEvents(new SetupDataListener(), this);

        // Registering commands
        getCommand("joingame").setExecutor(new JoinGameCommand());
        getCommand("bedwars").setExecutor(new BedWarsCommand());

        System.out.println("[BedWars] Loading games");

        if(this.getConfig().getConfigurationSection("games") == null){
            //No games to load
            System.out.println("[BedWars] All game(s) loaded");
            return;
        }
        for(String gameName : this.getConfig().getConfigurationSection("games").getKeys(false)){
            Game game = new Game(this, "games." + gameName, gameName);

            GameManager.addGame(game);

            System.out.println("[BedWars] Game called " + gameName + " added!");
        }

        System.out.println("[BedWars] All game(s) loaded");
    }

    public void onDisable(){
        for(Game game : GameManager.getGames()){
            game.end(false);
        }
    }

    public Location getSpawnLocation(){
        World world = Bukkit.getWorld(this.getConfig().getString("spawn-location.world"));

        double x = this.getConfig().getDouble("spawn-location.x");
        double y = this.getConfig().getDouble("spawn-location.y");
        double z = this.getConfig().getDouble("spawn-location.z");

        double yaw = this.getConfig().getDouble("spawn-location.yaw");
        double pitch = this.getConfig().getDouble("spawn-location.pitch");

        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }
}
