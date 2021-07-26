package org.hinoob.game;

import org.bukkit.*;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.hinoob.BedWars;
import org.hinoob.generator.Generator;
import org.hinoob.generator.GeneratorType;
import org.hinoob.gui.impl.PersonalShopGUI;
import org.hinoob.gui.impl.TeamSelectorGUI;
import org.hinoob.gui.impl.TeamShopGUI;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;
import org.hinoob.map.GameMap;
import org.hinoob.util.ConfigUtil;
import org.hinoob.util.ItemBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private GameState gameState = GameState.WAITING;
    public GameMap gameMap;
    private final String name;

    private List<Player> globalPlayers = new ArrayList<>();
    private List<Island> islands = new ArrayList<>();
    private List<Generator> generators = new ArrayList<>();

    public Map<Player, String> desiredTeam = new HashMap<>();

    public Location spawnLocation;

    private int minPlayers = 2, maxPlayers = 10;
    public int maxPlayersPerTeam;

    private int generatorSpawnTask;

    private TeamSelectorGUI teamSelectorGUI = new TeamSelectorGUI(this);
    private PersonalShopGUI personalShopGUI = new PersonalShopGUI(this);
    private TeamShopGUI teamShopGUI = new TeamShopGUI(this);

    public Game(BedWars bedWars, String configurationKey, String name){
        this.name = name;
        this.gameMap = new GameMap(bedWars.getConfig().getString(configurationKey + ".world"));

        gameMap.load();

        this.minPlayers = bedWars.getConfig().getInt(configurationKey + ".min-players");
        this.maxPlayers = bedWars.getConfig().getInt(configurationKey + ".max-players");
        this.maxPlayersPerTeam = bedWars.getConfig().getInt(configurationKey + ".max-players-per-team");

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
            Generator generator = new Generator(ConfigUtil.processLocation(bedWars.getConfig(), "games." + name + ".generators." + gen + ".location", gameMap.temporaryWorld), GeneratorType.valueOf(bedWars.getConfig().getString("games." + name + ".generators." + gen + ".type")), this);
            generators.add(generator);
        }

        teamSelectorGUI.update();

    }

    public String getName(){
        return name;
    }

    public GameState getState(){
        return gameState;
    }
    public List<Player> getGlobalPlayers(){ return globalPlayers; }
    public List<Island> getIslands() { return islands; }

    public TeamSelectorGUI getTeamSelectorGUI(){
        return teamSelectorGUI;
    }
    public PersonalShopGUI getPersonalShopGUI(){
        return personalShopGUI;
    }
    public TeamShopGUI getTeamShopGUI() { return teamShopGUI; }


    public Island getIslandByPlayer(Player player){
        try {
            return islands.stream().filter(p -> p.getPlayers().contains(player)).findAny().get();
        }catch(Exception e){
            return null;
        }
    }

    private Island getIslandWithLowestMembers(){
        Island island = islands.get(0);

        for(Island islandObject : islands){
            if(islandObject.getPlayers().size() < island.getPlayers().size()){
                island = islandObject;
            }
        }

        return island;
    }

    private Island getRandomAvailableIsland(){
        Island availableIsland = null;

        for(Island island : islands){
            if(island.getPlayers().size() < maxPlayersPerTeam){
                availableIsland = island;
            }
        }

        return availableIsland;
    }


    public void start(){
        if(gameState == GameState.PLAYING) return; // Duplicate fix
        gameState = GameState.PLAYING;

        globalPlayers.stream().forEach(b -> b.getInventory().clear());
        globalPlayers.stream().forEach(b -> b.getInventory().setArmorContents(null));

        for(Player player : globalPlayers){
            if(desiredTeam.containsKey(player)){
                Island island = islands.stream().filter(p -> p.getColor().equalsIgnoreCase(desiredTeam.get(player))).findAny().get();
                island.getPlayers().add(player);
            }
        }

        for(Player player : globalPlayers){
            if(getIslandByPlayer(player) == null){
                Island island = getIslandWithLowestMembers();
                if(island.getPlayers().size() < maxPlayersPerTeam){
                    island.getPlayers().add(player);
                }else{
                    getRandomAvailableIsland().getPlayers().add(player);
                }
            }
        }

        for(Player player : globalPlayers){
            Island island = getIslandByPlayer(player);
            player.sendMessage(ChatColor.valueOf(island.getColor().toUpperCase()) + "You are in " + island.getColor() + " team!");
            player.teleport(island.getSpawnLocation());

            ItemStack woodenSword = new ItemBuilder(Material.WOODEN_SWORD).build();

            player.getInventory().setItem(0, woodenSword);
        }

        broadcastMessage(ChatColor.GREEN + "Game started!");

        startGeneratorTask();
        islands.forEach(Island::start);
        islands.forEach(Island::spawnVillagers);

        for(Island island : islands){
            if(island.getPlayers().size() == 0){
                island.setAlive(false);
                island.setHasBed(false);
                broadcastMessage(ChatColor.valueOf(island.getColor()) + island.getColor() + ChatColor.GRAY + " was eliminated! (No Players)");
            }
        }
        startWinCheck();
    }

    private void startGeneratorTask() {
        generatorSpawnTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.INSTANCE, new Runnable() {
            @Override
            public void run() {
                for(Generator generator : generators){
                    generator.attemptSpawn();
                }
            }
        }, 0L, 0L);
    }

    private int winTask;
    private boolean won = false;
    private void startWinCheck(){
        winTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.INSTANCE, new Runnable() {
            @Override
            public void run() {
                int aliveIslands = (int) islands.stream().filter(Island::isAlive).count();
                if(aliveIslands == 1 && !won){
                    won = true;
                    Island lastTeamAlive = islands.stream().filter(p -> p.isAlive()).findAny().get();

                    broadcastMessage(ChatColor.valueOf(lastTeamAlive.getColor()) + " " + ChatColor.GOLD + "won");

                    new BukkitRunnable(){
                        public void run(){
                            end(true);
                        }
                    }.runTaskLater(BedWars.INSTANCE, 2 * 20);
                }else{
                    if(!won && islands.stream().noneMatch(Island::isHasBed) && globalPlayers.stream().filter(p -> p != null && p.getGameMode() == GameMode.SURVIVAL).count() == 1){ // Nobody has a bed and 1 player is alive
                        won = true;
                        Player lastPlayerAlive = globalPlayers.stream().filter(p -> p != null && p.getGameMode() == GameMode.SURVIVAL).findAny().get();
                        Island lastTeamAlive = islands.stream().filter(p -> p.getPlayers().contains(lastPlayerAlive)).findAny().get();

                        broadcastMessage(ChatColor.valueOf(lastTeamAlive.getColor()) + " " + ChatColor.GOLD + "won");

                        new BukkitRunnable(){
                            public void run(){
                                end(true);
                            }
                        }.runTaskLater(BedWars.INSTANCE, 2 * 20);
                    }
                }
            }
        }, 0L, 0L);
    }

    public void end(boolean nextMatch){
        Bukkit.getScheduler().cancelTask(generatorSpawnTask);
        Bukkit.getScheduler().cancelTask(winTask);

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
            islands.forEach(b -> b.setHasBed(true));
            islands.forEach(b -> b.setAlive(true));
            islands.forEach(Island::end);

            won = false;
        }

        gameState = GameState.WAITING;

    }

    private void giveTeamSelector(Player player){
        ItemStack teamSelector = new ItemBuilder(Material.WHITE_WOOL).setDisplayName(ChatColor.GRAY + "Team Selector").build();

        player.getInventory().addItem(teamSelector);
    }

    public void addPlayer(Player player){
        if(gameState == GameState.PLAYING) return;

        globalPlayers.add(player);

        player.teleport(spawnLocation);

        //Editing player data
        player.getEnderChest().clear();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setHealth(20);
        player.setFoodLevel(20);

        giveTeamSelector(player);
        //Editing player data

        if(globalPlayers.size() >= minPlayers){
            broadcastMessage(ChatColor.GREEN + "Game countdown of 5 seconds started!");

            new BukkitRunnable(){
                public void run(){
                    start();
                }
            }.runTaskLater(BedWars.INSTANCE, 5 * 20);
        }


    }

    public void broadcastMessage(String s) {
        globalPlayers.forEach(b -> b.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "BedWars" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + s));
    }


}
