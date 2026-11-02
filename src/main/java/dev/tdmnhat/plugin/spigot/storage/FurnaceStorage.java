package dev.tdmnhat.plugin.spigot.storage;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FurnaceStorage {

    private final FurnaceAdvanced plugin;
    private final File file;
    private final File bak;
    private final File temp;

    public FurnaceStorage(FurnaceAdvanced plugin) {
        this.plugin = plugin;

        File dir = plugin.getDataFolder();
        dir.mkdirs();

        this.file = new File(dir, "furnaces.yml");
        this.bak = new File(dir, "furnaces.yml.bak");
        this.temp =  new File(dir, "furnaces.yml.tmp");
    }

    public List<FurnaceInformation> load() {
        List<FurnaceInformation> list = new ArrayList<>();
        if(!file.exists()) {
            return list;
        }

        YamlConfiguration configuration =  YamlConfiguration.loadConfiguration(file);
        ConfigurationSection root = configuration.getConfigurationSection("furnaces");
        if(root == null) {
            return list;
        }

        for(String id : root.getKeys(false)) {
            ConfigurationSection furnace = root.getConfigurationSection(id);
            if(furnace == null) continue;

            String world = furnace.getString("worldUUID");
            if(world == null) {
                plugin.getLogger().warning("Skipped invalid furnace entry: " + id);
                continue;
            }

            list.add(new FurnaceInformation(
                    world,
                    furnace.getInt("x"), furnace.getInt("y"), furnace.getInt("z"),
                    furnace.getInt("cookTimeLevel", 1),
                    furnace.getInt("generateAmountLevel", 1),
                    furnace.getString("authorUUID")
            ));
        }
        return list;
    }

    public synchronized void save(Collection<FurnaceInformation> snapshot) throws IOException {
        if(snapshot.isEmpty() && file.exists() && file.length() > 100) {
            plugin.getLogger().warning("Cache is empty but furnaces.yml has data. Save skipped.");
            return;
        }

        YamlConfiguration configuration = new YamlConfiguration();
        for(FurnaceInformation furnaceInformation : snapshot) {
            String path = "furnaces." + furnaceInformation.getKey().replace(";", "_");
            configuration.set(path + ".worldUUID", furnaceInformation.getWorldUUID());
            configuration.set(path + ".x", furnaceInformation.getX());
            configuration.set(path + ".y", furnaceInformation.getY());
            configuration.set(path + ".z", furnaceInformation.getZ());
            configuration.set(path + ".cookTimeLevel", furnaceInformation.getCookTimeLevel());
            configuration.set(path + ".generateAmountLevel", furnaceInformation.getGenerateAmountLevel());
            configuration.set(path + ".authorUUID", furnaceInformation.getAuthorUUID());
        }

        configuration.save(this.temp);
        if(file.exists()) {
            Files.copy(file.toPath(), bak.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
