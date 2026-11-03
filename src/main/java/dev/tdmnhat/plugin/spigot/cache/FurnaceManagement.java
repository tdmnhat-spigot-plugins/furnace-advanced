package dev.tdmnhat.plugin.spigot.cache;

import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class FurnaceManagement {

    private final List<FurnaceInformation> list;

    public FurnaceManagement(List<FurnaceInformation> list) {
        this.list = list;
    }

    public void addFurnaceInformation(FurnaceInformation furnaceInformation) {
        list.add(furnaceInformation);
    }

    public void removeFurnaceInformation(FurnaceInformation furnaceInformation) {
        furnaceInformation.getHolograms().forEach(Entity::remove);
        list.remove(furnaceInformation);
    }

    public FurnaceInformation getFurnaceInformation(Location location) {
        return list.stream().filter(
                item -> item.getWorldUUID().equalsIgnoreCase(location.getWorld().getUID().toString())
                && location.getBlockX() == item.getX()
                && location.getBlockY() == item.getY()
                && location.getBlockZ() == item.getZ()
        ).findFirst().orElse(null);
    }

    public List<FurnaceInformation> getList() {
        return list;
    }

    public List<FurnaceInformation> snapshot() {
        List<FurnaceInformation> snapshot = new ArrayList<>(list.size());
        snapshot.addAll(list);
        return snapshot;
    }
}
