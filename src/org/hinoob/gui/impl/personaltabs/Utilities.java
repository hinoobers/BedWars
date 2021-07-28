package org.hinoob.gui.impl.personaltabs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.util.ItemBuilder;

public class Utilities {

    public static Inventory get(Player player){
        Inventory utilitiesGui = Bukkit.createInventory(null, 27, "Utilities");

        ItemStack tnt = new ItemBuilder(Material.TNT).setDisplayName(ChatColor.WHITE + "Tnt").setLore(ChatColor.GOLD + "4").build();
        ItemStack epearl = new ItemBuilder(Material.ENDER_PEARL).setDisplayName(ChatColor.WHITE + "Ender Pearl").setLore(ChatColor.GREEN + "2").build();
        ItemStack fireBall = new ItemBuilder(Material.FIRE_CHARGE).setDisplayName(ChatColor.WHITE + "Fire Ball").setLore(ChatColor.GRAY + "40").build();
        ItemStack gapples = new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName(ChatColor.WHITE + "Golden Apple").setLore(ChatColor.GOLD + "3").build();


        ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.WHITE + "Back").build();

        utilitiesGui.setItem(10, tnt);
        utilitiesGui.setItem(11, epearl);
        utilitiesGui.setItem(12, fireBall);
        utilitiesGui.setItem(13, gapples);
        utilitiesGui.setItem(18, back);

        return utilitiesGui;
    }
}
