package ru.fozeton.training.Task2.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.fozeton.training.Task2.PlayerScoreboard;

public class EntityDeath implements Listener {
    private final PlayerScoreboard sbManager;

    public EntityDeath(PlayerScoreboard sbManager) {
        this.sbManager = sbManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();
        if(player == null) return;
        sbManager.addStat(player, PlayerScoreboard.PlayerStat.KILLS);
    }
}
