package dev.tdmnhat.plugin.spigot.util;

import org.bukkit.Bukkit;

public class ServerVersion {

    private static final int MAJOR;
    private static final int MINOR;
    private static final int PATCH;

    static {
        String version = Bukkit.getBukkitVersion().split("-")[0];
        String[] parts = version.split("\\.");


        MAJOR = Integer.parseInt(parts[0]);
        MINOR = Integer.parseInt(parts[1]);
        if(parts.length > 2) {
            PATCH = Integer.parseInt(parts[2]);
        } else {
            PATCH = 0;
        }

    }

    public static int getMajor() { return MAJOR; }
    public static int getMinor() { return MINOR; }
    public static int getPatch() { return PATCH; }
}
