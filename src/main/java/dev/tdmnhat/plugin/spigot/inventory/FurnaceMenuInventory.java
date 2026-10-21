package dev.tdmnhat.plugin.spigot.inventory;

import dev.tdmnhat.plugin.spigot.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FurnaceMenuInventory {

    private static final FileConfiguration configuration = Bukkit.getPluginManager().getPlugin("FurnaceAdvanced").getConfig();
    public static final String title = "Furnace Upgrade Menu";

    private Inventory inventory;
    private static Furnace furnace;

    public FurnaceMenuInventory(int currentCookTimeLevel, int currentGenerateAmountLevel, Furnace target) {
        inventory = Bukkit.createInventory(null, 27, title);
        furnace = target;
        addItem(currentCookTimeLevel, currentGenerateAmountLevel);
    }

    private void addItem(int currentCookTime, int currentGenerateAmountLevel) {
        ItemStack generateAmount = new ItemStack(Material.DIAMOND, currentGenerateAmountLevel);
        ItemMeta generateAmountMeta = generateAmount.getItemMeta();

        generateAmount.setItemMeta(generateAmountMeta);
        inventory.setItem(11, generateAmount);

        ItemStack openFurnaceInventory = new ItemStack(Material.FURNACE, 1);
        ItemMeta openFurnaceInventoryMeta = openFurnaceInventory.getItemMeta();

        openFurnaceInventory.setItemMeta(openFurnaceInventoryMeta);
        inventory.setItem(13, openFurnaceInventory);

        // The Material.CLOCK doesn't have in the version 1.12-, it only has in 1.13+
        ItemStack cookTime = new ItemStack(ServerVersion.getMajor() >= 26 || (ServerVersion.getMajor() == 1 && ServerVersion.getMinor() >= 13) ? Material.CLOCK : Material.COMPASS, currentCookTime);
        ItemMeta cookTimeMeta = cookTime.getItemMeta();

        cookTime.setItemMeta(cookTimeMeta);
        inventory.setItem(15, cookTime);
    }

    public Inventory getInventory() { return inventory; }

    public static Furnace getFurnace() {
        return furnace;
    }
}
