package dev.tdmnhat.plugin.spigot.listener.menu;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.inventory.FurnaceMenuInventory;
import dev.tdmnhat.plugin.spigot.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class FurnaceMenuListener implements Listener {

    private FurnaceAdvanced plugin;

    public FurnaceMenuListener() {
        this.plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(ServerVersion.getMajor() >= 26 || (ServerVersion.getMajor() == 1 && ServerVersion.getMinor() >= 21)) {
            InventoryView view = event.getView();
            if(view.getTitle().equalsIgnoreCase(FurnaceMenuInventory.title) && event.getWhoClicked() instanceof Player) {
                handleClickItem(event);
            }
        } else {
            Inventory inventory = event.getClickedInventory();
            if(inventory.getTitle().equalsIgnoreCase(FurnaceMenuInventory.title) && event.getWhoClicked() instanceof Player) {
                handleClickItem(event);
            }
        }
    }

    private void handleClickItem(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int getSlotClicked = event.getRawSlot();
        if(getSlotClicked < 0 && getSlotClicked >= event.getClickedInventory().getSize()) return;

        switch(getSlotClicked) {
            case 11: {

                break;
            }
            case 13: {
                player.openInventory(FurnaceMenuInventory.getFurnace().getInventory());
                break;
            }
            case 15: {

                break;
            }
            case 22: {
                player.closeInventory();
                break;
            }
            default: {}
        }
    }
}
