package dev.tdmnhat.plugin.spigot.util.hologram;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public abstract class Hologram {

    protected ArmorStand hologram;

    public Hologram(String name, Block target, double height) {
        hologram = (ArmorStand) target.getWorld().spawnEntity(target.getLocation().add(0.5, height, 0.5).subtract(0, 0.8, 0), EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
        hologram.setCustomNameVisible(true);
    }

    public abstract void renameHologram(String newName);

    public ArmorStand getHologram() {
        return hologram;
    }
}
