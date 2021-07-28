package org.hinoob.gui.impl.teamtabs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.island.Island;
import org.hinoob.util.ItemBuilder;

public class Enchantments {

    public static Inventory get(Island island){
        Inventory trapsGui = Bukkit.createInventory(null, 27, "Enchantments");

        ItemStack sharpness = new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(ChatColor.WHITE + "Permanent Sharpness 1").setLore(ChatColor.AQUA + "1", ChatColor.GRAY + "Has: " + (island.isHasSharp() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")).build();
        ItemStack protection = new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(ChatColor.WHITE + "Permanent Protection 2").setLore(ChatColor.AQUA + "2", ChatColor.GRAY + "Has: " + (island.isHasProt() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")).build();

        sharpness.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        protection.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.WHITE + "Back").build();

        trapsGui.setItem(10, sharpness);
        trapsGui.setItem(11, protection);
        trapsGui.setItem(18, back);

        return trapsGui;
    }
}
