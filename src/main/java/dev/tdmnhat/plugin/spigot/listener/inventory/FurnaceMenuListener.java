package dev.tdmnhat.plugin.spigot.listener.inventory;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.inventory.FurnaceMenuInventory;
import dev.tdmnhat.plugin.spigot.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FurnaceMenuListener implements Listener {

    private FurnaceAdvanced plugin;

    public FurnaceMenuListener() {
        this.plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check the menu inventory, some methods don't support for some versions
        if (ServerVersion.getMajor() >= 26 || (ServerVersion.getMajor() == 1 && ServerVersion.getMinor() >= 14)) {
            if (event.getView().getTitle().equalsIgnoreCase(FurnaceMenuInventory.title) && event.getWhoClicked() instanceof Player player) {
                if(event.getCurrentItem() == null) return;
                doCheckItemClicked(player, event.getCurrentItem());
            }
        } else {
            if(event.getClickedInventory() == null) return;

            if (event.getClickedInventory().getTitle().equalsIgnoreCase(FurnaceMenuInventory.title) && event.getWhoClicked() instanceof Player player) {
                if(event.getCurrentItem() == null) return;
                doCheckItemClicked(player, event.getCurrentItem());
            }
        }
    }

    private void doCheckItemClicked(Player player, ItemStack itemClicked) {
        if(itemClicked.getType().equals(Material.FURNACE)) {
            player.openInventory(FurnaceMenuInventory.getFurnace().getInventory());
        }
    }
}
