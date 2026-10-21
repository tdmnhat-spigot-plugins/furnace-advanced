package dev.tdmnhat.plugin.spigot.listener.inventory;

import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryType inventoryType = event.getInventory().getType();
        if(inventoryType.equals(InventoryType.FURNACE)) {
            FurnaceInventory furnaceInventory = (FurnaceInventory) event.getInventory();

            FurnaceInfo furnaceInfo = FurnaceManagement.getInstance().getFurnaceInfos().stream().filter(item -> item.getPlaceLocation().equals(event.getInventory().getLocation())).findFirst().orElse(null);
            if(furnaceInfo != null) {
                ArmorStand armorStand = furnaceInfo.getHolograms().get(3);
                ItemStack getSmeltingItem = furnaceInventory.getSmelting();
                ItemStack getResultItem = furnaceInventory.getResult();

                int smeltingItemAmount = getSmeltingItem == null ? 0 : getSmeltingItem.getAmount();
                int resultItemAmount = getResultItem == null ? 0 : getResultItem.getAmount();
                armorStand.setCustomName(smeltingItemAmount + " ➜ " + resultItemAmount);
            }
        }
    }
}
