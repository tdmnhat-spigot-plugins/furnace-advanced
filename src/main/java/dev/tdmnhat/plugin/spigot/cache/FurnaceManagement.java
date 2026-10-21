package dev.tdmnhat.plugin.spigot.cache;

import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;

import java.util.ArrayList;
import java.util.List;

public class FurnaceManagement {

    private static FurnaceManagement instance;
    private List<FurnaceInfo> furnaceInfos;

    private FurnaceManagement() {
        furnaceInfos = new ArrayList<>();
    }

    public static FurnaceManagement getInstance() {
        if(instance == null) {
            instance = new FurnaceManagement();
        }
        return instance;
    }

    public List<FurnaceInfo> getFurnaceInfos() {
        return furnaceInfos;
    }
}
