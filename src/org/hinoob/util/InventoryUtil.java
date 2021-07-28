package org.hinoob.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.hinoob.island.Island;

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

    public void giveFullArmor(Island island, Player player, Color color){
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateMeta.setColor(color);
        chestplate.setItemMeta(chestplateMeta);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(color);
        helmet.setItemMeta(helmetMeta);

        if(island.isHasProt()){
            boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        }

        player.getInventory().setArmorContents(new ItemStack[] {boots, leggings, chestplate, helmet});
    }
}
