package org.hinoob.game;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hinoob.BedWars;
import org.hinoob.generator.Generator;
import org.hinoob.generator.GeneratorType;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;
import org.hinoob.map.GameMap;
import org.hinoob.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private GameState gameState = GameState.WAITING;
    private GameMap gameMap;
    private final String name;

    private List<Player> globalPlayers = new ArrayList<>();
    private List<Island> islands = new ArrayList<>();
    private List<Generator> generators = new ArrayList<>();

    private Location spawnLocation;

    private int minPlayers = 2, maxPlayers = 10;

    public Game(BedWars bedWars, String configurationKey, String name){
        this.name = name;
        this.gameMap = new GameMap(bedWars.getConfig().getString(configurationKey + ".world"));

        gameMap.load();

        this.minPlayers = bedWars.getConfig().getInt(configurationKey + ".min-players");
        this.maxPlayers = bedWars.getConfig().getInt(configurationKey + ".max-players");

        this.spawnLocation = ConfigUtil.processLocation(bedWars.getConfig(), "games." + name + ".spawn-location", gameMap.temporaryWorld);

        for(String islandColor : bedWars.getConfig().getConfigurationSection("games." + name + ".islands").getKeys(false)){
            Island island = new Island();
            island.setColor(islandColor);
            island.setBedLocation(ConfigUtil.processLocation(bedWars.getConfig(), "games." + name + ".islands." + islandColor + ".bed-location", gameMap.temporaryWorld));
            island.setResourceSpawnLocation(ConfigUtil.processLocation(bedWars.getConfig(), "games." + name + ".islands." + islandColor + ".resource-spawn-location", gameMap.temporaryWorld));
            island.setSpawnLocation(ConfigUtil.processLocation(bedWars.getConfig(), "games." + name + ".islands." + islandColor + ".spawn-location", gameMap.temporaryWorld));
            island.setTeamShopLocation(ConfigUtil.processLocation(bedWars.getConfig(), "games." + name + ".islands." + islandColor + ".team-shop-location", gameMap.temporaryWorld));
            island.setPersonalShopLocation(ConfigUtil.processLocation(bedWars.getConfig(), "games." + name + ".islands." + islandColor + ".personal-shop-location", gameMap.temporaryWorld));
            islands.add(island);
        }

        for(String gen : bedWars.getConfig().getConfigurationSection("games." + name + ".generators").getKeys(false)){
            Generator generator = new Generator(ConfigUtil.processLocation(bedWars.getConfig(), "games." + name + ".generators." + gen + ".location", gameMap.temporaryWorld), GeneratorType.valueOf(bedWars.getConfig().getString("games." + name + ".generators." + gen + ".type")));
            generators.add(generator);
        }

    }

    public String getName(){
        return name;
    }

    public GameState getState(){
        return gameState;
    }
    public List<Player> getGlobalPlayers(){ return globalPlayers; }

    public void start(){

        for(Player player : globalPlayers){
            //TODO: Team checking etc
        }

        broadcastMessage(ChatColor.GREEN + "Game started!");
    }

    public void end(boolean nextMatch){
        if(!nextMatch){
            gameMap.delete();

            // Called on onDisable() ^^
            // No need to reset values because they're gonna be reset anyways
        }else {
            gameMap.delete();
            gameMap.load();

            spawnLocation.setWorld(gameMap.temporaryWorld);
            islands.forEach(b -> b.setWorlds(gameMap.temporaryWorld));
            generators.forEach(b -> b.location.setWorld(gameMap.temporaryWorld));

            globalPlayers.clear();
            islands.forEach(b -> b.getPlayers().clear());
        }

    }

    public void addPlayer(Player player){
        if(gameState == GameState.PLAYING) return;

        globalPlayers.add(player);

        player.teleport(spawnLocation);

        if(globalPlayers.size() >= minPlayers){
            broadcastMessage(ChatColor.GREEN + "Game countdown of 5 seconds started!");

            new BukkitRunnable(){
                public void run(){
                    start();
                }
            }.runTaskLater(BedWars.INSTANCE, 5 * 20);
        }


    }

    private void broadcastMessage(String s) {
        globalPlayers.forEach(b -> b.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + s));
    }


}
