package org.hinoob.gui.impl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.game.Game;
import org.hinoob.gui.AbstractGUI;
import org.hinoob.util.ItemBuilder;

public class TeamShopGUI implements AbstractGUI {
    private Game game;
    private Inventory inventory;

    public TeamShopGUI(Game game){
        this.game = game;

        this.inventory = Bukkit.createInventory(null, 27, "Team Shop");
    }

    public void init(){
        ItemStack traps = new ItemBuilder(Material.FIREWORK_ROCKET).setDisplayName(ChatColor.GRAY + "Traps").build();
        ItemStack enchantments = new ItemBuilder(Material.ENCHANTED_BOOK).setDisplayName(ChatColor.GRAY + "Enchantments").build();

        inventory.setItem(10, traps);
        inventory.setItem(11, enchantments);
    }


    public void open(Player player){
        init();
        player.openInventory(inventory);
    }
}
