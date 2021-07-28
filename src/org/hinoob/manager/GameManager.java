package org.hinoob.manager;

import lombok.experimental.UtilityClass;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.hinoob.game.Game;
import org.hinoob.game.GameState;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class GameManager {

    private List<Game> games = new ArrayList<>();

    public void destroyAll(){
        games.clear();
    }

    public void addGame(Game game){
        games.add(game);
    }

    public List<Game> getGames(){
        return games;
    }

    private int getIntFromMode(String mode){
        int a = 0;

        if(mode.equalsIgnoreCase("solo")){
            a = 1;
        }else if(mode.equalsIgnoreCase("duo")){
            a = 2;
        }else if(mode.equalsIgnoreCase("trio")){
            a = 3;
        }else if(mode.equalsIgnoreCase("squad")){
            a = 4;
        }

        return a;
    }

    public Game findAvailableGame(String mode){
        try {

            return games.stream().filter(p -> p.getState() == GameState.WAITING && p.maxPlayersPerTeam == getIntFromMode(mode)).findFirst().get();
        }catch(Exception e){
            // Game not found
            return null;
        }
    }

    public Game findGameByWorld(World world){
        for(Game game : getGames()){
            if(game.gameMap.temporaryWorld.equals(world)){
                return game;
            }
        }

        return null;
    }

    public Game findGameByPlayer(Player player){
        try {

            return games.stream().filter(p -> p.getGlobalPlayers().stream().anyMatch(b -> b.equals(player))).findAny().get();
        }catch(Exception e){

            return null;
        }
    }

    public Game findGameByName(String name, boolean ignoreCase){
        try {
            if(ignoreCase){
                return games.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findAny().get();
            }else{
                return games.stream().filter(p -> p.getName().equals(name)).findAny().get();
            }
        }catch(Exception e){
            // Game not found
            return null;
        }
    }
}
