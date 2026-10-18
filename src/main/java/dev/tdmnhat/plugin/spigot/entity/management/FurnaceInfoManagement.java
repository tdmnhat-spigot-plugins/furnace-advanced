package dev.tdmnhat.plugin.spigot.entity.management;

import dev.tdmnhat.plugin.spigot.entity.FurnaceInfo;

import java.util.ArrayList;
import java.util.List;

public class FurnaceInfoManagement {

    private static FurnaceInfoManagement instance;
    private static List<FurnaceInfo> furnacePlacedList = new ArrayList<>();
    private static List<FurnaceInfo> furnaceInventoryList = new ArrayList<>();

    private FurnaceInfoManagement() {}

    public static FurnaceInfoManagement getInstance() {
        if(instance == null) {
            instance = new FurnaceInfoManagement();
        }
        return instance;
    }

    public List<FurnaceInfo> getFurnacePlacedList() {
        return furnacePlacedList;
    }
    public List<FurnaceInfo> getFurnaceInventoryList() {
        return furnaceInventoryList;
    }

    public boolean removeFurnaceInfo(FurnaceInfo furnaceInfo) {
        return furnacePlacedList.remove(furnaceInfo);
    }
}
