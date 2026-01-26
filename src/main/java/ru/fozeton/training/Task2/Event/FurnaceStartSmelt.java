package ru.fozeton.training.Task2.Event;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;

public class FurnaceStartSmelt implements Listener {
    @EventHandler
    public void onFurnaceStartSmale(FurnaceStartSmeltEvent event) {
        if(event.getBlock().getState() instanceof Furnace){
            event.setTotalCookTime(100);
        }
    }
}
