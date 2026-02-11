package ru.fozeton.training.Task2.Event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.fozeton.training.Level2;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static ru.fozeton.training.Task2.Constants.FriendMobs;

public class PlayerInteract implements Listener {
    public static final Set<UUID> ignorDamage = new HashSet<>();

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        Block clickBlock = event.getClickedBlock();
        World world = playerLoc.getWorld();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickBlock != null && clickBlock.getType().toString().toLowerCase().contains("leaves")) {
            Entity zombie = world.spawnEntity(playerLoc, EntityType.ZOMBIE);
            FriendMobs.put(zombie.getUniqueId(), player.getUniqueId());
        }
    }

    @EventHandler
    public void onFeatherClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        if (item != null && (action.isRightClick() || action.isLeftClick()) && item.getType() == Material.FEATHER) {
            player.setVelocity(new Vector(0, 2, 0));
            ignorDamage.add(player.getUniqueId());

            World world = player.getWorld();
            Location loc = player.getLocation();

            int radius = 5 / 2;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = world.getBlockAt(loc.getBlockX() + x, loc.blockY(), loc.blockZ() + z);
                    block.setType(Material.SLIME_BLOCK);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            block.setType(Material.AIR);
                        }
                    }.runTaskLater(Level2.getInstance(), 5L * 20);
                }
            }
        }
    }
}
