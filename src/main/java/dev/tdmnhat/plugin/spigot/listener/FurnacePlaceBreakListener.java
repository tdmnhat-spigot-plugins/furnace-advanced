package dev.tdmnhat.plugin.spigot.listener;

import de.tr7zw.nbtapi.NBT;
import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class FurnacePlaceBreakListener implements Listener {

    private final FurnaceManagement furnaceManagement;
    private final FurnaceAdvanced plugin;

    public FurnacePlaceBreakListener(FurnaceManagement furnaceManagement) {
        this.furnaceManagement = furnaceManagement;
        plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // Handles
        Material material = event.getBlock().getType();
        Player player = event.getPlayer();
        if (checkIfTheBlockIsFurnace(material)) {
            Furnace blockPlaced = (Furnace) event.getBlock().getState();
            ItemStack itemInHand = getFurnaceInHand(event);

            String author = NBT.get(itemInHand, nbt -> (String) nbt.getString("Author"));
            Integer cookLevel = NBT.get(itemInHand, nbt -> (Integer) nbt.getInteger("Cook Level"));
            Integer generateLevel = NBT.get(itemInHand, nbt -> (Integer) nbt.getInteger("Generate Level"));

            FurnaceInformation furnaceInformation;
            if (author.isEmpty() || cookLevel == 0 || generateLevel == 0) {
                furnaceInformation = generateFurnaceInformation(
                        blockPlaced.getLocation(),
                        1,
                        1,
                        player.getUniqueId().toString()
                );
            } else {
                furnaceInformation = generateFurnaceInformation(
                        blockPlaced.getLocation(),
                        cookLevel,
                        generateLevel,
                        player.getUniqueId().toString()
                );
            }
            furnaceManagement.addFurnaceInformation(furnaceInformation);
        }
    }

    private ItemStack getFurnaceInHand(BlockPlaceEvent event) {
        if ((ServerVersion.getMajor() == 1 && ServerVersion.getMinor() < 8) || ServerVersion.getMajor() >= 26) {
            return event.getPlayer().getInventory().getItemInMainHand();
        } else {
            return event.getPlayer().getItemInHand();
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        if (checkIfTheBlockIsFurnace(material)) {
            Block block = event.getBlock();
            FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(block.getLocation());

            if (furnaceInformation != null) {
                ItemStack itemStack = new ItemStack(block.getType(), 1);
                NBT.modify(itemStack, nbt -> {
                    nbt.setString("Author", event.getPlayer().getUniqueId().toString().equals(furnaceInformation.getAuthorUUID()) ? event.getPlayer().getName() : "Anonymous");
                    nbt.setInteger("Cook Level", furnaceInformation.getCookTimeLevel());
                    nbt.setInteger("Generate Level", furnaceInformation.getGenerateAmountLevel());
                });
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("[Furnace Advanced] Furnace Technology");
                itemMeta.setLore(new ArrayList<>(){{
                    add("Author: " + (furnaceInformation.getAuthorUUID().toString().equals(event.getPlayer().getUniqueId().toString()) ? event.getPlayer().getName() : "Anonymous"));
                    add("Cook Level: " + furnaceInformation.getCookTimeLevel());
                    add("Generate Level: " + furnaceInformation.getGenerateAmountLevel());
                }});
                itemStack.setItemMeta(itemMeta);

                // Disable drop items. The version from 1.12+ has the method setDropItems()
                if (ServerVersion.getMajor() >= 26 || (ServerVersion.getMinor() >= 12)) {
                    event.setDropItems(false);
                } else {
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                }

                event.getPlayer().getInventory().addItem(itemStack);
            } else {
                event.getPlayer().sendMessage("[Furnace Advanced] This furnace is not registered the advanced technology");
            }
        }
    }

    private boolean checkIfTheBlockIsFurnace(Material target) {
        return target.toString().equalsIgnoreCase("FURNACE") || target.toString().equalsIgnoreCase("BLAST_FURNACE") || target.toString().equalsIgnoreCase("BURNING_FURNACE");
    }

    private ArmorStand generateHologram(String name, Block target, double addHeight) {
        ArmorStand armorStand = (ArmorStand) target.getWorld().spawnEntity(target.getLocation().add(0.5, addHeight, 0.5).subtract(0, 0.8, 0), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        return armorStand;
    }

    private FurnaceInformation generateFurnaceInformation(Location world, int cookTimeLevel, int generateAmountLevel, String playerUuid) {
        return new FurnaceInformation(world.getWorld().getUID().toString(), world.getBlockX(), world.getBlockY(), world.getBlockZ(), cookTimeLevel, generateAmountLevel, playerUuid);
    }
}
