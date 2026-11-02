package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.async.FurnaceCookingProgress;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.inventory.FurnaceUpgradeMenuInventory;
import dev.tdmnhat.plugin.spigot.inventory.Menu;
import dev.tdmnhat.plugin.spigot.inventory.PlayerMenuUtility;
import org.bukkit.Bukkit;
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

    private FurnaceManagement furnaceManagement;
    private FurnaceAdvanced plugin;
    public FurnaceInventoryListener(FurnaceAdvanced plugin, FurnaceManagement furnaceManagement) {
        this.plugin = plugin;
        this.furnaceManagement = furnaceManagement;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryType inventoryType = event.getInventory().getType();
        if(inventoryType.equals(InventoryType.FURNACE) && event.getPlayer() instanceof Player player) {
            // Update status into hologram

            FurnaceInventory furnaceInventory = (FurnaceInventory) event.getInventory();
            FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(furnaceInventory.getHolder().getLocation());

            // Update hologram
            if(furnaceInformation != null) {
                ArmorStand armorStand = furnaceInformation.getHolograms().get(3);
                ItemStack getSmeltingItem = furnaceInventory.getSmelting();
                ItemStack getResultItem = furnaceInventory.getResult();

                int smeltingItemAmount = getSmeltingItem == null ? 0 : getSmeltingItem.getAmount();
                int resultItemAmount = getResultItem == null ? 0 : getResultItem.getAmount();
                armorStand.setCustomName(smeltingItemAmount + " ➜ " + resultItemAmount);

                new FurnaceCookingProgress(plugin).addTask(furnaceInventory.getHolder(), furnaceInformation);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof FurnaceInventory && event.getPlayer() instanceof Player player) {
            if (player.isSneaking()) {
                event.setCancelled(true); // Avoid opening the furnace inventory, must open the machine menu

                BlockState blockState = (BlockState) event.getInventory().getHolder();
                if (blockState == null) return;

                FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(blockState.getLocation());
                if (furnaceInformation != null) {
                    new FurnaceUpgradeMenuInventory(plugin, new PlayerMenuUtility(player), (Furnace) blockState, furnaceManagement).open();
                }
            }
        }
    }
}
