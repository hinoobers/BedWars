package org.hinoob.island;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hinoob.BedWars;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Island {

    private String color;
    private Location teamShopLocation, personalShopLocation;
    private Location spawnLocation, resourceSpawnLocation;
    private Location bedLocation;

    private boolean hasBed = true, alive = true;

    int resourceSpawnTask;
    int ironCooldown = 15, goldCooldown = 150;

    private List<Player> players = new ArrayList<>();

    public void setWorlds(World world){
        teamShopLocation.setWorld(world);
        personalShopLocation.setWorld(world);
        spawnLocation.setWorld(world);
        resourceSpawnLocation.setWorld(world);
        bedLocation.setWorld(world);
    }

    public void start(){
        resourceSpawnTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.INSTANCE, new Runnable() {
            @Override
            public void run() {
                --ironCooldown;
                --goldCooldown;

                if(ironCooldown <= 0){
                    resourceSpawnLocation.getWorld().dropItemNaturally(resourceSpawnLocation, new ItemStack(Material.IRON_INGOT));
                    ironCooldown = 15;
                }

                if(goldCooldown <= 0){
                    resourceSpawnLocation.getWorld().dropItemNaturally(resourceSpawnLocation, new ItemStack(Material.GOLD_INGOT));
                    goldCooldown = 50;
                }
            }
        }, 0L, 0L);
    }

    public void end(){
        Bukkit.getScheduler().cancelTask(resourceSpawnTask);
    }

    private LivingEntity personalShopVillager, teamShopVillager;

    public void spawnVillagers(){
        personalShopVillager = (LivingEntity) personalShopLocation.getWorld().spawnEntity(personalShopLocation, EntityType.VILLAGER);
        teamShopVillager = (LivingEntity) teamShopLocation.getWorld().spawnEntity(teamShopLocation, EntityType.VILLAGER);

        personalShopVillager.setAI(false);
        teamShopVillager.setAI(false);

        personalShopVillager.setCustomName(ChatColor.RED + "Personal Shop");
        teamShopVillager.setCustomName(ChatColor.RED + "Team Shop");
    }
}
