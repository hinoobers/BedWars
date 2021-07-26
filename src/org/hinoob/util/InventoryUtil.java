package org.hinoob.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class InventoryUtil {

    public void removeItems(Player player, ItemStack item, int amount){
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack loopItem = player.getInventory().getItem(i);
            if (loopItem == null || !item.isSimilar(loopItem)) {
                continue;
            }
            if (amount <= 0) {
                return;
            }
            if (amount < loopItem.getAmount()) {
                loopItem.setAmount(loopItem.getAmount() - amount);
                return;
            }
            player.getInventory().clear(i);
            amount -= loopItem.getAmount();
        }
    }

    public int getAmount(Player player, Material material){
        int amount = 0;

        for(ItemStack itemStack : player.getInventory().getContents()){
            if(itemStack != null && itemStack.getType() == material){
                amount += itemStack.getAmount();
            }
        }

        return amount;
    }
}
