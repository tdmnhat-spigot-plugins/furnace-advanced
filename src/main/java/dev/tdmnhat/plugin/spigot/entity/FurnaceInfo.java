package dev.tdmnhat.plugin.spigot.entity;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

public class FurnaceInfo {

    private String id;
    private String author;
    private int cookLevel;
    private int generateAmountLevel;
    private Location location;
    private List<ArmorStand> armorStands;

    public FurnaceInfo() {
    }

    public FurnaceInfo(String id, String author, int cookLevel, int generateAmountLevel, Location location) {
        this.id = id;
        this.author = author;
        this.cookLevel = cookLevel;
        this.generateAmountLevel = generateAmountLevel;
        this.location = location;
        this.armorStands = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCookLevel() {
        return cookLevel;
    }

    public void setCookLevel(int cookLevel) {
        this.cookLevel = cookLevel;
    }

    public int getGenerateAmountLevel() {
        return generateAmountLevel;
    }

    public void setGenerateAmountLevel(int generateAmountLevel) {
        this.generateAmountLevel = generateAmountLevel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public void setArmorStands(List<ArmorStand> armorStands) {
        this.armorStands = armorStands;
    }
}
