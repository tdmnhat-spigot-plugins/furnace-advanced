package dev.tdmnhat.plugin.spigot.dto;

public class GenerateLevelDto {

    private int level;
    private double cost;
    private int min;
    private int max;

    public GenerateLevelDto(int level, double cost, int min, int max) {
        this.level = level;
        this.cost = cost;
        this.min = min;
        setMax(max);
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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max < 0 || max < min) {
            this.max = min;
        } else {
            this.max = max;
        }
    }
}
