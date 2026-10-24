package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import dev.tdmnhat.plugin.spigot.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FurnacePlaceBreakListener implements Listener {

    private FurnaceAdvanced plugin;

    public FurnacePlaceBreakListener() {
        plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if(checkIfTheBlockIsFurnace(event.getBlockAgainst().getType())) {
            event.getPlayer().sendMessage("Interrupted event");
            event.setCancelled(true);
        }

        // Handle
        Material material = event.getBlock().getType();
        Player player = event.getPlayer();
        if (checkIfTheBlockIsFurnace(material)) {
            Furnace blockPlaced = (Furnace) event.getBlock().getState();
            ItemMeta furnaceItemMeta = getFurnaceItemMeta(event);

            FurnaceInfo furnaceInfo;
            if (furnaceItemMeta == null || furnaceItemMeta.getLore() == null) {
                furnaceInfo = generateFurnaceInfo(
                        "1",
                        player.getName(),
                        blockPlaced.getType().toString(),
                        1,
                        1,
                        blockPlaced.getLocation(),
                        List.of(generateHologram(player.getName(), blockPlaced.getBlock(), 1),
                                generateHologram("Cook Time Level: 1", blockPlaced.getBlock(), 0.75),
                                generateHologram("Generate Level: 1", blockPlaced.getBlock(), 0.5),
                                generateHologram("0 ➜ 0", blockPlaced.getBlock(), 0.25)
                        )
                );
            } else {
                String id = furnaceItemMeta.getLore().stream().filter(lore -> lore.contains("ID:")).findFirst().orElse("ID: 1").replace("ID:", "").trim();
                String author = furnaceItemMeta.getLore().stream().filter(lore -> lore.contains("Author:")).findFirst().orElse("Author: " + player.getName()).replace("Author:", "").trim();
                int cookTimeLevel = Integer.parseInt(furnaceItemMeta.getLore().stream().filter(lore -> lore.contains("Cook Time Level:")).findFirst().orElse("Cook Time Level: 1").replace("Cook Time Level:", "").trim());
                int generateAmountLevel = Integer.parseInt(furnaceItemMeta.getLore().stream().filter(lore -> lore.contains("Generate Amount Level:")).findFirst().orElse("Generate Amount Level: 1").replace("Generate Amount Level:", "").trim());

                furnaceInfo = generateFurnaceInfo(id, author, blockPlaced.getType().toString(), cookTimeLevel, generateAmountLevel, blockPlaced.getLocation(), List.of(
                        generateHologram(player.getName(), blockPlaced.getBlock(), 1),
                        generateHologram("Cook Tim Level: " + cookTimeLevel, blockPlaced.getBlock(), 0.75),
                        generateHologram("Generate Level: " + generateAmountLevel, blockPlaced.getBlock(), 0.5),
                        generateHologram("0 ➜ 0", blockPlaced.getBlock(), 0.25)
                ));
            }
            FurnaceManagement.getInstance().addFurnace(player, furnaceInfo);
            player.sendMessage("[Size] " + FurnaceManagement.getInstance().getListFurnaces(player).size() + " Furnaces");
        }
    }

    private ItemMeta getFurnaceItemMeta(BlockPlaceEvent event) {
        ItemStack getFurnaceInHand;

        // Get information from the furnace in player's hand
        if ((ServerVersion.getMajor() == 1 && ServerVersion.getMinor() < 8) || ServerVersion.getMajor() >= 26) {
            getFurnaceInHand = event.getPlayer().getInventory().getItemInMainHand();
        } else {
            getFurnaceInHand = event.getPlayer().getItemInHand();
        }

        // Retrieve information from the furnace in player's hand and store into memory cache
        return getFurnaceInHand.getItemMeta();
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        if (checkIfTheBlockIsFurnace(material)) {
            Block block = event.getBlock();
            FurnaceInfo furnaceInfo = FurnaceManagement.getInstance().getListFurnaces(event.getPlayer()).stream().filter(furnace -> furnace.getPlaceLocation().equals(block.getLocation())).findFirst().orElse(null);

            if (furnaceInfo != null) {
                ItemStack itemStack = new ItemStack(block.getType());
                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setDisplayName("[Furnace Advanced] Furnace Technology");
                itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
                itemMeta.setLore(List.of(
                        "Author: " + furnaceInfo.getAuthor(),
                        "Cook Time Level: " + furnaceInfo.getCookTimeLevel(),
                        "Generate Amount Level: " + furnaceInfo.getGenerateAmountLevel()
                ));

                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemStack.setItemMeta(itemMeta);
                event.getPlayer().getInventory().addItem(itemStack);

                // Remove hologram & furnace management
                furnaceInfo.getHolograms().forEach(Entity::remove);
                FurnaceManagement.getInstance().getListFurnaces(event.getPlayer()).remove(furnaceInfo);
                event.getPlayer().sendMessage("[Size] " + FurnaceManagement.getInstance().getListFurnaces(event.getPlayer()).size() + " Furnaces");

                // Disable drop items. The version from 1.12+ has the method setDropItems()
                if (ServerVersion.getMajor() >= 26 || (ServerVersion.getMinor() >= 12)) {
                    event.setDropItems(false);
                } else {
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                }
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

    private FurnaceInfo generateFurnaceInfo(String id, String author, String type, int cookTimeLevel, int generateAmountLevel, Location placedLocation, List<ArmorStand> holograms) {
        return new FurnaceInfo(author, type, cookTimeLevel, generateAmountLevel, placedLocation, holograms);
    }
}
