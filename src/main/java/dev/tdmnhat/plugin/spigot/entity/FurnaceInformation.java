package dev.tdmnhat.plugin.spigot.entity;

import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FurnaceInformation {

    private String key; // "worldUUID;x;y;z"

    private String worldName;

    private String worldUUID;

    private int x;

    private int y;

    private int z;

    private int cookTimeLevel;

    private int generateAmountLevel;

    private String authorName;

    private String authorUUID;

    private List<ArmorStand> holograms;

    public FurnaceInformation() {
    }

    public FurnaceInformation(String worldName, String worldUUID, int x, int y, int z, int cookTimeLevel, int generateAmountLevel, String authorName, String authorUUID, List<ArmorStand> holograms) {
        this.key = String.format("%s;%d;%d;%d", worldUUID, x, y, z);
        this.worldName = worldName;
        this.worldUUID = worldUUID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.cookTimeLevel = cookTimeLevel;
        this.generateAmountLevel = generateAmountLevel;
        this.authorName = authorName;
        this.authorUUID = authorUUID;
        this.holograms = holograms;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getWorldUUID() {
        return worldUUID;
    }

    public void setWorldUUID(String worldUUID) {
        this.worldUUID = worldUUID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUUID() {
        return authorUUID;
    }

    public void setAuthorUUID(String authorUUID) {
        this.authorUUID = authorUUID;
    }

    public List<ArmorStand> getHolograms() {
        return holograms;
    }

    public void setHolograms(List<ArmorStand> holograms) {
        this.holograms = holograms;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("worldUUID", worldUUID);
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        map.put("cookTimeLevel", cookTimeLevel);
        map.put("generateAmountLevel", generateAmountLevel);
        map.put("authorUUID", authorUUID);
        return map;
    }
}
