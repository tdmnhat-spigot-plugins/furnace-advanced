package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.event.FurnaceSmeltDoneEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class FurnaceCookListener implements Listener {

    private FurnaceAdvanced plugin;

    public FurnaceCookListener(FurnaceAdvanced plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        short cookTime = 100;
        Furnace furnace = (Furnace) event.getBlock().getState();
        furnace.setCookTime(cookTime);
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
