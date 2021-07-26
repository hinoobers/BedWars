package org.hinoob.manager;

import lombok.experimental.UtilityClass;
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

    public Game findAvailableGame(){
        try {
            return games.stream().filter(p -> p.getState() == GameState.WAITING).findFirst().get();
        }catch(Exception e){
            // Game not found
            return null;
        }
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
