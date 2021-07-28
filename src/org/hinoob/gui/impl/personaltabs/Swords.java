package org.hinoob.gui.impl.personaltabs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.util.ItemBuilder;

public class Swords {

    public static Inventory get(){
        Inventory swordsGUI = Bukkit.createInventory(null, 27, "Swords");

        ItemStack stoneSword = new ItemBuilder(Material.STONE_SWORD).setDisplayName(ChatColor.WHITE + "Stone Sword").setLore(ChatColor.GRAY + "10").build();
        ItemStack ironSword = new ItemBuilder(Material.IRON_SWORD).setDisplayName(ChatColor.WHITE + "Stone Sword").setLore(ChatColor.GOLD + "7").build();
        ItemStack diamondSword = new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(ChatColor.WHITE + "Diamond Sword").setLore(ChatColor.GREEN + "6").build();
        ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.WHITE + "Back").build();

        swordsGUI.setItem(10, stoneSword);
        swordsGUI.setItem(11, ironSword);
        swordsGUI.setItem(12, diamondSword);

        swordsGUI.setItem(18, back);

        return swordsGUI;
    }
}
