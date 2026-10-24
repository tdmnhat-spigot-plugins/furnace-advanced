package dev.tdmnhat.plugin.spigot.file;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlayerDataFile {

    private static File file;
    private static FileConfiguration configuration;

    public static void setup(String uuid) {
        try {
            FurnaceAdvanced plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
            // Create folder if not exist
            new File(plugin.getDataFolder() + "/data").mkdir();

            // Create file
            file = new File(plugin.getDataFolder() + "/data/", uuid + ".yml");
            if(!file.exists()) {
                file.createNewFile();
            }

            configuration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[Furnace Advanced] " + e.getMessage());
        }
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    public static void save() {
        try {
          configuration.save(file);
          reload();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[Furnace Advanced] " + e.getMessage());
        }
    }

    public static void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }
}
