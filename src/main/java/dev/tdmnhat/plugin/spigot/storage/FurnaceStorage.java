package dev.tdmnhat.plugin.spigot.storage;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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

            Furnace target = (Furnace) new Location(Bukkit.getWorld(UUID.fromString(world)), furnace.getInt("x"), furnace.getInt("y"), furnace.getInt("z")).getBlock().getState();
            list.add(new FurnaceInformation(
                    furnace.getString("worldName"),
                    world,
                    furnace.getInt("x"), furnace.getInt("y"), furnace.getInt("z"),
                    furnace.getInt("cookTimeLevel", 1),
                    furnace.getInt("generateAmountLevel", 1),
                    furnace.getString("authorName"),
                    furnace.getString("authorUUID"),
                    new ArrayList<>(){{
                        add(generateHologram(furnace.getString("authorName"), target.getBlock(), 1.0));
                        add(generateHologram("Cook Level: " + furnace.getInt("cookTimeLevel"), target.getBlock(), 0.75));
                        add(generateHologram("Generate Level: " + furnace.getInt("generateAmountLevel"), target.getBlock(), 0.5));
                        add(generateHologram(Optional.ofNullable(target.getInventory().getSmelting()).orElse(new ItemStack(Material.AIR, 0)).getAmount()
                                + " ➜ " +
                                Optional.ofNullable(target.getInventory().getResult()).orElse(new ItemStack(Material.AIR, 0)).getAmount(), target.getBlock(), 0.25));
                    }}
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

        configuration.save(this.temp);
        if(file.exists()) {
            Files.copy(file.toPath(), bak.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private ArmorStand generateHologram(String name, Block target, double addHeight) {
        ArmorStand armorStand = (ArmorStand) target.getWorld().spawnEntity(target.getLocation().add(0.5, addHeight, 0.5).subtract(0, 0.8, 0), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        return armorStand;
    }
}
