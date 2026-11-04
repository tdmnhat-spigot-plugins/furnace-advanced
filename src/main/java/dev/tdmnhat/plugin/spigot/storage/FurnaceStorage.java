package dev.tdmnhat.plugin.spigot.storage;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.util.hologram.FurnaceHologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

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

            try {
                Furnace target = (Furnace) new Location(Bukkit.getWorld(UUID.fromString(world)), furnace.getInt("x"), furnace.getInt("y"), furnace.getInt("z")).getBlock().getState();
                list.add(new FurnaceInformation(
                        furnace.getString("worldName"),
                        world,
                        furnace.getInt("x"), furnace.getInt("y"), furnace.getInt("z"),
                        furnace.getInt("cookTimeLevel", 1),
                        furnace.getInt("generateAmountLevel", 1),
                        furnace.getString("authorName"),
                        furnace.getString("authorUUID"),
                        new ArrayList<>() {{
                            add(generateHologram(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.1st-line").replace("%player_name%", furnace.getString("authorName"))), target.getBlock(), 1.0));
                            add(generateHologram(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.2nd-line").replace("%furnace_cook_level%", String.valueOf(furnace.getInt("cookTimeLevel")))), target.getBlock(), 0.75));
                            add(generateHologram(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.3rd-line").replace("%furnace_generate_level%", String.valueOf(furnace.getInt("generateAmountLevel")))), target.getBlock(), 0.5));
                            add(generateHologram(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.4th-line")
                                    .replace("%furnace_smelting_amount%", String.valueOf(Optional.ofNullable(target.getInventory().getSmelting()).orElse(new ItemStack(Material.AIR, 0)).getAmount()))
                                    .replace("%furnace_resulting_amount%", String.valueOf(Optional.ofNullable(target.getInventory().getResult()).orElse(new ItemStack(Material.AIR, 0)).getAmount()))), target.getBlock(), 0.25));
                        }}
                ));
            } catch (Exception ex) {
                continue;
            }
        }
        return list;
    }

    public synchronized void save(Collection<FurnaceInformation> snapshot) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        if(snapshot.isEmpty() && file.exists() && file.length() > 100) {
            configuration.set("furnaces", null);
        } else if(snapshot.isEmpty()) {
            configuration.set("furnaces", null);
        } else {
            for (FurnaceInformation furnaceInformation : snapshot) {
                String path = "furnaces." + furnaceInformation.getKey().replace(";", "_");
                configuration.set(path + ".worldName", furnaceInformation.getWorldName());
                configuration.set(path + ".worldUUID", furnaceInformation.getWorldUUID());
                configuration.set(path + ".x", furnaceInformation.getX());
                configuration.set(path + ".y", furnaceInformation.getY());
                configuration.set(path + ".z", furnaceInformation.getZ());
                configuration.set(path + ".cookTimeLevel", furnaceInformation.getCookTimeLevel());
                configuration.set(path + ".generateAmountLevel", furnaceInformation.getGenerateAmountLevel());
                configuration.set(path + ".authorName", furnaceInformation.getAuthorName());
                configuration.set(path + ".authorUUID", furnaceInformation.getAuthorUUID());
            }
        }

        configuration.save(this.temp);
        if(file.exists()) {
            Files.copy(file.toPath(), bak.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private ArmorStand generateHologram(String name, Block target, double addHeight) {
        return new FurnaceHologram(name, target, addHeight).getHologram();
    }
}
