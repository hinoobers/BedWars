package org.hinoob.gui.impl.personaltabs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.util.ItemBuilder;

public class Tools {

    public static ItemStack getAxe(Inventory inventory){
        ItemStack currentAxe = null;

        for(ItemStack stack : inventory.getContents()){
            if(stack != null){
                if(stack.getType() == Material.WOODEN_AXE){
                    currentAxe = stack;
                }else if(stack.getType() == Material.STONE_AXE){
                    currentAxe = stack;
                }else if(stack.getType() == Material.IRON_AXE){
                    currentAxe = stack;
                }else if(stack.getType() == Material.DIAMOND_AXE){
                    currentAxe = stack;
                }
            }
        }

        return currentAxe;
    }

    public static ItemStack getPickaxe(Inventory inventory){
        ItemStack currentPickaxe = null;

        for(ItemStack stack : inventory.getContents()){
            if(stack != null){
                if(stack.getType() == Material.WOODEN_PICKAXE){
                    currentPickaxe = stack;
                }else if(stack.getType() == Material.STONE_PICKAXE){
                    currentPickaxe = stack;
                }else if(stack.getType() == Material.IRON_PICKAXE){
                    currentPickaxe = stack;
                }else if(stack.getType() == Material.DIAMOND_PICKAXE){
                    currentPickaxe = stack;
                }
            }
        }

        return currentPickaxe;
    }

    public static Inventory get(Player player){
        Inventory toolsGui = Bukkit.createInventory(null, 27, "Tools");

        Material nextAxe = null, nextPickaxe = null;
        ItemStack currentPickaxe = getPickaxe(player.getInventory());
        ItemStack currentAxe = getAxe(player.getInventory());
        String axePrice = null, pickaxePrice = null;

        if(currentAxe == null){
            axePrice = ChatColor.GRAY + "10";
            nextAxe = Material.WOODEN_AXE;
        }else if(currentAxe.getType() == Material.WOODEN_AXE){
            axePrice = ChatColor.GRAY + "15";
            nextAxe = Material.STONE_AXE;
        }else if(currentAxe.getType() == Material.STONE_AXE){
            axePrice = ChatColor.GRAY + "20";
            nextAxe = Material.IRON_AXE;
        }else if(currentAxe.getType() == Material.IRON_AXE){
            axePrice = ChatColor.GOLD + "5";
            nextAxe = Material.DIAMOND_AXE;
        }else if(currentAxe.getType() == Material.DIAMOND_AXE){
            axePrice = ChatColor.RED + "Max";
            nextAxe = Material.DIAMOND_AXE;
        }

        if(currentPickaxe == null){
            pickaxePrice = ChatColor.GRAY + "10";
            nextPickaxe = Material.WOODEN_PICKAXE;
        }else if(currentPickaxe.getType() == Material.WOODEN_PICKAXE){
            pickaxePrice = ChatColor.GRAY + "15";
            nextPickaxe = Material.STONE_PICKAXE;
        }else if(currentPickaxe.getType() == Material.STONE_PICKAXE){
            pickaxePrice = ChatColor.GRAY + "20";
            nextPickaxe = Material.IRON_PICKAXE;
        }else if(currentPickaxe.getType() == Material.IRON_PICKAXE){
            pickaxePrice = ChatColor.GOLD + "5";
            nextPickaxe = Material.DIAMOND_PICKAXE;
        }else if(currentPickaxe.getType() == Material.DIAMOND_PICKAXE){
            pickaxePrice = ChatColor.RED + "Max";
            nextPickaxe = Material.DIAMOND_PICKAXE;
        }


        ItemStack pickaxe = new ItemBuilder(nextPickaxe).setDisplayName(ChatColor.WHITE + "Pickaxe").setLore(pickaxePrice).build();
        ItemStack axe = new ItemBuilder(nextAxe).setDisplayName(ChatColor.WHITE + "Axe").setLore(axePrice).build();

        ItemStack shears = new ItemBuilder(Material.SHEARS).setDisplayName(ChatColor.WHITE + "Shears").setLore(ChatColor.GRAY + "20").build();

        ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.WHITE + "Back").build();

        toolsGui.setItem(10, pickaxe);
        toolsGui.setItem(11, axe);
        toolsGui.setItem(12, shears);
        toolsGui.setItem(18, back);

        return toolsGui;
    }
}
