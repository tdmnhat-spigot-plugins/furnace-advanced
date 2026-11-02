package dev.tdmnhat.plugin.spigot.setup;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

public class VaultSetup {

    private FurnaceAdvanced plugin;
    private static Economy economy;
    private static Permission permission;

    public VaultSetup(FurnaceAdvanced plugin) {
        if(plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().log(Level.WARNING, "[Furnace Advanced] Disabled due to no Vault dependency found!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) {
            plugin.getLogger().log(Level.WARNING, "Setup economy failed");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        economy = rsp.getProvider();

        RegisteredServiceProvider<Permission> prsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if(prsp == null) {
            plugin.getLogger().log(Level.WARNING, "Setup permission failed");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        permission = prsp.getProvider();
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Permission getPermission() {
        return permission;
    }
}
