package dev.tdmnhat.plugin.spigot.listener.inventory;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FurnaceMenuListener implements Listener {

    private FurnaceAdvanced plugin;

    public FurnaceMenuListener(FurnaceAdvanced plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {

    }
}
