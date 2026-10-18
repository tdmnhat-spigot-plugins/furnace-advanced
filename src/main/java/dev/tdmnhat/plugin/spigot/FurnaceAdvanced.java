package dev.tdmnhat.plugin.spigot;

import dev.tdmnhat.plugin.spigot.command.FurnaceCommand;
import dev.tdmnhat.plugin.spigot.listener.FurnaceCookListener;
import dev.tdmnhat.plugin.spigot.listener.PlayerInteractFurnaceListener;
import dev.tdmnhat.plugin.spigot.listener.PlayerListener;
import dev.tdmnhat.plugin.spigot.listener.inventory.FurnaceMenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class FurnaceAdvanced extends JavaPlugin {

    @Override
    public void onEnable() {
        // Load configuration file
        registerConfig();

        // Load listeners
        registerListeners();

        // Load commands
        registerCommands();
    }

    private void registerConfig() {
        // config.yml
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
    }

    private void registerListeners() {
        // Menu Inventory
        this.getServer().getPluginManager().registerEvents(new FurnaceMenuListener(this), this);

        // Action Inventory
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractFurnaceListener(this), this);
        this.getServer().getPluginManager().registerEvents(new FurnaceCookListener(this), this);
    }

    private void registerCommands() {
        this.getCommand("furnaceadvanced").setExecutor(new FurnaceCommand(this));
    }

    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }
}
