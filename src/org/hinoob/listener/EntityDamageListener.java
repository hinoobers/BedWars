package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.hinoob.BedWars;
import org.hinoob.game.Game;
import org.hinoob.game.GameState;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;
import org.hinoob.util.HitData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onFriendlyFire(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player player = (Player) e.getDamager();
            Player target = (Player) e.getEntity();
            if(GameManager.findGameByPlayer(player) != null && GameManager.findGameByPlayer(target) != null){
                //Friendly fire
                Game game = GameManager.findGameByPlayer(player);
                if(game.getState() == GameState.WAITING) return;

                if(game == GameManager.findGameByPlayer(target)){
                    // They're on the same game

                    Island island = game.getIslandByPlayer(player);

                    if(island.getPlayers().contains(target)){

                        //They're on the same team

                        player.sendMessage(ChatColor.RED + "You can't hit somebody in your team!");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Player player = (Player) e.getDamager();
            if(GameManager.findGameByPlayer(player) != null){
                Game game = GameManager.findGameByPlayer(player);
                if(game.getState() == GameState.WAITING) return;

                if(game.getIslands().stream().anyMatch(b -> b.getPersonalShopVillager().equals(e.getEntity()) || b.getTeamShopVillager().equals(e.getEntity()))){
                    // They're trying to hit the shop npcs
                    e.setCancelled(true);
                }
            }
        }
    }

    public boolean isResource(ItemStack itemStack){
        if(itemStack.getType() == Material.EMERALD ||
                itemStack.getType() == Material.DIAMOND ||
                itemStack.getType() == Material.IRON_INGOT ||
                itemStack.getType() == Material.GOLD_INGOT){
            return true;
        }else{
            return false;
        }
    }

    public ChatColor getResourceColor(ItemStack stack){
        if(!isResource(stack)) return null;

        if(stack.getType() == Material.EMERALD){
            return ChatColor.GREEN;
        }else if(stack.getType() == Material.IRON_INGOT){
            return ChatColor.GRAY;
        }else if(stack.getType() == Material.GOLD_INGOT){
            return ChatColor.GOLD;
        }else if(stack.getType() == Material.DIAMOND) {
            return ChatColor.AQUA;
        }else{
            return null;
        }
    }

    @EventHandler
    public void onMove(PlayerDeathEvent e){
        if(GameManager.findGameByPlayer(e.getEntity()) != null){
            Game game = GameManager.findGameByPlayer(e.getEntity());

            Island island = game.getIslandByPlayer(e.getEntity());

            e.setDeathMessage("");

            if(!island.isHasBed()){
                e.getEntity().spigot().respawn();
                e.getEntity().setGameMode(GameMode.SPECTATOR);

                e.getEntity().teleport(island.getSpawnLocation());

                if(e.getEntity().getKiller() == null) {
                    game.broadcastMessage(ChatColor.valueOf(island.getColor()) + e.getEntity().getName() + ChatColor.GRAY + " died!");
                }else{
                    game.broadcastMessage(ChatColor.valueOf(island.getColor()) + e.getEntity().getName() + ChatColor.GRAY + " was slain by " + ChatColor.valueOf(game.getIslandByPlayer(e.getEntity().getKiller()) + e.getEntity().getKiller().getName()));
                }
                e.getEntity().sendMessage(ChatColor.RED + "You can't respawn anymore!");

                if(e.getEntity().getKiller() != null){
                    List<ItemStack> drops = e.getDrops().stream().filter(this::isResource).collect(Collectors.toList());
                    if(drops.size() > 0){
                        e.getEntity().getKiller().sendMessage(ChatColor.GRAY + "Loot of " + e.getEntity().getName());
                        for(ItemStack drop : drops){
                            e.getEntity().getKiller().sendMessage(getResourceColor(drop) + " - " + drop.getType().name() + " x" + drop.getAmount());
                            e.getEntity().getKiller().getInventory().addItem(drop);
                        }
                    }
                }
                e.getDrops().clear();

                if(game.getIslandByPlayer(e.getEntity()).getPlayers().stream().allMatch(b -> b == null || b.getGameMode() == GameMode.SPECTATOR)){
                    game.broadcastMessage(ChatColor.valueOf(game.getIslandByPlayer(e.getEntity()).getColor()) + game.getIslandByPlayer(e.getEntity()).getColor() + ChatColor.GRAY + " was eliminated!");
                }

            }else{

                e.getEntity().spigot().respawn();
                e.getEntity().setGameMode(GameMode.SPECTATOR);

                e.getEntity().teleport(island.getSpawnLocation());

                if(e.getEntity().getKiller() == null) {
                    game.broadcastMessage(ChatColor.valueOf(island.getColor()) + e.getEntity().getName() + ChatColor.GRAY + " died!");
                }else{
                    game.broadcastMessage(ChatColor.valueOf(island.getColor()) + e.getEntity().getName() + ChatColor.GRAY + " was slain by " + ChatColor.valueOf(game.getIslandByPlayer(e.getEntity().getKiller()) + e.getEntity().getKiller().getName()));
                }
                e.getEntity().sendMessage(ChatColor.RED + "You'll respawn in 5 seconds if your bed is still there!");

                if(e.getEntity().getKiller() != null){
                    List<ItemStack> drops = e.getDrops().stream().filter(this::isResource).collect(Collectors.toList());
                    if(drops.size() > 0){
                        e.getEntity().getKiller().sendMessage(ChatColor.GRAY + "Loot of " + e.getEntity().getName());
                        for(ItemStack drop : drops){
                            e.getEntity().getKiller().sendMessage(getResourceColor(drop) + " - " + drop.getType().name() + " x" + drop.getAmount());
                            e.getEntity().getKiller().getInventory().addItem(drop);
                        }
                    }
                }
                e.getDrops().clear();

                new BukkitRunnable(){
                    public void run(){
                        if(game.getIslandByPlayer(e.getEntity()).isHasBed()){
                            e.getEntity().teleport(game.getIslandByPlayer(e.getEntity()).getSpawnLocation());
                            e.getEntity().setGameMode(GameMode.SURVIVAL);
                        }else{
                            e.getEntity().sendMessage(ChatColor.RED + "Respawn cancelled, your bed is gone.");
                        }
                    }
                }.runTaskLater(BedWars.INSTANCE, 5 * 20);
            }
        }
    }

    public Map<Player, HitData> lastHit = new HashMap<>();
    @EventHandler
    public void onLastCallback(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            lastHit.put((Player)e.getEntity(), new HitData(e.getDamager(), System.currentTimeMillis()));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null && e.getTo().getY() < 0 && e.getPlayer().getGameMode() == GameMode.SURVIVAL){
            Game game = GameManager.findGameByPlayer(e.getPlayer());

            Player killer = null;
            if(lastHit.containsKey(e.getPlayer())){
                HitData hitData = lastHit.get(e.getPlayer());
                if(Math.abs(System.currentTimeMillis() - hitData.timeStamp) < 3000){
                    killer = (Player)hitData.entity;
                }
            }

            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().teleport(game.getIslandByPlayer(e.getPlayer()).getSpawnLocation());

            if(killer != null){
                game.broadcastMessage(ChatColor.valueOf(game.getIslandByPlayer(e.getPlayer()).getColor()) + e.getPlayer().getName() + ChatColor.GRAY + " was thrown to the void by " + ChatColor.valueOf(game.getIslandByPlayer(killer).getColor()) + killer.getName());

                if(Arrays.stream(e.getPlayer().getInventory().getContents()).anyMatch(b -> b != null && isResource(b))){
                    killer.sendMessage(ChatColor.GRAY + "Loot of " + e.getPlayer().getName());
                    for(ItemStack drop : e.getPlayer().getInventory().getContents()){
                        if(drop != null && isResource(drop)){
                            killer.sendMessage(getResourceColor(drop) + " - " + drop.getType().name() + " x" + drop.getAmount());
                            killer.getInventory().addItem(drop);
                        }
                    }
                }
            }else{
                game.broadcastMessage(ChatColor.valueOf(game.getIslandByPlayer(e.getPlayer()).getColor()) + e.getPlayer().getName() + ChatColor.GRAY + " fell to the void!");
            }

            if(!game.getIslandByPlayer(e.getPlayer()).isHasBed()){
                e.getPlayer().sendMessage(ChatColor.RED + "You can't respawn anymore!");

                if(game.getIslandByPlayer(e.getPlayer()).getPlayers().stream().allMatch(b -> b == null || b.getGameMode() == GameMode.SPECTATOR)){
                    game.broadcastMessage(ChatColor.valueOf(game.getIslandByPlayer(e.getPlayer()).getColor()) + game.getIslandByPlayer(e.getPlayer()).getColor() + ChatColor.GRAY + " was eliminated!");
                }
            }else{
                e.getPlayer().sendMessage(ChatColor.RED + "You'll respawn in 5 seconds if your bed is still there!");

                new BukkitRunnable(){
                    public void run(){
                        if(!game.getIslandByPlayer(e.getPlayer()).isHasBed()){
                            e.getPlayer().sendMessage(ChatColor.RED + "Respawn cancelled, your bed is gone.");
                        }else{
                            e.getPlayer().teleport(game.getIslandByPlayer(e.getPlayer()).getSpawnLocation());
                            e.getPlayer().setGameMode(GameMode.SURVIVAL);
                        }
                    }
                }.runTaskLater(BedWars.INSTANCE, 5 * 20);
            }


        }
    }
}
