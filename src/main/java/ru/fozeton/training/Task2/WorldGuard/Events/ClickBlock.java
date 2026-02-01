package ru.fozeton.training.Task2.WorldGuard.Events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClickBlock implements Listener {

    private static final Map<UUID, Location> pos1 = new HashMap<>();
    private static final Map<UUID, Location> pos2 = new HashMap<>();

    @EventHandler
    public void onClickBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();

        if (item != null && clickedBlock != null && item.getType() == Material.WOODEN_AXE) {
            switch (action) {
                case LEFT_CLICK_BLOCK:
                    pos1.put(player.getUniqueId(), clickedBlock.getLocation());
                    player.sendMessage("§aПозиция 1 установленна: " + formatLocation(clickedBlock.getLocation()));
                    event.setCancelled(true);
                    break;
                case RIGHT_CLICK_BLOCK:
                    pos2.put(player.getUniqueId(), clickedBlock.getLocation());
                    player.sendMessage("§aПозиция 2 установленна: " + formatLocation(clickedBlock.getLocation()));
                    event.setCancelled(true);
                    break;
            }
        }
    }

    private String formatLocation(Location loc) {
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.blockZ();
    }

    public static Location getPos1(UUID uuid) {
        return pos1.get(uuid);
    }

    public static Location getPos2(UUID uuid) {
        return pos2.get(uuid);
    }
}
