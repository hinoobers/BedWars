package org.hinoob.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hinoob.game.Game;
import org.hinoob.gui.impl.personaltabs.Blocks;
import org.hinoob.gui.impl.personaltabs.Swords;
import org.hinoob.gui.impl.personaltabs.Tools;
import org.hinoob.gui.impl.personaltabs.Utilities;
import org.hinoob.gui.impl.teamtabs.Enchantments;
import org.hinoob.gui.impl.teamtabs.Traps;
import org.hinoob.island.Island;
import org.hinoob.manager.GameManager;
import org.hinoob.util.InventoryUtil;
import org.hinoob.util.ItemBuilder;

public class ShopListener implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView() != null && e.getView().getTitle() != null && GameManager.findGameByPlayer((Player) e.getWhoClicked()) != null) {
            Player player = (Player) e.getWhoClicked();
            Game game = GameManager.findGameByPlayer(player);

            if (e.getView().getTitle().equalsIgnoreCase("Team Shop")) {
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Traps")) {
                        e.getWhoClicked().openInventory(Traps.get(game.getIslandByPlayer(player)));
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Enchantments")) {

                        e.getWhoClicked().openInventory(Enchantments.get(game.getIslandByPlayer(player)));
                    }
                }

                e.setCancelled(true);
            } else if (e.getView().getTitle().equalsIgnoreCase("Personal Shop")) {
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Blocks")) {
                        e.getWhoClicked().openInventory(Blocks.get(game.getIslandByPlayer(player)));
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Swords")) {
                        e.getWhoClicked().openInventory(Swords.get());
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Tools")) {
                        e.getWhoClicked().openInventory(Tools.get((Player) e.getWhoClicked()));
                    }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Utilities")){
                        e.getWhoClicked().openInventory(Utilities.get((Player)e.getWhoClicked()));
                    }
                }

                e.setCancelled(true);
            } else {

                String displayNameA = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                if(e.getView().getTitle().equalsIgnoreCase("Tools") || e.getView().getTitle().equalsIgnoreCase("Utilities") || e.getView().getTitle().equalsIgnoreCase("Swords") || e.getView().getTitle().equalsIgnoreCase("Blocks")){
                    if(displayNameA.equalsIgnoreCase("Back")){
                        e.getWhoClicked().closeInventory();
                        game.getPersonalShopGUI().open(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase("Enchantments") || e.getView().getTitle().equalsIgnoreCase("Traps")){
                    if(displayNameA.equalsIgnoreCase("Back")){
                        e.getWhoClicked().closeInventory();
                        game.getTeamShopGUI().open(player);
                    }
                }


                if (e.getView().getTitle().equalsIgnoreCase("Tools")) {
                    if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {

                        String amount = e.getCurrentItem().getItemMeta().getLore().get(0);
                        Material required = null;
                        if (amount.startsWith(ChatColor.GOLD + "")) {
                            required = Material.GOLD_INGOT;
                        } else if (amount.startsWith(ChatColor.GRAY + "")) {
                            required = Material.IRON_INGOT;
                        } else if (amount.startsWith(ChatColor.AQUA + "")) {
                            required = Material.DIAMOND;
                        } else if (amount.startsWith(ChatColor.GREEN + "")) {
                            required = Material.EMERALD;
                        }

                        if (ChatColor.stripColor(amount).contains("Max")) {
                            e.getWhoClicked().sendMessage(ChatColor.RED + "You already own the maxed item!");
                            e.getWhoClicked().closeInventory();
                            return;
                        }

                        int amountNumber = Integer.parseInt(ChatColor.stripColor(amount));

                        if (InventoryUtil.getAmount(player, required) >= amountNumber) {


                            String firstLore = e.getCurrentItem().getItemMeta().getLore().get(0);


                            ItemStack copied = e.getCurrentItem().clone();
                            copied.setItemMeta(null);
                            e.getWhoClicked().getInventory().addItem(copied);

                            e.getWhoClicked().sendMessage(ChatColor.GREEN + "Bought!");
                            InventoryUtil.removeItems(player, new ItemStack(required), amountNumber);

                            e.getWhoClicked().closeInventory();
                        }
                    }
                    e.setCancelled(true);
                }else if(e.getView().getTitle().equalsIgnoreCase("Enchantments")){
                    if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                        String amount = e.getCurrentItem().getItemMeta().getLore().get(0);
                        Material required = null;
                        if (amount.startsWith(ChatColor.GOLD + "")) {
                            required = Material.GOLD_INGOT;
                        } else if (amount.startsWith(ChatColor.GRAY + "")) {
                            required = Material.IRON_INGOT;
                        } else if (amount.startsWith(ChatColor.AQUA + "")) {
                            required = Material.DIAMOND;
                        } else if (amount.startsWith(ChatColor.GREEN + "")) {
                            required = Material.EMERALD;
                        }

                        int amountNumber = Integer.parseInt(ChatColor.stripColor(amount));

                        if (InventoryUtil.getAmount(player, required) >= amountNumber) {


                            String displayName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                            if (displayName.equalsIgnoreCase("Permanent Sharpness 1")) {
                                if (game.getIslandByPlayer(player).isHasSharp()) {
                                    e.getWhoClicked().sendMessage(ChatColor.RED + "You already own that!");
                                    e.getWhoClicked().closeInventory();
                                    return;
                                } else {
                                    game.getIslandByPlayer(player).setHasSharp(true);

                                    for(Player a : game.getIslandByPlayer(player).getPlayers()){
                                        if(a.getGameMode() == GameMode.SURVIVAL){
                                            for(ItemStack stack : a.getInventory().getContents()){
                                                if(stack != null && stack.getType().toString().toLowerCase().contains("sword") && !stack.containsEnchantment(Enchantment.DAMAGE_ALL)){
                                                    stack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                                                }
                                            }
                                        }
                                    }

                                }

                                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Bought!");
                                InventoryUtil.removeItems(player, new ItemStack(required), amountNumber);

                                e.getWhoClicked().closeInventory();
                            } else if (displayName.equalsIgnoreCase("Permanent Protection 2")) {
                                if (game.getIslandByPlayer(player).isHasProt()) {
                                    e.getWhoClicked().sendMessage(ChatColor.RED + "You already own that!");
                                    e.getWhoClicked().closeInventory();
                                    return;
                                } else {
                                    game.getIslandByPlayer(player).setHasProt(true);

                                    for(Player a : game.getIslandByPlayer(player).getPlayers()){
                                        if(a.getGameMode() == GameMode.SURVIVAL){
                                            for(ItemStack armorPiece : a.getInventory().getArmorContents()){
                                                if(armorPiece != null && !armorPiece.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)){
                                                    armorPiece.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                                                }
                                            }
                                        }
                                    }
                                }
                                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Bought!");
                                InventoryUtil.removeItems(player, new ItemStack(required), amountNumber);

                                e.getWhoClicked().closeInventory();
                            } else {
                                e.getWhoClicked().sendMessage(ChatColor.RED + "You don't have enough " + required.name().toLowerCase() + "(s) !");
                                e.getWhoClicked().closeInventory();
                            }
                        }
                        e.setCancelled(true);
                    }
                } else if (e.getView().getTitle().equalsIgnoreCase("Traps")) {
                    if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                        String amount = e.getCurrentItem().getItemMeta().getLore().get(0);
                        Material required = null;
                        if (amount.startsWith(ChatColor.GOLD + "")) {
                            required = Material.GOLD_INGOT;
                        } else if (amount.startsWith(ChatColor.GRAY + "")) {
                            required = Material.IRON_INGOT;
                        } else if (amount.startsWith(ChatColor.AQUA + "")) {
                            required = Material.DIAMOND;
                        } else if (amount.startsWith(ChatColor.GREEN + "")) {
                            required = Material.EMERALD;
                        }

                        int amountNumber = Integer.parseInt(ChatColor.stripColor(amount));

                        if (InventoryUtil.getAmount(player, required) >= amountNumber) {


                            String displayName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                            if (displayName.equalsIgnoreCase("Alarm Trap")) {
                                if (game.getIslandByPlayer(player).isAlarmTrap()) {
                                    e.getWhoClicked().sendMessage(ChatColor.RED + "You already own that!");
                                    e.getWhoClicked().closeInventory();
                                    return;
                                } else {
                                    game.getIslandByPlayer(player).setAlarmTrap(true);
                                }

                                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Bought!");
                                InventoryUtil.removeItems(player, new ItemStack(required), amountNumber);

                                e.getWhoClicked().closeInventory();
                            } else if (displayName.equalsIgnoreCase("Blindness Trap")) {
                                if (game.getIslandByPlayer(player).isBlindnessTrap()) {
                                    e.getWhoClicked().sendMessage(ChatColor.RED + "You already own that!");
                                    e.getWhoClicked().closeInventory();
                                    return;
                                } else {
                                    game.getIslandByPlayer(player).setBlindnessTrap(true);
                                }
                                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Bought!");
                                InventoryUtil.removeItems(player, new ItemStack(required), amountNumber);

                                e.getWhoClicked().closeInventory();
                            } else {
                                e.getWhoClicked().sendMessage(ChatColor.RED + "You don't have enough " + required.name().toLowerCase() + "(s) !");
                                e.getWhoClicked().closeInventory();
                            }
                        }
                        e.setCancelled(true);
                    }
                } else if (e.getView().getTitle().equalsIgnoreCase("Blocks") || e.getView().getTitle().equalsIgnoreCase("Swords") || e.getView().getTitle().equalsIgnoreCase("Utilities")) {
                    if (e.getCurrentItem() != null) {
                        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Back")) {
                            e.getWhoClicked().closeInventory();
                            game.getPersonalShopGUI().open(player);
                        } else {

                            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore()) {
                                String amount = e.getCurrentItem().getItemMeta().getLore().get(0);
                                Material required = null;
                                if (amount.startsWith(ChatColor.GOLD + "")) {
                                    required = Material.GOLD_INGOT;
                                } else if (amount.startsWith(ChatColor.GRAY + "")) {
                                    required = Material.IRON_INGOT;
                                } else if (amount.startsWith(ChatColor.AQUA + "")) {
                                    required = Material.DIAMOND;
                                } else if (amount.startsWith(ChatColor.GREEN + "")) {
                                    required = Material.EMERALD;
                                }

                                int amountNumber = Integer.parseInt(ChatColor.stripColor(amount));

                                if (InventoryUtil.getAmount(player, required) >= amountNumber) {
                                    InventoryUtil.removeItems(player, new ItemStack(required), amountNumber);

                                    e.getWhoClicked().sendMessage(ChatColor.GREEN + "Bought!");

                                    ItemStack copied = e.getCurrentItem().clone();
                                    copied.setItemMeta(null);
                                    e.getWhoClicked().getInventory().addItem(copied);
                                } else {
                                    e.getWhoClicked().sendMessage(ChatColor.RED + "You don't have enough " + required.name().toLowerCase() + "(s) !");
                                    e.getWhoClicked().closeInventory();
                                }
                            }
                        }
                    }

                    e.setCancelled(true);
                }
            }
        }
    }
}
