package ru.fozeton.training.Task2.Event;

import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamage implements Listener {
    @EventHandler
    public void onSpiderDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Spider spider) {
            if (event.getCause() == DamageCause.FALL) {
                if (!spider.getPassengers().isEmpty()) {
                    if (spider.getPassengers().getFirst() instanceof Player) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getCause() == DamageCause.SUFFOCATION || event.getCause() == DamageCause.FALL) {
                if(player.isInsideVehicle() && player.getVehicle() instanceof Spider){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player && PlayerInteract.ignorDamage.contains(player.getUniqueId())){
            event.setCancelled(true);
            PlayerInteract.ignorDamage.remove(player.getUniqueId());
        }
    }
}
