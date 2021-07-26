package org.hinoob.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.game.Game;
import org.hinoob.manager.GameManager;
import org.hinoob.util.InventoryUtil;
import org.hinoob.util.ItemBuilder;

public class ShopListener implements Listener {

    public void openBlocksGUI(Player player, Material woolMaterial){
        Inventory blocksGUI = Bukkit.createInventory(null, 27, "Blocks");

        ItemStack wool = new ItemBuilder(woolMaterial).setDisplayName(ChatColor.WHITE + "Wool x16").setLore(ChatColor.GRAY + "4").setAmount(16).build();
        ItemStack wood = new ItemBuilder(Material.OAK_PLANKS).setDisplayName(ChatColor.WHITE + "Wood x16").setLore(ChatColor.GOLD + "4").setAmount(16).build();
        ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.WHITE + "Back").build();
        blocksGUI.setItem(10, wool);
        blocksGUI.setItem(11, wood);
        blocksGUI.setItem(18, back);

        player.openInventory(blocksGUI);
    }

    public void openSwordsGUI(Player player){
        Inventory swordsGUI = Bukkit.createInventory(null, 27, "Swords");

        ItemStack stoneSword = new ItemBuilder(Material.STONE_SWORD).setDisplayName(ChatColor.WHITE + "Stone Sword").setLore(ChatColor.GRAY + "10").build();
        ItemStack ironSword = new ItemBuilder(Material.IRON_SWORD).setDisplayName(ChatColor.WHITE + "Stone Sword").setLore(ChatColor.GOLD + "7").build();
        ItemStack back = new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.WHITE + "Back").build();

        swordsGUI.setItem(10, stoneSword);
        swordsGUI.setItem(11, ironSword);
        swordsGUI.setItem(18, back);

        player.openInventory(swordsGUI);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getView() != null && e.getView().getTitle() != null && GameManager.findGameByPlayer((Player)e.getWhoClicked()) != null){
            Player player = (Player) e.getWhoClicked();
            Game game = GameManager.findGameByPlayer(player);

            if(e.getView().getTitle().equalsIgnoreCase("Team Shop")){
                if(e.getCurrentItem() != null){

                }

                e.setCancelled(true);
            }else if(e.getView().getTitle().equalsIgnoreCase("Personal Shop")) {
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Blocks")) {
                        openBlocksGUI(player, Material.valueOf(game.getIslandByPlayer(player).getColor() + "_WOOL"));
                    }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Swords")){
                        openSwordsGUI(player);
                    }
                }

                e.setCancelled(true);
            }else{

                if(e.getView().getTitle().equalsIgnoreCase("Blocks") || e.getView().getTitle().equalsIgnoreCase("Swords")){
                    if(e.getCurrentItem() != null){
                        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Back")){
                            e.getWhoClicked().closeInventory();
                            game.getPersonalShopGUI().open(player);
                        }
                    }

                    e.setCancelled(true);
                }
                if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()){
                    String amount = e.getCurrentItem().getItemMeta().getLore().get(0);
                    Material required = null;
                    if(amount.startsWith(ChatColor.GOLD + "")){
                        required = Material.GOLD_INGOT;
                    }else if(amount.startsWith(ChatColor.GRAY + "")){
                        required = Material.IRON_INGOT;
                    }else if(amount.startsWith(ChatColor.AQUA + "")){
                        required = Material.DIAMOND;
                    }else if(amount.startsWith(ChatColor.GREEN + "")){
                        required = Material.EMERALD;
                    }

                    int amountNumber = Integer.parseInt(ChatColor.stripColor(amount));

                    if(InventoryUtil.getAmount(player, required) >= amountNumber){
                        InventoryUtil.removeItems(player, new ItemStack(required), amountNumber);

                        e.getWhoClicked().sendMessage(ChatColor.GREEN + "Bought!");

                        ItemStack copied = e.getCurrentItem().clone();
                        copied.setItemMeta(null);
                        e.getWhoClicked().getInventory().addItem(copied);
                        e.getWhoClicked().closeInventory();
                    }else{
                        e.getWhoClicked().sendMessage(ChatColor.RED + "You don't have enough " + required.name().toLowerCase() + "!");
                        e.getWhoClicked().closeInventory();
                    }
                }
            }
        }
    }
}
