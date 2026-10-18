package dev.tdmnhat.plugin.spigot.file;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PlayerDataFile {

    private static File file;
    private static FileConfiguration configuration;

    public static void setup(String playerName) {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
            // Create folder if not exist
            File folder = new File(plugin.getDataFolder() + "/data/");
            if(!folder.exists()) {
                folder.mkdir();
            }

            // Create data file player
            file = new File(folder + "/" + playerName + ".yml");
            if(!file.exists()) {
                file.createNewFile();
            }
            configuration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[Furnace Advanced]" + " " + e.getMessage());
        }
    }

    public static void save() {
        try {
            configuration.save(file);
            configuration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[Furnace Advanced]" + " " + e.getMessage());
        }
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }
}
