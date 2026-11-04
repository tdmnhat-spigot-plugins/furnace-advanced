package dev.tdmnhat.plugin.spigot.async;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.storage.FurnaceStorage;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.List;

public class FileAsyncAction {

    public static void startAutoSaveFurnaces(FurnaceAdvanced plugin, FurnaceStorage furnaceStorage, FurnaceManagement furnaceManagement) {
        long period = 20L * 60 * 3;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            List<FurnaceInformation> snapshot = furnaceManagement.getList();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    furnaceStorage.save(snapshot);
                } catch (IOException e) {
                    plugin.getLogger().severe("Autosave failed: " + e.getMessage());
                }
            });
        }, period, period);
    }
}
