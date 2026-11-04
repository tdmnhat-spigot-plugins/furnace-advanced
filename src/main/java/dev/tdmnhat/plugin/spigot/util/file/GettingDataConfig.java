package dev.tdmnhat.plugin.spigot.util.file;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.dto.CookLevelDto;
import dev.tdmnhat.plugin.spigot.dto.GenerateLevelDto;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GettingDataConfig {

    private FileConfiguration configuration;

    public GettingDataConfig(FurnaceAdvanced furnaceAdvanced) {
        this.configuration = furnaceAdvanced.getConfig();
    }

    public List<GenerateLevelDto> getListGenerateLevel() {
        List<GenerateLevelDto> list = new ArrayList<>();
        ConfigurationSection section = configuration.getConfigurationSection("generate-level");
        if (section == null) return null;

        for (String key : section.getKeys(false)) {
            int level = Integer.parseInt(key);

            double cost = configuration.getDouble("generate-level." + level + ".cost");
            int min = configuration.getInt("generate-level." + level + ".min");
            int max = configuration.getInt("generate-level." + level + ".max");

            list.add(new GenerateLevelDto(level, cost, min, max));
        }
        return list;
    }

    public List<CookLevelDto> getListCookTime() {
        List<CookLevelDto> list = new ArrayList<>();
        ConfigurationSection section = configuration.getConfigurationSection("cook-level");
        if (section == null) return null;

        for (String key : section.getKeys(false)) {
            int level = Integer.parseInt(key);

            double cost = configuration.getDouble("cook-level." + level + ".cost");
            int cookTime = configuration.getInt("cook-level." + level + ".cook-time");

            list.add(new CookLevelDto(level, cost, cookTime));
        }
        return list;
    }
}
