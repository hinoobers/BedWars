package org.hinoob.gui.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.hinoob.game.Game;
import org.hinoob.gui.AbstractGUI;

public class TeamShopGUI implements AbstractGUI {
    private Game game;
    private Inventory inventory;

    public TeamShopGUI(Game game){
        this.game = game;

        this.inventory = Bukkit.createInventory(null, 27, "Team Shop");
    }

    public void open(Player player){
        player.openInventory(inventory);
    }
}
