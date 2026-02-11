package ru.fozeton.training.Task2.Event;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.Objects;
import java.util.UUID;

import static ru.fozeton.training.Task2.Constants.FriendMobs;

public class EntityTargetLivingEntity implements Listener {
    @EventHandler
    public void onZombieTarget(EntityTargetLivingEntityEvent event){
        if(event.getEntity() instanceof Zombie) {
            UUID mobId = event.getEntity().getUniqueId();
            UUID playerId = FriendMobs.get(mobId);

            if(event.getTarget() instanceof Player player) {
                if (player.getUniqueId().equals(playerId)){
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onSpiderTarget(EntityTargetLivingEntityEvent event){
        if(event.getEntity() instanceof Spider) {
            UUID mobId = event.getEntity().getUniqueId();
            UUID playerId = FriendMobs.get(mobId);

            if(event.getTarget() instanceof Player player) {
                if (player.getUniqueId().equals(playerId)){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onGuardianTarget(EntityTargetLivingEntityEvent event) {
        if(event.getEntity() instanceof Zombie guardian && guardian.customName() != null) {
            if (Objects.equals(guardian.customName(), Component.text("GUARDIAN"))) {
                event.setCancelled(true);
            }
        }
    }
}
