package dev.tdmnhat.plugin.spigot.inventory;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FurnaceMenuInventory {

    private FurnaceAdvanced plugin;
    private Inventory inventory;

    public FurnaceMenuInventory(FurnaceAdvanced plugin) {
        this.plugin = plugin;
        inventory = Bukkit.createInventory(null, 9, "Furnace Upgrade Menu");
    }

    public Inventory getInventory(Player author, Location location) {
        addItem(author, location);
        return inventory;
    }

    private void addItem(Player author, Location location) {

    }

    public FurnaceAdvanced getPlugin() {
        return plugin;
    }
}
