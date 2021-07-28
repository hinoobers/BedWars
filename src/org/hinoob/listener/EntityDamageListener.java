package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.hinoob.BedWars;
import org.hinoob.game.Game;
import org.hinoob.game.GameState;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;
import org.hinoob.util.InventoryUtil;

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
    public void onDeath(PlayerDeathEvent e){
        if(GameManager.findGameByPlayer(e.getEntity()) != null){
            Game game = GameManager.findGameByPlayer(e.getEntity());
            Island island = game.getIslandByPlayer(e.getEntity());

            e.setDeathMessage("");

            if(!island.isHasBed()){
                e.getEntity().sendMessage(ChatColor.RED + "You can't respawn anymore!");
                e.getEntity().spigot().respawn();
                e.getEntity().setGameMode(GameMode.SPECTATOR);
                e.getEntity().teleport(island.getSpawnLocation());

                if(island.getPlayers().stream().allMatch(b -> b == null || b.getGameMode() == GameMode.SPECTATOR)){
                    island.setAlive(false);
                    game.broadcastMessage(ChatColor.valueOf(island.getColor()) + island.getColor() + ChatColor.GRAY + " was eliminated!");
                }
            }else{
                e.getEntity().sendMessage(ChatColor.RED + "You'll respawn in 5 seconds");
                e.getEntity().setGameMode(GameMode.SPECTATOR);
                e.getEntity().teleport(island.getSpawnLocation());
                e.getEntity().spigot().respawn();
                e.getEntity().teleport(island.getSpawnLocation());

                new BukkitRunnable(){
                    public void run(){
                        e.getEntity().teleport(island.getSpawnLocation());
                        e.getEntity().setGameMode(GameMode.SURVIVAL);

                        e.getEntity().sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Respawned", "", 20, 20, 20);

                        e.getEntity().setFoodLevel(20);
                        e.getEntity().setHealth(20);

                        e.getEntity().getInventory().clear();
                        e.getEntity().getInventory().setArmorContents(null);

                        InventoryUtil.giveFullArmor(island, e.getEntity(), game.stringToColor(island.getColor()));

                        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
                        if(island.isHasSharp()){
                            sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        }
                        e.getEntity().getInventory().setItem(0, sword);
                    }
                }.runTaskLater(BedWars.INSTANCE, 5 * 20);
            }

            if(e.getEntity().getKiller() == null){
                game.broadcastMessage(ChatColor.valueOf(island.getColor()) + e.getEntity().getName() + " " + ChatColor.GRAY + "died!");
            }else{
                game.broadcastMessage(ChatColor.valueOf(island.getColor()) + e.getEntity().getName() + " " + ChatColor.GRAY + "was slain by " + ChatColor.valueOf(game.getIslandByPlayer(e.getEntity().getKiller()).getColor()) + e.getEntity().getKiller().getName());
            }

            e.getDrops().clear();
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null && e.getTo().getY() < -15 && e.getPlayer().getFallDistance() > 2F && e.getPlayer().getGameMode() == GameMode.SURVIVAL){
            Game game = GameManager.findGameByPlayer(e.getPlayer());
            if(game.getState() == GameState.WAITING || game.won) return;
            Island island = game.getIslandByPlayer(e.getPlayer());


            game.broadcastMessage(ChatColor.valueOf(island.getColor()) + e.getPlayer().getName() + ChatColor.GRAY + " fell to the void!");

            if(!island.isHasBed()){
                e.getPlayer().sendMessage(ChatColor.RED + "You can't respawn anymore!");
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
                e.getPlayer().teleport(island.getSpawnLocation());


                if(island.getPlayers().stream().allMatch(b -> b == null || b.getGameMode() == GameMode.SPECTATOR)){
                    island.setAlive(false);
                    game.broadcastMessage(ChatColor.valueOf(island.getColor()) + island.getColor() + ChatColor.GRAY + " was eliminated!");
                }
            }else{
                e.getPlayer().sendMessage(ChatColor.RED + "You'll respawn in 5 seconds");
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
                e.getPlayer().teleport(island.getSpawnLocation());

                new BukkitRunnable(){
                    public void run(){
                        e.getPlayer().teleport(island.getSpawnLocation());
                        e.getPlayer().setGameMode(GameMode.SURVIVAL);

                        e.getPlayer().sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Respawned", "", 20, 20, 20);

                        e.getPlayer().setFoodLevel(20);
                        e.getPlayer().setHealth(20);

                        e.getPlayer().getInventory().clear();
                        e.getPlayer().getInventory().setArmorContents(null);

                        InventoryUtil.giveFullArmor(island, e.getPlayer(), game.stringToColor(island.getColor()));

                        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
                        if(island.isHasSharp()){
                            sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        }
                        e.getPlayer().getInventory().setItem(0, sword);
                    }
                }.runTaskLater(BedWars.INSTANCE, 5 * 20);
            }



            game.updateScoreboardStatus();


        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && GameManager.findGameByPlayer((Player)e.getDamager()) != null){
            Game game = GameManager.findGameByPlayer((Player)e.getDamager());
            if(game.getState() == GameState.PLAYING) return;

            e.getDamager().sendMessage(ChatColor.RED + "You can't do that yet!");
            e.setCancelled(true);

        }
    }
}
