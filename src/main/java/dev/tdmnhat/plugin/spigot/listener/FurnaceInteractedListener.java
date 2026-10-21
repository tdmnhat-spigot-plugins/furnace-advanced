package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import dev.tdmnhat.plugin.spigot.inventory.FurnaceMenuInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;

public class FurnaceInteractedListener implements Listener {

    private FurnaceAdvanced plugin;

    public FurnaceInteractedListener() {
        this.plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) return;

        Material material = event.getClickedBlock().getType();
        Player player = event.getPlayer();
        if(material.toString().equalsIgnoreCase("FURNACE") || material.toString().equalsIgnoreCase("BLAST_FURNACE") || material.toString().equalsIgnoreCase("BURNING_FURNACE")) {
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
                FurnaceInfo furnaceInfo = FurnaceManagement.getInstance().getFurnaceInfos().stream().filter(furnace -> furnace.getPlaceLocation().equals(event.getClickedBlock().getLocation())).findFirst().orElse(null);
                if(furnaceInfo != null) {
                    FurnaceMenuInventory inventory = new FurnaceMenuInventory(furnaceInfo.getCookTimeLevel(), furnaceInfo.getGenerateAmountLevel(), (Furnace) event.getClickedBlock().getState());
                    player.openInventory(inventory.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if(event.getPlayer() instanceof Player player) {
            if(player.isSneaking() && event.getInventory() instanceof FurnaceInventory) {
                event.setCancelled(true);
            }
        }
    }
}
