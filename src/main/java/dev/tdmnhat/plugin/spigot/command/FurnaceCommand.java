package dev.tdmnhat.plugin.spigot.command;

import dev.tdmnhat.plugin.spigot.FurnaceAdvanced;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FurnaceCommand implements CommandExecutor {

    private FurnaceAdvanced plugin;

    public FurnaceCommand(FurnaceAdvanced plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(args.length == 0) {
                showListCommands(player);
            } else if(args.length == 1) {
                switch(args[0]) {
                    case "help": {
                        showListCommands(player);
                        break;
                    }
                    default: {
                        player.sendMessage(ChatColor.RED + "Invalid command");
                        break;
                    }
                }
            }

            return true;
        } else {
            sender.sendMessage("You must be a player to execute this command");
            return false;
        }
    }

    private void showListCommands(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("plugin-prefix") + " Furnace Advanced Help"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "A plugin has been made by @skyherobrine"));
        plugin.getConfig().getStringList("overview-menu.list-command.lore").forEach(message -> {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        });
    }
}
