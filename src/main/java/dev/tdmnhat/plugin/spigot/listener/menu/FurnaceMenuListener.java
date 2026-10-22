package dev.tdmnhat.plugin.spigot.listener.menu;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.inventory.FurnaceMenuInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

public class FurnaceMenuListener implements Listener {

    private FurnaceAdvanced plugin;

    public FurnaceMenuListener() {
        this.plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if(view.getTitle().equalsIgnoreCase(FurnaceMenuInventory.title) && event.getWhoClicked() instanceof Player player) {
            event.setCancelled(true);

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
}
