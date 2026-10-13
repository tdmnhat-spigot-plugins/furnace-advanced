package dev.tdmnhat.plugin.spigot.enums;

public enum FUEL_BURN_TIME {
    WOOD(10), COAL(20), BLOCK_COAL(50), LAVA(100);

    private int time;

    FUEL_BURN_TIME(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
