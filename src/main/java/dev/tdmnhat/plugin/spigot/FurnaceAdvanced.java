package dev.tdmnhat.plugin.spigot;

import dev.tdmnhat.plugin.spigot.async.FileAsyncAction;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.listener.*;
import dev.tdmnhat.plugin.spigot.setup.VaultSetup;
import dev.tdmnhat.plugin.spigot.storage.FurnaceStorage;
import dev.tdmnhat.plugin.spigot.util.hologram.FurnaceHologram;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;

public final class FurnaceAdvanced extends JavaPlugin {

    private FurnaceManagement furnaceManagement;
    private FurnaceStorage furnaceStorage;

    @Override
    public void onLoad() {
        try {
            furnaceManagement = new FurnaceManagement(new ArrayList<>());
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[Furnace Advanced] " + e.getMessage());
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        // Load configuration file
        registerConfig();

        // Load listeners
        registerListeners();

        // Load commands
        registerCommands();

        // Set up the pre-plugin
        setUpPrePlugin();

        // Run statistics
        int pluginId = 35122;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("chart_id", () -> "My value"));

        // Do any other actions
        //// Clear old holograms
        for(World world : this.getServer().getWorlds()) {
            world.getEntities().stream().filter(FurnaceHologram::isHologram).forEach(Entity::remove);
        }

        furnaceStorage = new FurnaceStorage(this);
        for (FurnaceInformation furnaceInformation : furnaceStorage.load()) {
            furnaceManagement.addFurnaceInformation(furnaceInformation);
        }

        FileAsyncAction.startAutoSaveFurnaces(this, furnaceStorage, furnaceManagement);
    }

    @Override
    public void onDisable() {
        // Removing all the holograms
        try {
            furnaceStorage.save(furnaceManagement.getList());
            furnaceManagement.getList().forEach(item -> item.getHolograms().forEach(Entity::remove));
        } catch (IOException e) {
            getLogger().severe("Save failed: " + e.getMessage());
        } finally {
            furnaceManagement.getList().removeAll(furnaceManagement.getList());
            Bukkit.getConsoleSender().sendMessage("The plugin has been disabled.");
        }
    }

    private void setUpPrePlugin() {
        new VaultSetup(this);   // Setup Vault
    }

    private void registerConfig() {
        // config.yml
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
    }

    private void registerListeners() {
        // Other events

        // Menu Inventory
        this.getServer().getPluginManager().registerEvents(new FurnaceInventoryListener(this, furnaceManagement), this);
        this.getServer().getPluginManager().registerEvents(new InventoryMenuItemListener(), this);

        // Action Inventory
        this.getServer().getPluginManager().registerEvents(new FurnacePlaceBreakListener(furnaceManagement), this);
        this.getServer().getPluginManager().registerEvents(new FurnaceCookListener(this, furnaceManagement), this);
    }

    private void registerCommands() {

    }
}
