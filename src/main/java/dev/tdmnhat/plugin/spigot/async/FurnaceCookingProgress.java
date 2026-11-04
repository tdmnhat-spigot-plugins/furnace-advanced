package dev.tdmnhat.plugin.spigot.async;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import org.bukkit.ChatColor;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class FurnaceCookingProgress {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
    private final FurnaceAdvanced plugin;

    public FurnaceCookingProgress(FurnaceAdvanced plugin) {
        this.plugin = plugin;
    }

    public void addTask(Furnace furnace, FurnaceInformation furnaceInformation) {
        String getKey = furnaceInformation.getKey();
        if(!tasks.containsKey(getKey)) {
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack getSmeltingItem = furnace.getInventory().getSmelting();
                    if (getSmeltingItem != null && getSmeltingItem.getAmount() > 0) {
                        updateHologramFurnace(furnace, furnaceInformation);
                    } else {
                        stopTask(getKey);
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
            tasks.put(getKey, task);
        }
    }

    private void stopTask(String key) {
        BukkitTask task = tasks.remove(key);
        if(task != null) {
            task.cancel();
        }
    }

    private void updateHologramFurnace(Furnace furnace, FurnaceInformation furnaceInformation) {
        ArmorStand armorStand = furnaceInformation.getHolograms().get(3);
        ItemStack getSmeltingItem = furnace.getInventory().getSmelting();
        ItemStack getResultItem = furnace.getInventory().getResult();

        int smeltingItemAmount = getSmeltingItem == null ? 0 : getSmeltingItem.getAmount();
        int resultItemAmount = getResultItem == null ? 0 : getResultItem.getAmount();
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("holograms.4th-line")
                .replace("%furnace_smelting_amount%", String.valueOf(smeltingItemAmount))
                        .replace("%furnace_resulting_amount%", String.valueOf(resultItemAmount))));
    }
}
