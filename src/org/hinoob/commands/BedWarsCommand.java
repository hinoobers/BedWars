package org.hinoob.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hinoob.game.Game;
import org.hinoob.manager.GameManager;
import org.hinoob.map.GameMap;
import org.hinoob.setup.SetupData;
import org.hinoob.setup.SetupDataManager;
import org.hinoob.util.ItemBuilder;

public class BedWarsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(commandSender.hasPermission("bedwars.admin")){
                if(strings.length == 0){
                    commandSender.sendMessage(ChatColor.RED + "/bedwars help");
                }else{
                    if(strings.length == 1){
                        if(strings[0].equalsIgnoreCase("help")){
                            commandSender.sendMessage(ChatColor.RED + "/bedwars setup <name>");
                        }else if(strings[0].equalsIgnoreCase("setup")) {
                            commandSender.sendMessage(ChatColor.RED + "You must provide a name!");
                        }else if(strings[0].equalsIgnoreCase("endgame")) {
                            Game game = GameManager.findGameByPlayer((Player) commandSender);

                            if (game == null) {
                                commandSender.sendMessage(ChatColor.RED + "You must be in a game first!");
                            } else {
                                commandSender.sendMessage(ChatColor.GREEN + "Ended!");
                                game.end(true);
                            }
                        }else if(strings[0].equalsIgnoreCase("worldname")){
                            commandSender.sendMessage(((Player) commandSender).getLocation().getWorld().getName());
                        }else{
                            commandSender.sendMessage(ChatColor.RED + "/bedwars help");
                        }
                    }else if(strings.length == 2){
                        if(strings[0].equalsIgnoreCase("setup")){
                            if(GameManager.findGameByName(strings[1], true) != null){
                                commandSender.sendMessage(ChatColor.RED + "Game with that name already exists!");
                                return true;
                            }

                            SetupData setupData = SetupDataManager.getData((Player)commandSender);

                            Player player = (Player) commandSender;
                            if(!setupData.active){
                                setupData.active = true;
                                setupData.backupInventory = player.getInventory();

                                player.getInventory().clear(); // Can clear after backing it up

                                setupData.gameName = strings[1];

                                ItemStack setSpawnLocation = new ItemBuilder(Material.GREEN_WOOL).setDisplayName("&7Set spawn location").build();
                                ItemStack colorChanger = new ItemBuilder(Material.WHITE_WOOL).setDisplayName("&fWhite").setLore("color").build();
                                ItemStack islandSpawnLocation = new ItemBuilder(Material.GRAY_WOOL).setDisplayName("&7Set island spawn location").build();
                                ItemStack islandResourceLocation = new ItemBuilder(Material.GRAY_WOOL).setDisplayName("&7Set island resource spawn location").build();
                                ItemStack islandPersonalLocation = new ItemBuilder(Material.GRAY_WOOL).setDisplayName("&7Set island personal shop location").build();
                                ItemStack islandTeamShopLocation = new ItemBuilder(Material.GRAY_WOOL).setDisplayName("&7Set island team shop location").build();
                                ItemStack islandBedLocation = new ItemBuilder(Material.GRAY_WOOL).setDisplayName("&7Set island bed location").build();
                                ItemStack addIsland = new ItemBuilder(Material.GREEN_WOOL).setDisplayName("&aAdd Island").build();
                                ItemStack finish = new ItemBuilder(Material.GREEN_WOOL).setDisplayName("&aFinish").build();

                                ItemStack addDiamondSpawnLocation = new ItemBuilder(Material.DIAMOND).setDisplayName("&bAdd diamond generator").build();
                                ItemStack addEmeraldSpawnLocation = new ItemBuilder(Material.EMERALD).setDisplayName("&aAdd emerald generator").build();

                                player.getInventory().setItem(0, setSpawnLocation);
                                player.getInventory().setItem(1, colorChanger);
                                player.getInventory().setItem(2, islandSpawnLocation);
                                player.getInventory().setItem(3, islandResourceLocation);
                                player.getInventory().setItem(4, islandPersonalLocation);
                                player.getInventory().setItem(5, islandTeamShopLocation);
                                player.getInventory().setItem(6, islandBedLocation);
                                player.getInventory().setItem(7, addIsland);
                                player.getInventory().setItem(8, finish);

                                player.getInventory().setItem(11, addDiamondSpawnLocation);
                                player.getInventory().setItem(12, addEmeraldSpawnLocation);

                                commandSender.sendMessage(ChatColor.GREEN + "Entered setup mode (Make sure to check ur whole inventory)");

                            }else{
                                setupData.active = false;
                                player.getInventory().setContents(setupData.backupInventory.getContents());
                                commandSender.sendMessage(ChatColor.GREEN + "Quit setup mode!");
                            }
                        }else{
                            commandSender.sendMessage(ChatColor.RED + "/bedwars help");
                        }
                    }else{
                        commandSender.sendMessage(ChatColor.RED + "/bedwars help");
                    }
                }
            }else{
                commandSender.sendMessage(ChatColor.RED + "No permissions!");
            }
        }else{
            System.out.println("[BedWars] Only for players!");
        }

        return true;
    }
}
