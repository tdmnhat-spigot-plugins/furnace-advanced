package dev.tdmnhat.plugin.spigot;

import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.command.FurnaceCommand;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import dev.tdmnhat.plugin.spigot.listener.FurnaceCookListener;
import dev.tdmnhat.plugin.spigot.listener.FurnacePlaceBreakListener;
import dev.tdmnhat.plugin.spigot.listener.FurnaceInventoryListener;
import dev.tdmnhat.plugin.spigot.listener.menu.FurnaceMenuListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
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

    @Override
    public void onDisable() {
        // Removing all the holograms
        FurnaceManagement.getInstance().getFurnaceInfos().stream().map(FurnaceInfo::getHolograms).forEach(armorStands -> armorStands.forEach(Entity::remove));
    }

    private void registerConfig() {
        // config.yml
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
    }

    private void registerListeners() {
        // Menu Inventory
        this.getServer().getPluginManager().registerEvents(new FurnaceInventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new FurnaceMenuListener(), this);

        // Action Inventory
        this.getServer().getPluginManager().registerEvents(new FurnacePlaceBreakListener(), this);
        this.getServer().getPluginManager().registerEvents(new FurnaceCookListener(), this);
    }

    private void registerCommands() {
        this.getCommand("furnaceadvanced").setExecutor(new FurnaceCommand(this));
    }

    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
    }
}
