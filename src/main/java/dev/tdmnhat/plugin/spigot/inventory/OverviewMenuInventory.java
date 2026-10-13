package dev.tdmnhat.plugin.spigot.inventory;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class OverviewMenuInventory {

    private FurnaceAdvanced plugin;
    private static Inventory inventory;

    public OverviewMenuInventory(FurnaceAdvanced plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, 9, plugin.getConfig().getString("overview-menu.title"));
        addItem();
    }

    private void addItem() {
        // List commands
        ItemStack help = new ItemStack(Material.PAPER, 1);
        ItemMeta helpMeta = help.getItemMeta();
        helpMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("overview-menu.list-command.display-name")));

        List<String> helpLores = plugin.getConfig().getList("overview-menu.list-command.lore").stream().map(item -> ChatColor.translateAlternateColorCodes('&', (String) item)).toList();
        helpMeta.setLore(helpLores);

        help.setItemMeta(helpMeta);
        inventory.setItem(2, help);

        // How to upgrade the furnace
        ItemStack helpUpgrade = new ItemStack(Material.BOOK, 1);
        ItemMeta helpUpgradeMeta = helpUpgrade.getItemMeta();
        helpUpgradeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("overview-menu.help-upgrade.display-name")));

        List<String> helpUpgradeLores = plugin.getConfig().getList("overview-menu.menu-furnaces.lore").stream().map(item -> ChatColor.translateAlternateColorCodes('&', (String) item)).toList();
        helpUpgradeMeta.setLore(helpUpgradeLores);

        helpUpgrade.setItemMeta(helpUpgradeMeta);
        inventory.setItem(4, helpUpgrade);

        // Open list furnaces that player has placed
        ItemStack furnaces = new ItemStack(Material.FURNACE, 1);
        ItemMeta furnacesMeta = furnaces.getItemMeta();
        furnacesMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("overview-menu.menu-furnaces.display-name")));

        List<String> furnacesLore = plugin.getConfig().getList("overview-menu.menu-furnaces.lore").stream().map(item -> ChatColor.translateAlternateColorCodes('&', (String) item)).toList();
        furnacesMeta.setLore(furnacesLore);

        furnaces.setItemMeta(furnacesMeta);
        inventory.setItem(6, furnaces);
    }

    public static Inventory getInventory() {
        return inventory;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
