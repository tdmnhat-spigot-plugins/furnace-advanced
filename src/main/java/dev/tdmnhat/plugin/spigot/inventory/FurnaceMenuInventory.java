package dev.tdmnhat.plugin.spigot.inventory;

import dev.tdmnhat.plugin.spigot.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class FurnaceMenuInventory implements InventoryHolder {

    private static final FileConfiguration configuration = Bukkit.getPluginManager().getPlugin("FurnaceAdvanced").getConfig();
    public static final String title = "Furnace Upgrade Menu";

    private Inventory inventory;
    private static Furnace furnace;

    public FurnaceMenuInventory(int currentCookTimeLevel, int currentGenerateAmountLevel, Furnace target) {
        this.inventory = Bukkit.createInventory(null, 27, title);
        furnace = target;
        addItem(currentCookTimeLevel, currentGenerateAmountLevel);
    }

    private void addItem(int currentCookTime, int currentGenerateAmountLevel) {
        ItemStack generateAmount = new ItemStack(Material.DIAMOND, currentGenerateAmountLevel);
        ItemMeta generateAmountMeta = generateAmount.getItemMeta();
        generateAmountMeta.setDisplayName(ChatColor.AQUA + "Upgrade the generate amount");
        generateAmount.setItemMeta(generateAmountMeta);
        inventory.setItem(11, generateAmount);

        ItemStack openFurnaceInventory = new ItemStack(Material.FURNACE, 1);
        ItemMeta openFurnaceInventoryMeta = openFurnaceInventory.getItemMeta();
        openFurnaceInventoryMeta.setDisplayName(ChatColor.GREEN + "Open the main furnace inventory");
        openFurnaceInventoryMeta.setLore(new ArrayList<>(){{
            add(ChatColor.WHITE + "This main inventory for adding ingredient for smelting");
            add(ChatColor.YELLOW + "The progress will be updated continuously in top this furnace");
        }});
        openFurnaceInventory.setItemMeta(openFurnaceInventoryMeta);
        inventory.setItem(13, openFurnaceInventory);

        // The Material.CLOCK doesn't have in the version 1.12-, it only has in 1.13+
        ItemStack cookTime = new ItemStack(ServerVersion.getMajor() >= 26 || (ServerVersion.getMajor() == 1 && ServerVersion.getMinor() >= 13) ? Material.CLOCK : Material.COMPASS, currentCookTime);
        ItemMeta cookTimeMeta = cookTime.getItemMeta();
        cookTimeMeta.setDisplayName(ChatColor.YELLOW + "Upgrade the cooking time");
        cookTime.setItemMeta(cookTimeMeta);
        inventory.setItem(15, cookTime);

        // Close inventory item
        ItemStack closeItem = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta closeItemMeta = closeItem.getItemMeta();
        closeItemMeta.setDisplayName(ChatColor.RED + "Close Menu");
        closeItem.setItemMeta(closeItemMeta);
        inventory.setItem(22, closeItem);
    }

    public Inventory getInventory() { return inventory; }

    public static Furnace getFurnace() {
        return furnace;
    }
}
