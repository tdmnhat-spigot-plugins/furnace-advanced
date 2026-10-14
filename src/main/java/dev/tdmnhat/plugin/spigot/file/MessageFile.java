package dev.tdmnhat.plugin.spigot.file;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MessageFile {

    private static File file;
    private static FileConfiguration configuration;

    public static void setup() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
        try {
            plugin.saveResource("messages.yml", false);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[Furnace Advanced]" + e.getMessage());
        }
        file = new File(plugin.getDataFolder(), "messages.yml");
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    public static void save() {
        try {
            configuration.save(file);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[Furnace Advanced]" + e.getMessage());
        }
    }

    public static void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }
}
