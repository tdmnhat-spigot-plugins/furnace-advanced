package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.async.FurnaceCookingProgress;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.dto.CookLevelDto;
import dev.tdmnhat.plugin.spigot.dto.GenerateLevelDto;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.event.FurnaceSmeltDoneEvent;
import dev.tdmnhat.plugin.spigot.util.file.GettingDataConfig;
import org.bukkit.Bukkit;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FurnaceCookListener implements Listener {

    private final FurnaceManagement furnaceManagement;
    private final FurnaceAdvanced plugin;

    public FurnaceCookListener(FurnaceAdvanced plugin, FurnaceManagement furnaceManagement) {
        this.plugin = plugin;
        this.furnaceManagement = furnaceManagement;
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(furnace.getLocation());
        if(furnaceInformation == null) {
            return;
        }
        updateCookTime(furnace, furnaceInformation);

        new FurnaceCookingProgress(plugin).addTask(furnace, furnaceInformation);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(furnace.getLocation());

        if(furnaceInformation == null) {
            return;
        }

        List<GenerateLevelDto> generateLevelDtos = new GettingDataConfig(plugin).getListGenerateLevel();
        GenerateLevelDto generateLevelDto = generateLevelDtos
                .stream()
                .filter(item -> item.getLevel() == furnaceInformation.getGenerateAmountLevel())
                .findFirst()
                .orElse(null);

        if (generateLevelDto != null) {
            if (generateLevelDto.getMin() == generateLevelDto.getMax()) {
                event.getResult().setAmount(generateLevelDto.getMin());
            } else {
                event.getResult().setAmount(new Random().nextInt(generateLevelDto.getMin(), generateLevelDto.getMax() + 1));
            }
        }

        Bukkit.getServer().getPluginManager().callEvent(new FurnaceSmeltDoneEvent((Furnace) event.getBlock().getState()));
    }

    @EventHandler
    public void onFurnaceSmeltDone(FurnaceSmeltDoneEvent event) {
        Furnace furnace = event.getFurnace();
        FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(furnace.getLocation());
        if(furnaceInformation == null) {
            return;
        }

        updateCookTime(furnace, furnaceInformation);
        new FurnaceCookingProgress(plugin).addTask(furnace, furnaceInformation);
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        if(furnace.getCookTime() == 0) {
            FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(furnace.getLocation());
            updateCookTime(furnace, furnaceInformation);
            new FurnaceCookingProgress(plugin).addTask(furnace, furnaceInformation);
        }
    }

    private void updateCookTime(Furnace furnace, FurnaceInformation furnaceInformation) {
        List<CookLevelDto> cookLevelDtos = new GettingDataConfig(plugin).getListCookTime();
        short cookTime = Short.parseShort(200 - (cookLevelDtos.stream()
                .filter(item -> item.getLevel() == furnaceInformation.getCookTimeLevel())
                .findFirst()
                .orElse(cookLevelDtos.getFirst())
                .getCookTime() * 20) + "");
        furnace.setCookTime(cookTime);
    }
}
