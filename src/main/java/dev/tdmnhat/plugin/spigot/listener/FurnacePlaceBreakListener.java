package dev.tdmnhat.plugin.spigot.listener;

import de.tr7zw.nbtapi.NBT;
import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.util.ServerVersion;
import dev.tdmnhat.plugin.spigot.util.hologram.FurnaceHologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
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
                        player.getUniqueId().toString(),
                        player.getName()
                );
            } else {
                furnaceInformation = generateFurnaceInformation(
                        blockPlaced.getLocation(),
                        cookLevel,
                        generateLevel,
                        player.getUniqueId().toString(),
                        player.getName()
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
                    nbt.setString("Author", event.getPlayer().getName());
                    nbt.setInteger("Cook Level", furnaceInformation.getCookTimeLevel());
                    nbt.setInteger("Generate Level", furnaceInformation.getGenerateAmountLevel());
                });
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item-inventory.display-name")));
                itemMeta.setLore(new ArrayList<>(){{
                    add(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item-inventory.line.1st-line").replace("%player_name%", furnaceInformation.getAuthorName())));
                    add(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item-inventory.line.2nd-line").replace("%furnace_cook_level%", String.valueOf(furnaceInformation.getCookTimeLevel()))));
                    add(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item-inventory.line.3rd-line").replace("%furnace_generate_level%", String.valueOf(furnaceInformation.getGenerateAmountLevel()))));
                    addAll(plugin.getConfig().getStringList("item-inventory.description").stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).toList());
                }});
                itemStack.setItemMeta(itemMeta);

                // Disable drop items. The version from 1.12+ has the method setDropItems()
                if (ServerVersion.getMajor() >= 26 || (ServerVersion.getMinor() >= 12)) {
                    event.setDropItems(false);
                } else {
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                }

                furnaceManagement.removeFurnaceInformation(furnaceInformation);
                event.getPlayer().getInventory().addItem(itemStack);
            }
        }
    }

    private boolean checkIfTheBlockIsFurnace(Material target) {
        return target.toString().equalsIgnoreCase("FURNACE") || target.toString().equalsIgnoreCase("BURNING_FURNACE");
    }

    private ArmorStand generateHologram(String name, Block target, double addHeight) {
        return new FurnaceHologram(name, target, addHeight).getHologram();
    }

    private FurnaceInformation generateFurnaceInformation(Location world, int cookTimeLevel, int generateAmountLevel, String playerUuid, String playerName) {
        return new FurnaceInformation(world.getWorld().getName(), world.getWorld().getUID().toString(), world.getBlockX(), world.getBlockY(), world.getBlockZ(), cookTimeLevel, generateAmountLevel, playerName, playerUuid, new ArrayList<>(){{
            add(generateHologram(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.1st-line").replace("%player_name%", playerName)), world.getBlock(), 1.0));
            add(generateHologram(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.2nd-line").replace("%furnace_cook_level%", String.valueOf(cookTimeLevel))), world.getBlock(), 0.75));
            add(generateHologram(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.3rd-line").replace("%furnace_generate_level%", String.valueOf(generateAmountLevel))), world.getBlock(), 0.5));
            add(generateHologram(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.4th-line").replace("%furnace_smelting_amount%", "0").replace("%furnace_resulting_amount%", "0")), world.getBlock(), 0.25));
        }});
    }
}
