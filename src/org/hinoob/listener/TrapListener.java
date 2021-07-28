package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.hinoob.game.Game;
import org.hinoob.game.GameState;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;

public class TrapListener implements Listener {

    public Island getIslandNearby(Game game, Player player){
        try {
            return game.getIslands().stream().filter(p -> p.getSpawnLocation().distance(player.getLocation()) < 30).findAny().get();
        }catch(Exception e){
            return null;
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            Game game = GameManager.findGameByPlayer(e.getPlayer());

            if(game.getState() == GameState.WAITING) return;

            Island islandNearby = getIslandNearby(game, e.getPlayer());

            if(islandNearby != null && islandNearby.getPlayers().stream().noneMatch(b -> b.equals(e.getPlayer()))){
                if(islandNearby.isAlarmTrap()){
                    islandNearby.setAlarmTrap(false);
                    islandNearby.getPlayers().stream().forEach(b -> b.sendMessage(ChatColor.RED + "[Alarm Trap] Triggered by " + e.getPlayer().getName()));
                    for(Player player : islandNearby.getPlayers()){
                        player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Alarm Triggered", ChatColor.GRAY + e.getPlayer().getName(), 20, 20, 20);
                    }
                }

                if(islandNearby.isBlindnessTrap()){
                    islandNearby.setBlindnessTrap(false);
                    islandNearby.getPlayers().stream().forEach(b -> b.sendMessage(ChatColor.RED + "[Blindness Trap] Triggered by " + e.getPlayer().getName()));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 5));
                }

                if(islandNearby.isMiningFatigueTrap()){
                    islandNearby.setMiningFatigueTrap(false);
                    islandNearby.getPlayers().stream().forEach(b -> b.sendMessage(ChatColor.RED + "[Mining Fatigue Trap] Triggered by " + e.getPlayer().getName()));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5 * 20, 1));
                }
            }

        }
    }
}
