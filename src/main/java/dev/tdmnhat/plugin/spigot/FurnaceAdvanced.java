package dev.tdmnhat.plugin.spigot;

import dev.tdmnhat.plugin.spigot.command.MenuCommand;
import dev.tdmnhat.plugin.spigot.listener.FurnaceListener;
import dev.tdmnhat.plugin.spigot.listener.PlayerInteractFurnaceListener;
import dev.tdmnhat.plugin.spigot.listener.inventory.FurnaceMenuListener;
import dev.tdmnhat.plugin.spigot.listener.inventory.ListFurnaceMenuListener;
import dev.tdmnhat.plugin.spigot.listener.inventory.OverviewMenuListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class FurnaceAdvanced extends JavaPlugin {

    @Override
    public void onEnable() {
        // Load configuration file
        registerConfig();

        // Plugin startup logic
        registerListeners();
        registerCommands();
    }

    private void registerConfig() {
        // config.yml
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        saveResource("messages.yml", false);
    }

    private void registerListeners() {
        // Menu Inventory
        this.getServer().getPluginManager().registerEvents(new OverviewMenuListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ListFurnaceMenuListener(this), this);
        this.getServer().getPluginManager().registerEvents(new FurnaceMenuListener(this), this);

        // Action Inventory
        this.getServer().getPluginManager().registerEvents(new PlayerInteractFurnaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new FurnaceListener(this), this);
    }

    private void registerCommands() {
        this.getCommand("fa").setExecutor(new MenuCommand(this));
    }

    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }
}
