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

public class PersonalShopGUI implements AbstractGUI {

    private Game game;
    private Inventory inventory;

    private Player opened = null;

    public PersonalShopGUI(Game game){
        this.game = game;

        this.inventory = Bukkit.createInventory(null, 9 * 3, "Personal Shop");

    }

    public void init(){
        ItemStack blocks = new ItemBuilder(Material.valueOf(game.getIslandByPlayer(opened).getColor() + "_WOOL")).setDisplayName(ChatColor.GRAY + "Blocks").build();
        ItemStack swords = new ItemBuilder(Material.WOODEN_SWORD).setDisplayName(ChatColor.GRAY + "Swords").build();
        ItemStack tools = new ItemBuilder(Material.STONE_PICKAXE).setDisplayName(ChatColor.GRAY + "Tools").build();
        ItemStack utilities = new ItemBuilder(Material.TNT).setDisplayName(ChatColor.GRAY + "Utilities").build();

        inventory.setItem(10, blocks);
        inventory.setItem(11, swords);
        inventory.setItem(12, tools);
        inventory.setItem(13, utilities);
    }


    @Override
    public void open(Player player) {
        opened = player;
        init();
        player.openInventory(inventory);

    }
}
