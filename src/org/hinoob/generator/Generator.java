package org.hinoob.generator;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.hinoob.game.Game;

public class Generator {

    public Location location;
    public GeneratorType generatorType;

    public Game game;

    public int cooldown = 200;
    public int currentCooldown = cooldown;

    public Generator(Location location, GeneratorType type, Game game){
        this.location = location;
        this.generatorType = type;

        this.game = game;
    }


    public void attemptSpawn(){
        cooldown = generatorType == GeneratorType.DIAMOND ? 650 : 1000;

        --currentCooldown;

        if(currentCooldown <= 0){
            // Spawn
            location.getWorld().dropItemNaturally(location, generatorType == GeneratorType.DIAMOND ? new ItemStack(Material.DIAMOND) : new ItemStack(Material.EMERALD));
            currentCooldown = cooldown;



        }
    }
}
