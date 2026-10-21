package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import dev.tdmnhat.plugin.spigot.event.FurnaceSmeltDoneEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;

public class FurnaceCookListener implements Listener {

    private FileConfiguration configuration;
    private FurnaceAdvanced plugin;

    public FurnaceCookListener() {
        plugin = (FurnaceAdvanced) Bukkit.getPluginManager().getPlugin("FurnaceAdvanced");
        configuration = plugin.getConfig();
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        short cookTime = 100;
        Furnace furnace = (Furnace) event.getBlock().getState();
        furnace.setCookTime(cookTime);

        FurnaceInfo furnaceInfo = FurnaceManagement.getInstance().getFurnaceInfos().stream().filter(item -> item.getPlaceLocation().equals(furnace.getLocation())).findFirst().orElse(null);
        if(furnaceInfo != null) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, (task) -> {
                System.out.println("Run the async task");
                ArmorStand armorStand = furnaceInfo.getHolograms().get(3);
                ItemStack getSmeltingItem = furnace.getInventory().getSmelting();
                ItemStack getResultItem = furnace.getInventory().getResult();

                if(getSmeltingItem == null || getResultItem == null) {
                    task.cancel();
                } else if(getSmeltingItem.getAmount() == 0) {
                    task.cancel();
                }

                int smeltingItemAmount = getSmeltingItem == null ? 0 : getSmeltingItem.getAmount();
                int resultItemAmount = getResultItem == null ? 0 : getResultItem.getAmount();
                armorStand.setCustomName(smeltingItemAmount + " | " + resultItemAmount);
            }, 0L, 50L);
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(new FurnaceSmeltDoneEvent((Furnace) event.getBlock().getState()));
    }

    @EventHandler
    public void onFurnaceSmeltDone(FurnaceSmeltDoneEvent event) {
        Furnace furnace = event.getFurnace();

        short cookTime = 100;
        furnace.setCookTime(cookTime);
    }
}
