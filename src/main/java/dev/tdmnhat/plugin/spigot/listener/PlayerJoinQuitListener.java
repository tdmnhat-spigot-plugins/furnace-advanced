package dev.tdmnhat.plugin.spigot.listener;

import dev.tdmnhat.plugin.spigot.cache.FurnaceManagement;
import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import dev.tdmnhat.plugin.spigot.file.PlayerDataFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerDataFile.setup(player.getUniqueId().toString());
        List<FurnaceInfo> furnaceInfos = (List<FurnaceInfo>) PlayerDataFile.getConfiguration().getList("data");
        if(furnaceInfos != null && !furnaceInfos.isEmpty()) {
            FurnaceManagement.getInstance().addFurnace(player, furnaceInfos);
        } else {
            FurnaceManagement.getInstance().addFurnace(player, new ArrayList<>());
            PlayerDataFile.getConfiguration().set("data", new ArrayList<>());
            PlayerDataFile.save();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        List<FurnaceInfo> furnaceInfos = FurnaceManagement.getInstance().getListFurnaces(player);
        if(furnaceInfos != null && !furnaceInfos.isEmpty()) {
            PlayerDataFile.getConfiguration().set("data", furnaceInfos);
            PlayerDataFile.save();
            FurnaceManagement.getInstance().deleteKey(player);
        }
    }
}
