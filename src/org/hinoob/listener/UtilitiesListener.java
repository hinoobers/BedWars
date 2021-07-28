package org.hinoob.listener;

import org.bukkit.Material;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.hinoob.game.Game;
import org.hinoob.manager.GameManager;

public class UtilitiesListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            Game game = GameManager.findGameByPlayer(e.getPlayer());

            if(e.getBlock().getType() == Material.TNT){
                e.getBlock().setType(Material.AIR);

                e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem() != null && e.getItem().getType() == Material.FIRE_CHARGE && GameManager.findGameByPlayer(e.getPlayer()) != null){
            e.getItem().setAmount(e.getItem().getAmount() - 1);

            Fireball fireball = e.getPlayer().launchProjectile(Fireball.class);
            fireball.setVelocity(e.getPlayer().getLocation().getDirection().multiply(2));
            fireball.setIsIncendiary(false);
        }
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e){
        if(GameManager.findGameByWorld(e.getEntity().getWorld()) != null){
            Game game = GameManager.findGameByWorld(e.getEntity().getWorld());

            e.blockList().removeIf(b -> !game.blocksPlaced.contains(b));
        }
    }
}
