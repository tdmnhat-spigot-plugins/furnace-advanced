package dev.tdmnhat.plugin.spigot.cache;

import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FurnaceManagement {

    private static FurnaceManagement instance;
    private final Map<Player, List<FurnaceInfo>> furnaces;

    private FurnaceManagement() {
        furnaces = new HashMap<>();
    }

    public static FurnaceManagement getInstance() {
        if(instance == null) {
            instance = new FurnaceManagement();
        }
        return instance;
    }

    public Map<Player, List<FurnaceInfo>> getFurnaces() {
        return furnaces;
    }

    public List<FurnaceInfo> getListFurnaces(Player player) {
        furnaces.computeIfAbsent(player, k -> new ArrayList<>());
        return furnaces.get(player);
    }

    public void addFurnace(Player player, FurnaceInfo furnaceInfo) {
        if (furnaces.get(player) == null) {
            furnaces.put(player, new ArrayList<>() {{
                add(furnaceInfo);
            }});
        } else {
            furnaces.get(player).add(furnaceInfo);
        }
    }

    public void addFurnace(Player player, List<FurnaceInfo> furnaceInfos) {
        if (furnaces.get(player) == null) {
            furnaces.put(player, new ArrayList<>(furnaceInfos));
        } else {
            furnaces.get(player).addAll(furnaceInfos);
        }
    }

    public void deleteKey(Player player) {
        furnaces.remove(player);
    }
}
