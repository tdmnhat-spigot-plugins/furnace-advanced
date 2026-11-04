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

    private final FurnaceAdvanced plugin;
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
        this.plugin = plugin;
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
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("furnace-upgrade-menu.title"));
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
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfig().getString("plugin-prefix") + " " + plugin.getConfig().getString("furnace-already-max-level")
                                ));
                    } else if (economy.getBalance(player) >= getListGenerateLevel.get(currentGenerateAmountLevel).getCost()) {
                        economy.withdrawPlayer(player, getListGenerateLevel.get(currentGenerateAmountLevel).getCost());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfig().getString("prefix-plugin") + " " + plugin.getConfig().getString("upgrade-successfully")));

                        FurnaceInformation furnaceInformation = currentFurnaceInformation;
                        furnaceInformation.setGenerateAmountLevel(currentGenerateAmountLevel + 1);
                        furnaceInformation.getHolograms().get(2).setCustomName(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfig().getString("holograms.3rd-line").replace("%furnace_generate_level%", String.valueOf((currentGenerateAmountLevel + 1)))
                                ));
                        furnaceManagement.getList().set(furnaceManagement.getList().indexOf(currentFurnaceInformation), furnaceInformation);

                        this.currentFurnaceInformation = furnaceInformation;
                        updateFurnaceLevel();
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfig().getString("prefix-plugin") + " " + plugin.getConfig().getString("not-enough-money-upgrade")
                                ));
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
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfig().getString("prefix-plugin")
                                        + " " + plugin.getConfig().getString("furnace-already-max-level")));
                    } else if (economy.getBalance(player) >= getListCookLevel.get(currentCookTimeLevel).getCost()) {
                        economy.withdrawPlayer(player, getListCookLevel.get(currentCookTimeLevel).getCost());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfig().getString("prefix-plugin")
                                        + " " + plugin.getConfig().getString("upgrade-successfully")));

                        FurnaceInformation furnaceInformation = currentFurnaceInformation;
                        furnaceInformation.setCookTimeLevel(currentCookTimeLevel + 1);
                        furnaceInformation.getHolograms().get(1).setCustomName(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfig().getString("holograms.2nd-line").replace("%furnace_cook_level%", String.valueOf((currentCookTimeLevel + 1)))));
                        furnaceManagement.getList().set(furnaceManagement.getList().indexOf(currentFurnaceInformation), furnaceInformation);

                        this.currentFurnaceInformation = furnaceInformation;
                        updateFurnaceLevel();
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix-plugin") + " "
                        + plugin.getConfig().getString("not-enough-money-upgrade")));
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
                ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("furnace-upgrade-menu.generate-amount-item.name")),
                new ArrayList<>() {{
                    addAll(plugin.getConfig().getStringList("furnace-upgrade-menu.generate-amount-item.description").stream().map(item -> ChatColor.translateAlternateColorCodes('&', item)).toList());
                    addAll(getListGenerateLevel.stream().map(item -> ChatColor.translateAlternateColorCodes('&', (item.getMin() == item.getMax() ? plugin.getConfig().getString("furnace-upgrade-menu.generate-amount-item.level-info.generate-exactly").replace("%furnace_generate_level%", String.valueOf(item.getLevel())).replace("%furnace_generate_amount%", String.valueOf(item.getMin()))
                            : plugin.getConfig().getString("furnace-upgrade-menu.generate-amount-item.level-info.generate-range").replace("%furnace_generate_level%", String.valueOf(item.getLevel()))
                            .replace("%furnace_generate_amount_min%", String.valueOf(item.getMin()))
                            .replace("%furnace_generate_amount_max%", String.valueOf(item.getMax())))
                            + " " + (currentGenerateAmountLevel == item.getLevel() ? plugin.getConfig().getString("furnace-upgrade-menu.current-level") : ""))).toList());
                    add(ChatColor.translateAlternateColorCodes('&',
                            getListGenerateLevel.getLast().getLevel() == currentGenerateAmountLevel ?
                                    plugin.getConfig().getString("furnace-upgrade-menu.generate-amount-item.cost-info.already-max") :
                                    plugin.getConfig().getString("furnace-upgrade-menu.generate-amount-item.cost-info.info")
                                            .replace("%furnace_generate_cost%", String.valueOf(getListGenerateLevel.get(currentGenerateAmountLevel).getCost()))
                                            .replace("%furnace_generate_next_level%", String.valueOf(getListGenerateLevel.get(currentGenerateAmountLevel).getLevel()))));
                }}
        ));

        this.inventory.setItem(13, makeItem(
                Material.FURNACE,
                1,
                ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("furnace-upgrade-menu.open-furnace-inventory.name")),
                new ArrayList<>() {{
                    addAll(plugin.getConfig().getStringList("furnace-upgrade-menu.open-furnace-inventory.description").stream().map(item -> ChatColor.translateAlternateColorCodes('&', item)).toList());
                }}
        ));

        this.inventory.setItem(15, makeItem(
                (ServerVersion.getMajor() >= 26 || (ServerVersion.getMajor() == 1 && ServerVersion.getMinor() >= 13)) ? Material.CLOCK : Material.COMPASS,
                currentCookTimeLevel,
                ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("furnace-upgrade-menu.cook-time-item.name")),
                new ArrayList<>() {{
                    addAll(plugin.getConfig().getStringList("furnace-upgrade-menu.cook-time-item.description").stream().map(item -> ChatColor.translateAlternateColorCodes('&', item)).toList());
                    addAll(getListCookLevel.stream().map(item -> ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString("furnace-upgrade-menu.cook-time-item.level-info").replace("%furnace_cook_level%", String.valueOf(item.getLevel())).replace("%furnace_cook_time%", String.valueOf(item.getCookTime()))
                                    + " "
                                    + (currentCookTimeLevel == item.getLevel() ? plugin.getConfig().getString("furnace-upgrade-menu.current-level") : ""))).toList());
                    add(ChatColor.translateAlternateColorCodes('&', getListCookLevel.getLast().getLevel() == currentCookTimeLevel ?
                            plugin.getConfig().getString("furnace-upgrade-menu.cook-time-item.cost-info.already-max") : plugin.getConfig().getString("furnace-upgrade-menu.cook-time-item.cost-info.info")
                                    .replace("%furnace_cook_cost%", String.valueOf(getListCookLevel.get(currentCookTimeLevel).getCost())).replace("%furnace_cook_next_level%", String.valueOf(getListCookLevel.get(currentCookTimeLevel).getLevel()))));
                }}
        ));

        this.inventory.setItem(22, makeItem(
                Material.REDSTONE_BLOCK,
                1,
                ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("furnace-upgrade-menu.close.name"))
        ));
    }
}
