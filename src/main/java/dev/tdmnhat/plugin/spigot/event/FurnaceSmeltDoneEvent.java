package dev.tdmnhat.plugin.spigot.event;

import org.bukkit.block.Furnace;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FurnaceSmeltDoneEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private Furnace furnace;

    public FurnaceSmeltDoneEvent(Furnace furnace) {
        super(false);
        this.furnace = furnace;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Furnace getFurnace() {
        return furnace;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
