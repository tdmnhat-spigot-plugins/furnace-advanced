package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import dev.tdmnhat.plugin.spigot.entity.management.FurnaceInfoManagement;
import dev.tdmnhat.plugin.spigot.file.PlayerDataFile;
import dev.tdmnhat.plugin.spigot.inventory.FurnaceMenuInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class PlayerInteractFurnaceListener implements Listener {

    private FurnaceAdvanced plugin;
    public PlayerInteractFurnaceListener(FurnaceAdvanced plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material material = event.getBlock().getType();
        if(material.toString().equalsIgnoreCase("FURNACE") || material.toString().equalsIgnoreCase("BLAST_FURNACE")) {
            Player player = event.getPlayer();
            ItemStack itemStack = event.getItemInHand();
            Block block = event.getBlock();

            // Get & Generate information
            FurnaceInfo furnaceInfo = new FurnaceInfo();
            furnaceInfo.setId(player.getUniqueId() + "_" + DateTimeFormatter.ofPattern("ddMMyyyyHHmmss").format(LocalDateTime.now()));
            if (Objects.nonNull(itemStack.getItemMeta())) {
                if (itemStack.getItemMeta().hasLore()) {
                    furnaceInfo.setCookLevel(Integer.valueOf(itemStack.getItemMeta().getLore().stream().filter(lore -> lore.contains("Cook Level")).findFirst().orElse("Cook Level: 0").split(":")[1].trim()));
                    furnaceInfo.setGenerateAmountLevel(Integer.valueOf(itemStack.getItemMeta().getLore().stream().filter(lore -> lore.contains("Generate Amount")).findFirst().orElse("Generate Level: 0").split(":")[1].trim()));
                }
            } else {
                furnaceInfo.setCookLevel(1);
                furnaceInfo.setGenerateAmountLevel(1);
            }
            furnaceInfo.setLocation(block.getLocation());
            FurnaceInfoManagement.getInstance().getFurnacePlacedList().add(furnaceInfo);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) return;

        Material getMaterial = event.getClickedBlock().getType();
        Player author = event.getPlayer();
        if(getMaterial.toString().equalsIgnoreCase("FURNACE") || getMaterial.toString().equalsIgnoreCase("BLAST_FURNACE")) {
            FurnaceInfo furnaceInfo = FurnaceInfoManagement.getInstance().getFurnacePlacedList().stream().filter(item -> item.getLocation().equals(event.getClickedBlock().getLocation())).findFirst().orElse(null);
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK) && author.isSneaking()) {
                if(furnaceInfo == null) {
                    Bukkit.getConsoleSender().sendMessage("[Furnace Advanced] The furnace clicked from player " + author.getName() + " is not existed!");
                } else {
                    Inventory furnaceMenuInv = new FurnaceMenuInventory(plugin).getInventory(author, event.getClickedBlock().getLocation());
                    author.openInventory(furnaceMenuInv);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player author = event.getPlayer();
        Material material = event.getBlock().getType();

        boolean checkBlock = material.toString().equalsIgnoreCase("FURNACE") || material.toString().equalsIgnoreCase("BLAST_FURNACE");
        if((checkBlock) && author.isSneaking()) {
            // Stop breaking block due to the fast from mining
            event.setCancelled(true);
        } else if (checkBlock) {
            FurnaceInfo furnaceInfo = FurnaceInfoManagement.getInstance().getFurnacePlacedList().stream().filter(item -> item.getLocation().equals(event.getBlock().getLocation())).findFirst().orElse(null);
            if (furnaceInfo != null) {
                FurnaceInfoManagement.getInstance().getFurnaceInventoryList().add(furnaceInfo);
                if(FurnaceInfoManagement.getInstance().removeFurnaceInfo(furnaceInfo)) {
                    author.sendMessage("[Furnace Advanced] Break your furnace successfully! It stores in to your furnace inventory! Type '/furnaceadvanced inventory' to open");
                } else {
                    author.sendMessage("[Furnace Advanced] Failed to break! It looks like this furnace hasn't been managed in this server");
                    event.setCancelled(true);
                    return;
                }

                event.setDropItems(false);
            }
        }
    }
}
