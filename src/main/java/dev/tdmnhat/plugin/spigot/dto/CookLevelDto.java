package dev.tdmnhat.plugin.spigot.dto;

public class CookLevelDto {
    private int level;
    private double cost;
    private int cookTime;

    public CookLevelDto(int level, double cost, int cookTime) {
        setLevel(level);
        setCost(cost);
        setCookTime(cookTime);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }
}
