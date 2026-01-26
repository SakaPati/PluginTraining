package ru.fozeton.training.Task2.Event;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static ru.fozeton.training.Task2.Constants.FriendMobs;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onLeafClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        Block clickBlock = event.getClickedBlock();
        World world = playerLoc.getWorld();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickBlock != null && clickBlock.getType().toString().toLowerCase().contains("leaves")) {
            Entity zombie = world.spawnEntity(playerLoc, EntityType.ZOMBIE);
            FriendMobs.put(zombie.getUniqueId(), player.getUniqueId());
        }
    }
}
