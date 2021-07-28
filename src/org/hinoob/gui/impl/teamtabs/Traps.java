package org.hinoob.gui.impl.teamtabs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;
import org.hinoob.util.ItemBuilder;

public class Traps {

    public static Inventory get(Island island){
        Inventory trapsGui = Bukkit.createInventory(null, 27, "Traps");

        ItemStack alarmTrap = new ItemBuilder(Material.FIREWORK_ROCKET).setDisplayName(ChatColor.WHITE + "Alarm Trap").setLore(ChatColor.AQUA + "1", ChatColor.GRAY + "Has: " + (island.isAlarmTrap() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")).build();
        ItemStack blidnessTrap = new ItemBuilder(Material.COAL_BLOCK).setDisplayName(ChatColor.WHITE + "Blindness Trap").setLore(ChatColor.AQUA + "2", ChatColor.GRAY + "Has: " + (island.isBlindnessTrap() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")).build();
        ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.WHITE + "Back").build();

        trapsGui.setItem(10, alarmTrap);
        trapsGui.setItem(11, blidnessTrap);
        trapsGui.setItem(18, back);

        return trapsGui;
    }
}
