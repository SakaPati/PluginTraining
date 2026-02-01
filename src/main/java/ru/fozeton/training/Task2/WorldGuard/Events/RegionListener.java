package ru.fozeton.training.Task2.WorldGuard.Events;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.fozeton.training.Task2.WorldGuard.RegionManager;

public class RegionListener implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation();

        if (RegionManager.checkRegion(blockLocation, player) && !RegionManager.isParty(blockLocation, player)) {
            player.sendMessage("§cВы не можете взаимодействовать с этим блоком");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlacedBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation();

        if (RegionManager.checkRegion(blockLocation, player) && !RegionManager.isParty(blockLocation, player)) {
            player.sendMessage("§cВы не можете взаимодействовать с этим блоком");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractionBlock(PlayerInteractEvent event) {
        if (event.getAction().isRightClick() && event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            Location blockLocation = event.getClickedBlock().getLocation();

            if (RegionManager.checkRegion(blockLocation, player) && !RegionManager.isParty(blockLocation, player)) {
                player.sendMessage("§cВы не можете взаимодействовать с этим блоком");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerAttack(PrePlayerAttackEntityEvent event) {
        if (!(event.getAttacked() instanceof Player defender)) return;
        Player attacker = event.getPlayer();
        if (RegionManager.checkPvP(defender.getLocation(), defender, attacker.getLocation(), attacker)) {
            attacker.sendMessage("§cВы не можете атаковать игрока");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (RegionManager.checkRegion(to, player) && !RegionManager.isParty(to, player)) {
            player.teleport(from);
        }
    }
}
