package dev.tdmnhat.plugin.spigot.entity;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FurnaceInfo {

    private String author;
    // The old version doesn't have BLAST_FURNACE
    private String type;
    private int cookTimeLevel;
    private int generateAmountLevel;
    private Location placeLocation;
    private List<ArmorStand> holograms;

    public FurnaceInfo() {
    }

    public FurnaceInfo(String author, String type, int cookTimeLevel, int generateAmountLevel, Location placeLocation) {
        this.author = author;
        this.type = type;
        this.cookTimeLevel = cookTimeLevel;
        this.generateAmountLevel = generateAmountLevel;
        this.placeLocation = placeLocation;
        this.holograms = new ArrayList<>();
    }

    public FurnaceInfo(String author, String type, int cookTimeLevel, int generateAmountLevel, Location placeLocation, List<ArmorStand> holograms) {
        this.author = author;
        this.type = type;
        this.cookTimeLevel = cookTimeLevel;
        this.generateAmountLevel = generateAmountLevel;
        this.placeLocation = placeLocation;
        this.holograms = holograms;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCookTimeLevel() {
        return cookTimeLevel;
    }

    public void setCookTimeLevel(int cookTimeLevel) {
        this.cookTimeLevel = cookTimeLevel;
    }

    public int getGenerateAmountLevel() {
        return generateAmountLevel;
    }

    public void setGenerateAmountLevel(int generateAmountLevel) {
        this.generateAmountLevel = generateAmountLevel;
    }

    public Location getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(Location placeLocation) {
        this.placeLocation = placeLocation;
    }

    public List<ArmorStand> getHolograms() {
        return holograms;
    }

    public void setHolograms(List<ArmorStand> holograms) {
        this.holograms = holograms;
    }

    public Map<String, Object> toMap() {
        return new HashMap<>(){{
            put("author", author);
            put("type", type);
            put("cookTimeLevel", cookTimeLevel);
            put("generateAmountLevel", generateAmountLevel);
            put("placeLocation", placeLocation);
        }};
    }
}
