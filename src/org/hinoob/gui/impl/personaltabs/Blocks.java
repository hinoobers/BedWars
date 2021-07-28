package org.hinoob.gui.impl.personaltabs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.island.Island;
import org.hinoob.util.ItemBuilder;

public class Blocks {

    public static Inventory get(Island island){
        Inventory blocksGUI = Bukkit.createInventory(null, 27, "Blocks");

        ItemStack wool = new ItemBuilder(Material.valueOf(island.getColor() + "_WOOL")).setDisplayName(ChatColor.WHITE + "Wool x16").setLore(ChatColor.GRAY + "4").setAmount(16).build();
        ItemStack wood = new ItemBuilder(Material.OAK_PLANKS).setDisplayName(ChatColor.WHITE + "Wood x16").setLore(ChatColor.GOLD + "4").setAmount(16).build();
        ItemStack ladder = new ItemBuilder(Material.LADDER).setDisplayName(ChatColor.WHITE + "Ladder x16").setLore(ChatColor.GOLD + "2").setAmount(16).build();
        ItemStack endstone = new ItemBuilder(Material.END_STONE).setDisplayName(ChatColor.WHITE + "EndStone x24").setLore(ChatColor.GRAY + "12").setAmount(24).build();
        ItemStack obsidian = new ItemBuilder(Material.OBSIDIAN).setDisplayName(ChatColor.WHITE + "Obsidian x4").setLore(ChatColor.GREEN + "4").setAmount(4).build();
        ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.WHITE + "Back").build();
        blocksGUI.setItem(10, wool);
        blocksGUI.setItem(11, wood);
        blocksGUI.setItem(12, endstone);
        blocksGUI.setItem(13, ladder);
        blocksGUI.setItem(14, obsidian);
        blocksGUI.setItem(18, back);

        return blocksGUI;

    }
}
