package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.inventory.Menu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryMenuItemListener implements Listener {

    private final FurnaceAdvanced plugin;

    public InventoryMenuItemListener() {
        this.plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;

        if(event.getClickedInventory().getHolder() instanceof Menu menu) {
            menu.handleMenu(event);
        }
    }
}
