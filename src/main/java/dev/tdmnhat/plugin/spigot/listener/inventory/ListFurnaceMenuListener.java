package dev.tdmnhat.plugin.spigot.listener.inventory;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.inventory.ListFurnaceMenuInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ListFurnaceMenuListener implements Listener {

    private FurnaceAdvanced plugin;

    public ListFurnaceMenuListener(FurnaceAdvanced plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if(event.getInventory().equals(ListFurnaceMenuInventory.getInventory())) {

        }
    }
}
