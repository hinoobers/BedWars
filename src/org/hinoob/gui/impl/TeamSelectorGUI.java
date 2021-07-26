package org.hinoob.gui.impl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hinoob.game.Game;
import org.hinoob.gui.AbstractGUI;
import org.hinoob.island.Island;
import org.hinoob.util.ItemBuilder;

import java.util.ArrayList;
import java.util.Locale;

public class TeamSelectorGUI implements AbstractGUI {

    private Game game;
    private Inventory inventory;

    public TeamSelectorGUI(Game game){
        this.game = game;

        this.inventory = Bukkit.createInventory(null, 27, "Team Selector");

        update();
    }

    public void update(){
        inventory.clear();
        for(Island island : game.getIslands()){
            ItemStack itemStack = new ItemBuilder(Material.valueOf(island.getColor().toUpperCase() + "_WOOL")).setDisplayName(ChatColor.valueOf(island.getColor().toUpperCase()) + island.getColor() + " team").build();
            ItemMeta itemMeta = itemStack.getItemMeta();
            ArrayList<String> strings = new ArrayList<>();

            for(Player player : game.getGlobalPlayers()){
                if(game.desiredTeam.containsKey(player) && game.desiredTeam.get(player).equalsIgnoreCase(island.getColor())){
                    strings.add(ChatColor.GRAY + " - " + player.getName());
                }
            }
            itemMeta.setLore(strings);

            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }
    }

    public void open(Player player){
        player.openInventory(inventory);
    }
}
