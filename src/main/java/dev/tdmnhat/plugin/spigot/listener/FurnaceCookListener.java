package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.dto.CookLevelDto;
import dev.tdmnhat.plugin.spigot.dto.GenerateLevelDto;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.event.FurnaceSmeltDoneEvent;
import dev.tdmnhat.plugin.spigot.util.file.GettingDataConfig;
import org.bukkit.Bukkit;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.FurnaceInventory;

import java.util.List;
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

        List<CookLevelDto> cookLevelDtos = new GettingDataConfig(plugin).getListCookTime();
        short cookTime = Short.parseShort(200 - (cookLevelDtos.stream()
                .filter(item -> item.getLevel() == furnaceInformation.getCookTimeLevel())
                .findFirst()
                .orElse(cookLevelDtos.getFirst())
                .getCookTime() * 20) + "");
        furnace.setCookTime(cookTime);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(new FurnaceSmeltDoneEvent((Furnace) event.getBlock().getState()));

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
    }

    @EventHandler
    public void onFurnaceSmeltDone(FurnaceSmeltDoneEvent event) {
        Furnace furnace = event.getFurnace();
        FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(furnace.getLocation());

        if(furnaceInformation == null) {
            return;
        }

        List<CookLevelDto> cookLevelDtos = new GettingDataConfig(plugin).getListCookTime();
        short cookTime = Short.parseShort(200 - (cookLevelDtos.stream()
                .filter(item -> item.getLevel() == furnaceInformation.getCookTimeLevel())
                .findFirst()
                .orElse(cookLevelDtos.getFirst())
                .getCookTime() * 20) + "");
        furnace.setCookTime(cookTime);
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        Furnace furnace = (Furnace) event.getBlock().getState();
        if(furnace.getCookTime() == 0) {
            FurnaceInformation furnaceInformation = furnaceManagement.getFurnaceInformation(furnace.getLocation());
            List<CookLevelDto> cookLevelDtos = new GettingDataConfig(plugin).getListCookTime();
            short cookTime = Short.parseShort(200 - (cookLevelDtos.stream()
                    .filter(item -> item.getLevel() == furnaceInformation.getCookTimeLevel())
                    .findFirst()
                    .orElse(cookLevelDtos.getFirst())
                    .getCookTime() * 20) + "");
            furnace.setCookTime(cookTime);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof FurnaceInventory && event.getPlayer() instanceof Player) {
            // Update hologram
        }
    }

//    private void updateHologramFurnace(Furnace furnace, FurnaceInfo furnaceInfo) {
//        ArmorStand armorStand = furnaceInfo.getHolograms().get(3);
//        ItemStack getSmeltingItem = furnace.getInventory().getSmelting();
//        ItemStack getResultItem = furnace.getInventory().getResult();
//
//        int smeltingItemAmount = getSmeltingItem == null ? 0 : getSmeltingItem.getAmount();
//        int resultItemAmount = getResultItem == null ? 0 : getResultItem.getAmount();
//        armorStand.setCustomName(smeltingItemAmount + " ➜ " + resultItemAmount);
//    }
}
