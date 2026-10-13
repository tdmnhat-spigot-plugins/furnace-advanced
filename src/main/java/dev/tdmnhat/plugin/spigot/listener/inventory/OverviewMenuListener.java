package dev.tdmnhat.plugin.spigot.listener.inventory;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.inventory.ListFurnaceMenuInventory;
import dev.tdmnhat.plugin.spigot.inventory.OverviewMenuInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class OverviewMenuListener implements Listener {

    private FurnaceAdvanced plugin;

    public OverviewMenuListener(FurnaceAdvanced plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory().equals(OverviewMenuInventory.getInventory())) {
            Player player = (Player) event.getWhoClicked();
            if(event.getClick().isRightClick()) return;

            event.setCancelled(true);

            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("overview-menu.menu-furnaces.display-name"))) {
                Inventory furnaceMenuInventory = new ListFurnaceMenuInventory(plugin).getInventory();
                player.closeInventory();
                player.openInventory(furnaceMenuInventory);
            }
        }
    }
}
