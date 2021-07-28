package org.hinoob.generator;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.hinoob.game.Game;

public class Generator {

    public Location location;
    public GeneratorType generatorType;

    public Game game;

    public int cooldown = 200;
    public int currentCooldown = cooldown;

    private ArmorStand armorStand;

    public Generator(Location location, GeneratorType type, Game game){
        this.location = location;
        this.generatorType = type;

        this.game = game;
    }

    private void spawnArmorStand(){
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0,1,0), EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setCustomName(generatorType == GeneratorType.DIAMOND ? ChatColor.AQUA + "Next diamond spawn: " + (currentCooldown / 20) + "s": ChatColor.GREEN + "Next emerald spawn: " + (currentCooldown / 20));
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);

        this.armorStand = armorStand;
    }

    public void killStand(){
        if(armorStand != null) armorStand.remove();
    }

    private void updateStand(){
        if(armorStand != null){
            armorStand.setCustomName(generatorType == GeneratorType.DIAMOND ? ChatColor.AQUA + "Next diamond spawn: " + (currentCooldown / 20) + "s": ChatColor.GREEN + "Next emerald spawn: " + (currentCooldown / 20));
        }
    }


    public void attemptSpawn(){
        if(armorStand == null) spawnArmorStand();

        cooldown = generatorType == GeneratorType.DIAMOND ? 650 : 1000;

        --currentCooldown;

        if(currentCooldown <= 0){
            // Spawn
            location.getWorld().dropItemNaturally(location, generatorType == GeneratorType.DIAMOND ? new ItemStack(Material.DIAMOND) : new ItemStack(Material.EMERALD));
            currentCooldown = cooldown;



        }

        updateStand();
    }
}
