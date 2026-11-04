package dev.tdmnhat.plugin.spigot.util;

import org.bukkit.Bukkit;

public class ServerVersion {

    private static final int MAJOR;
    private static final int MINOR;

    static {
        String version = Bukkit.getBukkitVersion().split("-")[0];
        String[] parts = version.split("\\.");


        MAJOR = Integer.parseInt(parts[0]);
        MINOR = Integer.parseInt(parts[1]);
    }

    public static int getMajor() { return MAJOR; }
    public static int getMinor() { return MINOR; }
}
