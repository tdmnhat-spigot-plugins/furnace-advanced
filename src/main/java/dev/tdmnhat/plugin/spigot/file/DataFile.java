package dev.tdmnhat.plugin.spigot.file;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DataFile {

    private static File file;
    private static FileConfiguration configuration;

    public static void setup() {
        try {
            file = new File(Bukkit.getServer().getPluginManager().getPlugin("FurnaceAdvanced").getDataFolder(), "data.yml");
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[Furnace Advanced]" + e.getMessage());
        }

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
