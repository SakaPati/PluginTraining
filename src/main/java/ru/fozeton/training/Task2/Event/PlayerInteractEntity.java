package ru.fozeton.training.Task2.Event;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.fozeton.training.Level2;

public class PlayerInteractEntity implements Listener {
    @EventHandler
    public void onSpiderClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked().getType() == EntityType.SPIDER) {
            Spider spider = (Spider) event.getRightClicked();

            if (spider.getPassengers().isEmpty()) {
                spider.addPassenger(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!spider.getPassengers().isEmpty()) {
                            Vector direction = player.getLocation().getDirection();
                            direction.multiply(0.2);
                            direction.setY(spider.getVelocity().getY());
                            float pYaw = player.getYaw();
                            spider.setRotation(pYaw, 0F);
                            spider.setVelocity(direction);
                        } else cancel();
                    }
                }.runTaskTimer(Level2.getInstance(), 0, 1);
            }
        }
    }
}
