package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import dev.tdmnhat.plugin.spigot.inventory.FurnaceMenuInventory;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryType inventoryType = event.getInventory().getType();
        if(inventoryType.equals(InventoryType.FURNACE) && event.getPlayer() instanceof Player player) {
            FurnaceInventory furnaceInventory = (FurnaceInventory) event.getInventory();
            // Get furnace location
            BlockState getFurnaceState = (BlockState) event.getInventory().getHolder();
            if (getFurnaceState == null) return;

            FurnaceInfo furnaceInfo = FurnaceManagement.getInstance().getListFurnaces(player).stream().filter(item -> item.getPlaceLocation().equals(getFurnaceState.getLocation())).findFirst().orElse(null);

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

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof FurnaceInventory && event.getPlayer() instanceof Player player) {
            if (player.isSneaking()) {
                event.setCancelled(true);

                BlockState blockState = (BlockState) event.getInventory().getHolder();
                if (blockState == null) return;

                FurnaceInfo furnaceInfo = FurnaceManagement.getInstance().getListFurnaces(player).stream().filter(furnace -> furnace.getPlaceLocation().equals(blockState.getLocation())).findFirst().orElse(null);
                if (furnaceInfo != null) {
                    FurnaceMenuInventory inventory = new FurnaceMenuInventory(furnaceInfo.getCookTimeLevel(), furnaceInfo.getGenerateAmountLevel(), (Furnace) blockState);
                    player.openInventory(inventory.getInventory());
                }
            }
        }
    }
}
