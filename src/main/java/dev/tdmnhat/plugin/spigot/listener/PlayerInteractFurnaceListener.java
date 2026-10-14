package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.inventory.FurnaceMenuInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInteractFurnaceListener implements Listener {

    private FurnaceAdvanced plugin;

    public PlayerInteractFurnaceListener(FurnaceAdvanced plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material material = event.getBlock().getType();
        if(material.toString().equalsIgnoreCase("FURNACE") || material.toString().equalsIgnoreCase("BLAST_FURNACE")) {

        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) return;

        Material getMaterial = event.getClickedBlock().getType();
        Player author = event.getPlayer();
        if(getMaterial.toString().equalsIgnoreCase("FURNACE") || getMaterial.toString().equalsIgnoreCase("BLAST_FURNACE")) {
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK) && author.isSneaking()) {
                Inventory furnaceMenuInv = new FurnaceMenuInventory(plugin).getInventory(author, event.getClickedBlock().getLocation());
                author.openInventory(furnaceMenuInv);
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player author = event.getPlayer();
        Material material = event.getBlock().getType();

        if((material.toString().equalsIgnoreCase("FURNACE") || material.toString().equalsIgnoreCase("BLAST_FURNACE")) && author.isSneaking()) {
            event.setCancelled(true);
        }
    }
}
