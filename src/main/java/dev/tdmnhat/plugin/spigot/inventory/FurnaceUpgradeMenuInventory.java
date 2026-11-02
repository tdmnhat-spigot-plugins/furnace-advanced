package dev.tdmnhat.plugin.spigot.inventory;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.dto.CookLevelDto;
import dev.tdmnhat.plugin.spigot.dto.GenerateLevelDto;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInformation;
import dev.tdmnhat.plugin.spigot.setup.VaultSetup;
import dev.tdmnhat.plugin.spigot.util.ServerVersion;
import dev.tdmnhat.plugin.spigot.util.file.GettingDataConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class FurnaceUpgradeMenuInventory extends Menu {

    private final Furnace furnace;
    private int currentGenerateAmountLevel;
    private int currentCookTimeLevel;
    private final List<GenerateLevelDto> getListGenerateLevel;
    private final List<CookLevelDto> getListCookLevel;
    private final Economy economy;
    private final FurnaceManagement furnaceManagement;
    private FurnaceInformation currentFurnaceInformation;

    public FurnaceUpgradeMenuInventory(FurnaceAdvanced plugin, PlayerMenuUtility playerMenuUtility, Furnace target, FurnaceManagement furnaceManagement) {
        super(playerMenuUtility);
        this.furnace = target;
        this.furnaceManagement = furnaceManagement;
        this.economy = VaultSetup.getEconomy();

        GettingDataConfig gettingDataConfig = new GettingDataConfig(plugin);
        this.getListGenerateLevel = gettingDataConfig.getListGenerateLevel();
        this.getListCookLevel = gettingDataConfig.getListCookTime();

        this.currentFurnaceInformation = furnaceManagement.getFurnaceInformation(furnace.getLocation());
        this.updateFurnaceLevel();
    }

    private void updateFurnaceLevel() {
        this.currentGenerateAmountLevel = currentFurnaceInformation.getGenerateAmountLevel();
        this.currentCookTimeLevel = currentFurnaceInformation.getCookTimeLevel();
    }

    @Override
    public String getMenuName() {
        return "Furnace Upgrade Menu";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getWhoClicked() instanceof Player player) {

            int getSlotClicked = event.getRawSlot();
            if (getSlotClicked < 0 && getSlotClicked >= event.getClickedInventory().getSize()) return;

            switch (getSlotClicked) {
                case 11: {
                    if (getListGenerateLevel.getLast().getLevel() == currentGenerateAmountLevel) {
                        player.sendMessage("[Furnace Advanced] You have upgraded this to maximum level");
                    } else if (economy.getBalance(player) >= getListGenerateLevel.get(currentGenerateAmountLevel).getCost()) {
                        economy.withdrawPlayer(player, getListGenerateLevel.get(currentGenerateAmountLevel).getCost());
                        player.sendMessage("[Furnace Advanced] Upgrade generate amount successfully");

                        FurnaceInformation furnaceInformation = currentFurnaceInformation;
                        furnaceInformation.setGenerateAmountLevel(currentGenerateAmountLevel + 1);
                        furnaceInformation.getHolograms().get(2).setCustomName("Generate Level: " + (currentGenerateAmountLevel + 1));
                        furnaceManagement.getList().set(furnaceManagement.getList().indexOf(currentFurnaceInformation), furnaceInformation);

                        this.currentFurnaceInformation = furnaceInformation;
                        updateFurnaceLevel();
                    } else {
                        player.sendMessage("[Furnace Advanced] You don't have enough money to upgrade this");
                    }
                    player.closeInventory();
                    this.open();
                    break;
                }
                case 13: {
                    player.openInventory(this.furnace.getInventory());
                    break;
                }
                case 15: {
                    if (getListCookLevel.getLast().getLevel() == currentCookTimeLevel) {
                        player.sendMessage("[Furnace Advanced] You have already upgraded this to maximum level");
                    } else if (economy.getBalance(player) >= getListCookLevel.get(currentCookTimeLevel).getCost()) {
                        economy.withdrawPlayer(player, getListCookLevel.get(currentCookTimeLevel).getCost());
                        player.sendMessage("[Furnace Advanced] Upgrade generate amount successfully");

                        FurnaceInformation furnaceInformation = currentFurnaceInformation;
                        furnaceInformation.setCookTimeLevel(currentCookTimeLevel + 1);
                        furnaceInformation.getHolograms().get(1).setCustomName("Cook Level: " + (currentCookTimeLevel + 1));
                        furnaceManagement.getList().set(furnaceManagement.getList().indexOf(currentFurnaceInformation), furnaceInformation);

                        this.currentFurnaceInformation = furnaceInformation;
                        updateFurnaceLevel();
                    } else {
                        player.sendMessage("[Furnace Advanced] You don't have enough money to upgrade this");
                    }
                    player.closeInventory();
                    this.open();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        this.inventory.setItem(11, makeItem(
                Material.DIAMOND,
                currentGenerateAmountLevel,
                ChatColor.AQUA + "Generate Amount Level",
                new ArrayList<>() {{
                    add("Increasing a number of item when smelting done");
                    addAll(getListGenerateLevel.stream().map(item -> (item.getMin() == item.getMax() ? String.format("Level %d: Exactly %d amounts", item.getLevel(), item.getMin()) : String.format("Level %d: Range %d to %d amounts", item.getLevel(), item.getMin(), item.getMax())) + " " + (currentGenerateAmountLevel == item.getLevel() ? "(You)" : "")).toList());
                    add(getListGenerateLevel.getLast().getLevel() == currentGenerateAmountLevel ? "You have upgraded this type to max level" : String.format("Cost: %.2f. To upgrade the level %d", getListGenerateLevel.get(currentGenerateAmountLevel).getCost(), getListGenerateLevel.get(currentGenerateAmountLevel).getLevel()));
                }}
        ));

        this.inventory.setItem(13, makeItem(
                Material.FURNACE,
                1,
                ChatColor.GREEN + "Open the main furnace inventory",
                new ArrayList<>() {{
                    add(ChatColor.WHITE + "This main inventory for adding ingredient for smelting");
                    add(ChatColor.YELLOW + "The progress will be updated continuously in top this furnace");
                }}
        ));

        this.inventory.setItem(15, makeItem(
                (ServerVersion.getMajor() >= 26 || (ServerVersion.getMajor() == 1 && ServerVersion.getMinor() >= 13)) ? Material.CLOCK : Material.COMPASS,
                currentCookTimeLevel,
                ChatColor.YELLOW + "Upgrade the cooking time",
                new ArrayList<>() {{
                    add("Decreasing the time cook the ingredient");
                    addAll(getListCookLevel.stream().map(item -> String.format("Level %d: %d seconds", item.getLevel(), item.getCookTime()) + " " + (currentCookTimeLevel == item.getLevel() ? "(You)" : "")).toList());
                    add(String.format(getListCookLevel.getLast().getLevel() == currentCookTimeLevel ? "You have upgraded this type to max level" : String.format("Cost: %.2f. For upgrading to the level %d", getListCookLevel.get(currentCookTimeLevel).getCost(), getListCookLevel.get(currentCookTimeLevel).getLevel())));
                }}
        ));

        this.inventory.setItem(22, makeItem(
                Material.REDSTONE_BLOCK,
                1,
                ChatColor.RED + "Close Menu"
        ));
    }
}
