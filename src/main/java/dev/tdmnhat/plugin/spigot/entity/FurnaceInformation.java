package dev.tdmnhat.plugin.spigot.entity;

public class FurnaceInformation {

    private String key; // "worldUUID;x;y;z"

    private String worldUUID;

    private int x;

    private int y;

    private int z;

    private int cookTimeLevel;

    private int generateAmountLevel;

    private String authorUUID;

    public FurnaceInformation() {
    }

    public FurnaceInformation(String worldUUID, int x, int y, int z, int cookTimeLevel, int generateAmountLevel, String authorUUID) {
        this.key = String.format("%s;%d;%d;%d", worldUUID, x, y, z);
        this.worldUUID = worldUUID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.cookTimeLevel = cookTimeLevel;
        this.generateAmountLevel = generateAmountLevel;
        this.authorUUID = authorUUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getAuthorUUID() {
        return authorUUID;
    }

    public void setAuthorUUID(String authorUUID) {
        this.authorUUID = authorUUID;
    }
}
