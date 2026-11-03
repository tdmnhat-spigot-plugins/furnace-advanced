package dev.tdmnhat.plugin.spigot.util.hologram;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class FurnaceHologram extends Hologram {

    private static final String MARKER = "§r§f§u§r§n§a§c§e§r";

    public FurnaceHologram(String name, Block target, double height) {
        super(MARKER + name, target, height);
    }

    @Override
    public void renameHologram(String newName) {
        this.hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', MARKER + newName));
    }

    public static boolean isHologram(Entity entity) {
        if(!(entity instanceof ArmorStand)) {
            return false;
        }
        String getName = entity.getCustomName();
        return getName != null && getName.startsWith(MARKER);
    }
}
