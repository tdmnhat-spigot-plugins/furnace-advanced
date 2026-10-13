package dev.tdmnhat.plugin.spigot.inventory;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class ListFurnaceMenuInventory {

    private FurnaceAdvanced plugin;
    private static Inventory inventory;

    public ListFurnaceMenuInventory(FurnaceAdvanced plugin) {
        this.plugin = plugin;
        inventory = Bukkit.createInventory(null, 9, plugin.getConfig().getString("furnace-menu.title"));
        addItem();
    }

    public void addItem() {

    }

    public static Inventory getInventory() {
        return inventory;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
