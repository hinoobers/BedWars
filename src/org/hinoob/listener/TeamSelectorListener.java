package org.hinoob.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.hinoob.game.Game;
import org.hinoob.manager.GameManager;

public class TeamSelectorListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getItem() != null && e.getAction() == Action.RIGHT_CLICK_AIR && GameManager.findGameByPlayer(e.getPlayer()) != null){
            if(!e.getItem().hasItemMeta()) return;

            String displayName = ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName());

            if(displayName.equalsIgnoreCase("Team Selector")){
                GameManager.findGameByPlayer(e.getPlayer()).getTeamSelectorGUI().open(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getView() != null && e.getView().getTitle() != null && e.getView().getTitle().equalsIgnoreCase("Team Selector") && GameManager.findGameByPlayer((Player)e.getWhoClicked()) != null){
            if(e.getCurrentItem() != null){
                Game game = GameManager.findGameByPlayer((Player)e.getWhoClicked());

                String teamName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                teamName = teamName.split(" ")[0];

                int alreadyInTeam = 0;

                for(Player player : game.getGlobalPlayers()){
                    if(game.desiredTeam.containsKey(player) && game.desiredTeam.get(player).equalsIgnoreCase(teamName)){
                        ++alreadyInTeam;
                    }
                }

                if(alreadyInTeam != 0 && alreadyInTeam + 1 >= game.maxPlayersPerTeam){
                    e.getWhoClicked().sendMessage(ChatColor.RED + "Team is full!");
                    e.getWhoClicked().closeInventory();
                }else{
                    Player player = (Player) e.getWhoClicked();
                    player.setPlayerListName(ChatColor.valueOf(teamName) + player.getName());
                    game.desiredTeam.put((Player)e.getWhoClicked(), teamName);
                    e.getWhoClicked().closeInventory();

                    for(ItemStack itemStack : e.getWhoClicked().getInventory().getContents()){
                        if(itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Team Selector")){
                            itemStack.setType(Material.valueOf(teamName + "_WOOL"));
                        }
                    }
                }

                game.getTeamSelectorGUI().update();
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if(GameManager.findGameByPlayer(e.getPlayer()) != null){
            String displayName = ChatColor.stripColor(e.getItemDrop().getItemStack().getItemMeta().getDisplayName());

            if(displayName.equalsIgnoreCase("Team Selector")){
                e.getPlayer().sendMessage(ChatColor.RED + "You can't drop the team selector!");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(!e.isCancelled() && e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasDisplayName() && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Team Selector")){
            e.getPlayer().sendMessage(ChatColor.RED + "You can't place the team selector!");
            e.setCancelled(true);
        }
    }
}
